package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.Config;
import lkd.namsic.game.Variable;
import lkd.namsic.game.base.ConcurrentArrayList;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.LimitLong;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.event.DeathEvent;
import lkd.namsic.game.event.EarnEvent;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.event.MoveEvent;
import lkd.namsic.game.exception.InvalidNumberException;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.exception.UnhandledEnumException;
import lombok.Getter;
import lombok.Setter;

/*
    Must be Thread-Safe
    Entity class can be frequently changed
*/

@Getter
public abstract class Entity extends NamedObject {

    final LimitInteger lv = new LimitInteger(Config.MIN_LV, Config.MIN_LV, Config.MAX_LV);
    final LimitLong money = new LimitLong(0, 0L, null);

    final Location location = new Location();

    @Setter
    @NonNull
    Doing doing = Doing.NONE;

    final Set<Long> skill = new ConcurrentHashSet<>();

    final Map<StatType, Integer> basicStat = new ConcurrentHashMap<>();
    final Map<StatType, Integer> equipStat = new ConcurrentHashMap<>();
    final Map<StatType, Integer> buffStat = new ConcurrentHashMap<>();

    final Map<EquipType, Long> equip = new ConcurrentHashMap<>();
    final Map<Long, ConcurrentHashMap<StatType, Integer>> buff = new ConcurrentHashMap<>();
    final Map<Long, Integer> inventory = new ConcurrentHashMap<>();
    final Set<Long> equipInventory = new ConcurrentHashSet<>();

    final Map<Id, ConcurrentHashSet<Long>> enemies = new ConcurrentHashMap<>();
    final Map<String, ConcurrentArrayList<Event>> events = new ConcurrentHashMap<>();

    final Map<Variable, Object> variable = new ConcurrentHashMap<>();

    protected Entity(@NonNull String name) {
        super(name);
    }

    public boolean setMoney(long money) {
        boolean isCancelled = false;

        long gap = money - this.getMoney();
        if(gap > 0) {
            isCancelled = EarnEvent.handleEvent(this.events.get(EarnEvent.getName()), new Object[]{gap});
        }

        if (!isCancelled) {
            this.money.set(this.getMoney() + money);
        }

        return isCancelled;
    }

    public long getMoney() {
        return this.money.get();
    }

    public boolean addMoney(long money) {
        return this.setMoney(this.getMoney() + money);
    }

    public void dropMoney(long money) {
        long currentMoney = this.getMoney();

        if(money > currentMoney || money < 1) {
            throw new NumberRangeException(money, 1, currentMoney);
        }

        this.addMoney(-1 * money);
        MapClass map = Config.loadMap(this.location);
        map.addMoney(this.location, money);
        Config.unloadMap(map);
    }

    public boolean setField(int fieldX, int fieldY) {
        return setField(fieldX, fieldY, this.getFieldDistance(new Location(0, 0, fieldX, fieldY)));
    }

    public boolean setField(int fieldX, int fieldY, int distance) {
        if(distance < 0) {
            throw new NumberRangeException(distance, 0);
        }

        boolean isCancelled = MoveEvent.handleEvent(this.events.get(MoveEvent.getName()), new Object[]{distance, true});

        if(!isCancelled) {
            this.location.setField(fieldX, fieldY);

            MapClass map = null;

            try {
                map = Config.loadMap(this.location);

                long money = map.getMoney(this.location);
                if(money != 0) {
                    this.addMoney(money);
                    map.money.remove(this.location);
                }

                Map<Long, Integer> item = map.getItem().get(location);
                if(item != null) {
                    for(Map.Entry<Long, Integer> entry : item.entrySet()) {
                        this.addItem(entry.getKey(), entry.getValue());
                    }

                    map.item.remove(location);
                }

                Set<Long> equip = map.getEquip().get(location);
                if(equip != null) {
                    for (long equipId : equip) {
                        this.addEquip(equipId);
                    }

                    map.getEquip().remove(this.location);
                }
            } finally {
                if(map != null) {
                    Config.unloadMap(map);
                }
            }
        }

        return isCancelled;
    }

    public boolean setMap(int x, int y) {
        return this.setMap(new Location(x, y),true);
    }

    public boolean setMap(int x, int y, int fieldX, int fieldY) {
        return this.setMap(new Location(x, y, fieldX, fieldY), getMapDistance(new Location(x, y)), false);
    }

    public boolean setMap(Location location) {
        return this.setMap(location, false);
    }

    public boolean setMap(Location location, boolean isToBase) {
        return this.setMap(location, getMapDistance(location), isToBase);
    }

    public boolean setMap(Location location, int distance, boolean isToBase) {
        if(isToBase) {
            return this.setMap(location.getX().get(), location.getY().get(), 1, 1, distance,true);
        } else {
            return this.setMap(location.getX().get(), location.getY().get(),
                    location.getFieldX().get(), location.getFieldY().get(), distance, false);
        }
    }

    public boolean setMap(int x, int y, int fieldX, int fieldY, int distance, boolean isToBase) {
        if(distance <= 0) {
            throw new NumberRangeException(distance, 1);
        }

        boolean isCancelled = MoveEvent.handleEvent(this.events.get(MoveEvent.getName()), new Object[]{distance, false});

        if(!isCancelled) {
            MapClass prevMap = null;

            try {
                prevMap = Config.loadMap(this.location);
                prevMap.removeEntity(this);
            } finally {
                if(prevMap != null) {
                    Config.unloadMap(prevMap);
                }
            }

            MapClass moveMap = null;

            try {
                moveMap = Config.loadMap(x, y);

                if(this.lv.get() < moveMap.getRequireLv().get()) {
                    throw new NumberRangeException(this.lv.get(), moveMap.getRequireLv().get(), Config.MAX_LV);
                }

                this.location.setMap(x, y);
                if(isToBase) {
                    this.setField(moveMap.getLocation().getFieldX().get(), moveMap.getLocation().getFieldY().get());
                } else {
                    this.setField(fieldX, fieldY);
                }

                moveMap.addEntity(this);
            } finally {
                if(moveMap != null) {
                    Config.unloadMap(moveMap);
                }
            }
        }

        return isCancelled;
    }

    public void addSkill(@NonNull Set<Long> skill) {
        for(long skillId : skill) {
            this.addSkill(skillId);
        }
    }

    public void addSkill(long skillId) {
        Config.checkId(Id.SKILL, skillId);
        this.skill.add(skillId);
    }

    public boolean canAddSkill(long skillId) {
        Config.checkId(Id.SKILL, skillId);
        Skill skill = Config.getData(Id.SKILL, skillId);
        return this.checkStatRange(skill.getLimitStat().getMin(), skill.getLimitStat().getMax());
    }

    public boolean setBasicStat(@NonNull StatType statType, int stat) {
        this.revalidateBuff();

        boolean isCancelled = false;
        boolean isDeath = false;
        if(statType.equals(StatType.HP)) {
            int maxHp = this.getStat(StatType.MAXHP);

            if(stat > maxHp) {
                stat = maxHp;
            } else if(stat <= 0) {
                isDeath = true;
                isCancelled = DeathEvent.handleEvent(this.events.get(DeathEvent.getName()), new Object[]{this.getStat(StatType.HP), stat});
            }
        } else if(statType.equals(StatType.MN)) {
            int maxMn = this.getStat(StatType.MAXMN);

            if(stat > maxMn) {
                stat = maxMn;
            }
        }

        if(!isCancelled) {
            this.basicStat.put(statType, stat);

            if(isDeath) {
                this.onDeath();
            }
        }

        return isCancelled;
    }

    public int getBasicStat(@NonNull StatType statType) {
        return this.basicStat.getOrDefault(statType, 0);
    }

    public void addBasicStat(@NonNull StatType statType, int stat) {
        this.setBasicStat(statType, this.getBasicStat(statType) + stat);
    }

    public void setEquipStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat == 0) {
            this.equipStat.remove(statType);
        } else {
            this.equipStat.put(statType, stat);
        }
    }

    public int getEquipStat(@NonNull StatType statType) {
        return this.equipStat.getOrDefault(statType, 0);
    }

    public void addEquipStat(@NonNull StatType statType, int stat) {
        this.setEquipStat(statType, this.getEquipStat(statType) + stat);
    }

    public void setBuffStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat == 0) {
            this.buffStat.remove(statType);
        } else {
            this.buffStat.put(statType, stat);
        }
    }

    public int getBuffStat(@NonNull StatType statType) {
        return this.buffStat.getOrDefault(statType, 0);
    }

    public void addBuffStat(@NonNull StatType statType, int stat) {
        this.setBuffStat(statType, this.getBuffStat(statType) + stat);
    }

    public int getStat(@NonNull StatType statType) {
        int value = this.getBasicStat(statType) + this.getEquipStat(statType) + this.getBuffStat(statType);

        if(statType.equals(StatType.HP)) {
            int maxHp = this.getStat(StatType.MAXHP);

            if(maxHp < value) {
                this.setBasicStat(StatType.HP, maxHp);
                value = maxHp;
            }
        } else if(statType.equals(StatType.MN)) {
            int maxMn = this.getStat(StatType.MAXMN);

            if(maxMn < value) {
                this.setBasicStat(StatType.MN, maxMn);
                value = maxMn;
            }
        }

        return value;
    }

    public boolean compareStat(@NonNull Map<StatType, Integer> map) {
        return this.compareStat(map, true);
    }

    public boolean compareStat(@NonNull Map<StatType, Integer> map, boolean entityIsBig) {
        this.revalidateBuff();

        boolean flag;
        for(Map.Entry<StatType, Integer> entry : map.entrySet()) {
            flag = this.getStat(entry.getKey()) >= entry.getValue();

            if(entityIsBig != flag) {
                return false;
            }
        }

        return true;
    }

    public boolean checkStatRange(@NonNull Map<StatType, Integer> min, @NonNull Map<StatType, Integer> max) {
        this.revalidateBuff();

        Integer minValue, maxValue;
        int value;

        for(StatType statType : StatType.values()) {
            try {
                Config.checkStatType(statType);
            } catch (UnhandledEnumException e) {
                continue;
            }

            minValue = min.get(statType);
            maxValue = max.get(statType);
            value = this.getStat(statType);

            if(minValue != null && maxValue != null) {
                if(value > maxValue || value < minValue) {
                    return false;
                }
            }  else if(minValue == null && maxValue != null) {
                if(value > maxValue) {
                    return false;
                }
            } else if(minValue != null) {
                if(value < minValue) {
                    return false;
                }
            }
        }

        return true;
    }

    public abstract void onDeath();

    public abstract void onKill(Entity entity);

    public long getEquip(EquipType equipType) {
        return this.equip.getOrDefault(equipType, 0L);
    }

    @NonNull
    public EquipType equip(long equipId) {
        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
        EquipType equipType = equipment.getEquipType();

        if (equip.containsKey(equipType)) {
            this.unEquip(equipType);
        }

        for(StatType statType : StatType.values()) {
            this.setEquipStat(statType, this.getEquipStat(statType) + equipment.getStat(statType));
        }

        this.equip.put(equipType, equipId);

        return equipType;
    }

    public void unEquip(@NonNull EquipType equipType) {
        Long equipId = equip.get(equipType);
        if(equipId == null) {
            throw new ObjectNotFoundException(equipType);
        }

        Equipment equipment = Config.loadObject(Id.EQUIPMENT, equipId);

        for(StatType statType : StatType.values()) {
            this.setEquipStat(statType, this.getEquipStat(statType) - equipment.getStat(statType));
        }

        this.equip.remove(equipType);
    }

    public void setBuff(long time, @NonNull StatType statType, int stat) {
        long currentTime = System.currentTimeMillis();

        if(time < currentTime) {
            throw new NumberRangeException(time, currentTime);
        } else if(stat == 0) {
            throw new InvalidNumberException(stat);
        }

        ConcurrentHashMap<StatType, Integer> buffTimeMap = this.buff.get(time);
        if(buffTimeMap != null) {
            Integer buffStat = buffTimeMap.get(statType);

            if(buffStat == null) {
                buffTimeMap.put(statType, stat);
            } else {
                buffTimeMap.put(statType, buffStat + stat);
            }
        } else {
            buffTimeMap = new ConcurrentHashMap<>();
            buffTimeMap.put(statType, stat);

            this.buff.put(time, buffTimeMap);

            this.setBuffStat(statType, this.getBuffStat(statType) + stat);
        }
    }

    public int getBuff(long time, @NonNull StatType statType) {
        Map<StatType, Integer> buffMap = this.buff.get(time);

        if(buffMap != null) {
            return buffMap.getOrDefault(statType, 0);
        }

        return 0;
    }

    public void addBuff(long time, @NonNull StatType statType, int stat) {
        this.setBuff(time, statType, this.getBuff(time, statType) + stat);
    }

    public void revalidateBuff() {
        long time;
        long currentTime = System.currentTimeMillis();

        for(Map.Entry<Long, ConcurrentHashMap<StatType, Integer>> entry : this.buff.entrySet()) {
            time = entry.getKey();

            if(time < currentTime) {
                StatType statType;
                for(Map.Entry<StatType, Integer> buffEntry : entry.getValue().entrySet()) {
                    statType = buffEntry.getKey();
                    this.setBuffStat(statType, this.getBuffStat(statType) - buffEntry.getValue());
                }

                this.buff.remove(time);
            }
        }
    }

    public void setItem(long itemId, int count) {
        Config.checkId(Id.ITEM, itemId);

        if(count < 0) {
            throw new NumberRangeException(count, 0);
        }

        if(count == 0) {
            this.inventory.remove(itemId);
        } else {
            this.inventory.put(itemId, count);
        }
    }

    public int getItem(long itemId) {
        Config.checkId(Id.ITEM, itemId);
        return this.inventory.getOrDefault(itemId, 0);
    }

    public void addItem(long itemId, int count) {
        this.setItem(itemId, this.getItem(itemId) + count);
    }

    public void dropItem(long itemId, int count) {
        int itemCount = this.getItem(itemId);

        if(count > itemCount || count < 1) {
            throw new NumberRangeException(count, 1, itemCount);
        }

        this.addItem(itemId, -1 * count);
        MapClass map = Config.loadMap(this.location);
        map.addItem(this.location, itemId, count);
        Config.unloadMap(map);
    }

    public void addEquip(long equipId) {
        Config.checkId(Id.EQUIPMENT, equipId);

        Equipment newEquip = Config.newObject(Config.getData(Id.EQUIPMENT, equipId));
        long newEquipId = newEquip.getId().getObjectId();
        this.equipInventory.add(newEquipId);
        Config.unloadObject(newEquip);
    }

    public void removeEquip(long equipId) {
        if(!this.equipInventory.contains(equipId)) {
            throw new ObjectNotFoundException(Id.EQUIPMENT, equipId);
        }

        for(Map.Entry<EquipType, Long> entry : this.equip.entrySet()) {
            if(entry.getValue().equals(equipId)) {
                this.unEquip(entry.getKey());
                break;
            }
        }

        this.equipInventory.remove(equipId);
    }

    public void dropEquip(long equipId) {
        this.removeEquip(equipId);
        MapClass map = Config.loadMap(this.location);
        map.addEquip(this.location, equipId);
        Config.unloadMap(map);
    }

    public void addEnemy(@NonNull Id id, long objectId) {
        Id.checkEntityId(id);
        Config.checkId(id, objectId);

        ConcurrentHashSet<Long> enemySet = this.enemies.get(id);
        if(enemySet == null) {
            enemySet = new ConcurrentHashSet<>();
            enemySet.add(objectId);

            this.enemies.put(id, enemySet);
        } else {
            enemySet.add(objectId);
        }
    }

    public void removeEnemy(@NonNull Id id, long objectId) {
        Id.checkEntityId(id);
        Config.checkId(id, objectId);

        Set<Long> enemySet = Objects.requireNonNull(this.enemies.get(id));
        enemySet.remove(objectId);

        if(enemySet.isEmpty()) {
            this.enemies.remove(id);
        }
    }

    public void addEvent(@NonNull String eventName, @NonNull Event event) {
        ConcurrentArrayList<Event> eventSet = this.events.get(eventName);

        if(eventSet == null) {
            eventSet = new ConcurrentArrayList<>();
            eventSet.add(event);
            this.events.put(eventName, eventSet);
        } else {
            eventSet.add(event);
        }
    }

    public abstract boolean canFight(@NonNull Entity enemy);

    public boolean startFight(@NonNull Set<Entity> enemies) {
        for(Entity enemy : enemies) {
            if(!enemy.canFight(enemy)) {
                return false;
            }
        }

        this.setDoing(Doing.FIGHT);

        Id id = this.id.getId();
        long objectId = this.id.getObjectId();

        enemies.forEach(enemy -> {
            this.addEnemy(enemy.id.getId(), enemy.id.getObjectId());

            enemy.addEnemy(id, objectId);
            enemy.setDoing(Doing.FIGHT);

            if(enemy.id.getId().equals(Id.PLAYER)) {
                Player player = (Player) enemy;
                player.replyPlayer(this.getName() + " 와(과) 의 강제 전투가 시작되었습니다", player.getBattleMsg());
            }
        });

        return true;
    }

    public void endFight() {
        Id id;
        Entity enemy = null;

        for(Map.Entry<Id, ConcurrentHashSet<Long>> entry : this.enemies.entrySet()) {
            id = entry.getKey();

            for(long enemyId : entry.getValue()) {
                try {
                    enemy = Config.loadObject(id, enemyId);

                    this.removeEnemy(id, enemyId);
                    enemy.removeEnemy(this.id.getId(), this.id.getObjectId());
                    
                    if(id.equals(Id.PLAYER)) {
                        ((Player) enemy).replyPlayer("전투가 종료되었습니다");
                    }
                } finally {
                    if(enemy != null) {
                        Config.unloadObject(enemy);
                    }
                }
            }
        }

        if(this.id.getId().equals(Id.PLAYER)) {
            ((Player) this).replyPlayer("전투가 종료되었습니다");
        }
    }

    public boolean damage(Entity entity, int physicDmg, int magicDmg, int staticDmg, boolean canEvade) {
        Random random = new Random();

        this.revalidateBuff();

        boolean evade = true;
        if(canEvade) {
            evade = random.nextInt(100) < Math.min(
                    this.getStat(StatType.ACC) - entity.getStat(StatType.EVA), Config.MAX_EVADE);
        }

        if(evade) {
            int def = entity.getStat(StatType.DEF) - this.getStat(StatType.BRE);
            int mdef = entity.getStat(StatType.MDEF) - this.getStat(StatType.MBRE);

            physicDmg = Math.max(physicDmg - def, 0);
            magicDmg = Math.max(magicDmg - mdef, 0);

            int dra = physicDmg * this.getStat(StatType.DRA) / 100;
            int mdra = magicDmg * this.getStat(StatType.MDRA) / 100;

            int totalDmg = physicDmg + magicDmg + staticDmg;
            int totalDra = dra + mdra;

            entity.addBasicStat(StatType.HP, -1 * totalDmg);
            this.addBasicStat(StatType.HP, totalDra);

            int hp = entity.getStat(StatType.HP);
            int selfHp = this.getStat(StatType.HP);
            String innerMsg = "총 데미지 : " + totalDmg + "\n총 흡수량 : " + totalDra + "\n";

            if(hp == 0 || hp == 1 && entity.id.getId().equals(Id.PLAYER)) {
                this.onKill(entity);
            }

            if(this.id.getId().equals(Id.PLAYER)) {
                ((Player) this).replyPlayer("공격에 성공했습니다\n적 체력 : " + hp,
                        innerMsg + "남은 체력 : " + selfHp);
            }

            if(entity.id.getId().equals(Id.PLAYER)) {
                ((Player) entity).replyPlayer(this.getName() + " 에게 공격당했습니다!\n남은 체력 : " + hp,
                        innerMsg + "적 체력 : " + selfHp);
            }

            return true;
        } else {
            if(this.getId().getId().equals(Id.PLAYER)) {
                ((Player) this).replyPlayer("공격이 빗나갔습니다");
            }

            if(entity.id.getId().equals(Id.PLAYER)) {
                ((Player) entity).replyPlayer(this.getName() + " 의 공격을 피했습니다");
            }

            return false;
        }
    }

    public int getFieldDistance(Location location) {
        return (int) Math.sqrt(Math.pow(this.location.getFieldX().get() - this.location.getFieldX().get(), 2) +
                Math.pow(location.getFieldY().get() - this.location.getFieldX().get(), 2));
    }

    public int getMapDistance(Location location) {
        return (int) Math.sqrt(Math.pow(this.location.getX().get() - this.location.getX().get(), 2) +
                Math.pow(location.getY().get() - this.location.getY().get(), 2));
    }

    public void setVariable(Variable variable, Object value) {
        this.variable.put(variable, value);
    }

    public int getVariable(Variable variable) {
        Object value = this.variable.getOrDefault(variable, 0);

        if(value instanceof Double) {
            return ((Double) value).intValue();
        } else {
            return (int) value;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getObjectVariable(Variable variable) {
        return (T) this.variable.get(variable);
    }

    public List<Long> getListVariable(Variable variable) {
        List<String> list = this.getObjectVariable(variable);
        List<Long> outputList = new ArrayList<>();

        for(String element : list) {
            outputList.add(Long.parseLong(element));
        }

        this.setVariable(variable, outputList);
        return outputList;
    }

    public Map<Long, Integer> getMapVariable(Variable variable) {
        Map<String, Double> map = this.getObjectVariable(variable);
        Map<Long, Integer> outputMap = new HashMap<>();

        for(Map.Entry<String, Double> entry : map.entrySet()) {
            outputMap.put((long) Double.parseDouble(entry.getKey()), entry.getValue().intValue());
        }

        this.setVariable(variable, outputMap);
        return outputMap;
    }

    public void addVariable(Variable variable, int value) {
        this.setVariable(variable, this.getVariable(variable) + value);
    }

    @NonNull
    @Override
    public String getName() {
        return this.name + " (Lv." + this.getLv() + ")";
    }

}
