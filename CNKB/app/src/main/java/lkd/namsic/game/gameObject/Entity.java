package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.base.Bool;
import lkd.namsic.game.base.ConcurrentArrayList;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.Int;
import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.LimitLong;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object_list.EquipList;
import lkd.namsic.game.event.DamageEvent;
import lkd.namsic.game.event.DeathEvent;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.event.MoneyChangeEvent;
import lkd.namsic.game.exception.InvalidNumberException;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.exception.UnhandledEnumException;
import lkd.namsic.game.manager.EquipManager;
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

    @Setter
    Location location = new Location();

    @Setter
    @NonNull
    Doing doing = Doing.NONE;

    final Set<Long> skill = new ConcurrentHashSet<>();

    final Map<StatType, Integer> basicStat = new ConcurrentHashMap<>();
    final Map<StatType, Integer> equipStat = new ConcurrentHashMap<>();
    final Map<StatType, Integer> buffStat = new ConcurrentHashMap<>();

    final Map<EquipType, Long> equipped = new ConcurrentHashMap<>();
    final Map<Long, Map<StatType, Integer>> buff = new ConcurrentHashMap<>();
    final Map<Long, Integer> inventory = new ConcurrentHashMap<>();
    final Set<Long> equipInventory = new ConcurrentHashSet<>();

    final Map<Id, ConcurrentHashSet<Long>> enemies = new ConcurrentHashMap<>();
    final Map<String, ConcurrentArrayList<Event>> events = new ConcurrentHashMap<>();
    final Map<EquipType, ConcurrentArrayList<Event>> equipEvents = new ConcurrentHashMap<>();

    final Map<Variable, Object> variable = new ConcurrentHashMap<>();

    final Location lastDeathLoc = new Location();
    long lastDropMoney = 0L;
    final Map<Long, Integer> lastDropItem = new HashMap<>();
    final Set<Long> lastDropEquip = new HashSet<>();

    protected Entity(@NonNull String name) {
        super(name);
        this.setBasicStat(StatType.ATS, 100);
    }

    public int getMovableDistance() {
        return (int) Math.sqrt(2 + this.getStat(StatType.AGI) / 4D);
    }

    @NonNull
    public String getDisplayHp() {
        return this.getDisplayHp(this.getStat(StatType.HP));
    }

    @NonNull
    public String getDisplayHp(int hp) {
        return Config.getBar(hp, this.getStat(StatType.MAXHP));
    }

    @NonNull
    public String getDisplayMn() {
        return Config.getBar(this.getStat(StatType.MN), this.getStat(StatType.MAXMN));
    }

    public boolean setMoney(long money) {
        boolean isCancelled = MoneyChangeEvent.handleEvent(this,
                this.getEvents(MoneyChangeEvent.class.getName()), money);
        if (!isCancelled) {
            this.money.set(money);
        }

        return isCancelled;
    }

    public long getMoney() {
        return this.money.get();
    }

    public boolean addMoney(long money) {
        money *= Config.MONEY_BOOST;
        return this.setMoney(this.getMoney() + money);
    }

    public void dropMoney(long money) {
        long currentMoney = this.getMoney();

        if(money > currentMoney || money < 1) {
            throw new NumberRangeException(money, 1, currentMoney);
        }

        this.lastDropMoney = money;
        this.addMoney(money * -1);

        GameMap map = Config.loadMap(this.location);
        map.addMoney(this, money);
        Config.unloadMap(map);
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
                isCancelled = DeathEvent.handleEvent(this,
                        this.getEvents(DeathEvent.class.getName()), this.getStat(StatType.HP), stat);
            }
        } else if(statType.equals(StatType.MN)) {
            int maxMn = this.getStat(StatType.MAXMN);

            if(stat > maxMn) {
                stat = maxMn;
            }
        } else if(statType.equals(StatType.MAXHP)) {
            if(stat < 1) {
                throw new NumberRangeException(stat, 1);
            }
        } else if(statType.equals(StatType.MAXMN)) {
            if(stat < 0) {
                throw new NumberRangeException(stat, 0);
            }
        }

        if(!isCancelled) {
            this.basicStat.put(statType, stat);

            if(isDeath) {
                this.onDeath();
                return true;
            }
        }

        return false;
    }

    public int getBasicStat(@NonNull StatType statType) {
        return this.basicStat.getOrDefault(statType, 0);
    }

    public boolean addBasicStat(@NonNull StatType statType, int stat) {
        return this.setBasicStat(statType, this.getBasicStat(statType) + stat);
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

    public void onDeath() {
        this.lastDeathLoc.set(this.location);
        this.lastDropMoney = 0L;
        this.lastDropItem.clear();
        this.lastDropEquip.clear();
    }

    public abstract void onKill(@NonNull Entity entity);

    public long getEquipped(@NonNull EquipType equipType) {
        return this.equipped.getOrDefault(equipType, EquipList.NONE.getId());
    }

    public void setBuff(long time, @NonNull StatType statType, int stat) {
        long currentTime = System.currentTimeMillis();

        if(time < currentTime) {
            throw new NumberRangeException(time, currentTime);
        } else if(stat == 0) {
            throw new InvalidNumberException(stat);
        }

        Map<StatType, Integer> buffTimeMap = this.buff.get(time);
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
        }

        this.setBuffStat(statType, this.getBuffStat(statType) + stat);
    }

    public int getBuff(long time, @NonNull StatType statType) {
        Map<StatType, Integer> buffMap = this.buff.get(time);

        if(buffMap != null) {
            return buffMap.getOrDefault(statType, 0);
        }

        return 0;
    }

    public void addBuff(long duration, @NonNull StatType statType, int stat) {
        this.setBuff(duration, statType, this.getBuff(duration, statType) + stat);
    }

    public void revalidateBuff() {
        if(!this.buff.isEmpty()) {
            long currentTime = System.currentTimeMillis();

            Set<Long> removeSet = new HashSet<>();
            for (Map.Entry<Long, Map<StatType, Integer>> entry : this.buff.entrySet()) {
                long time = entry.getKey();

                if (time < currentTime) {
                    StatType statType;
                    for (Map.Entry<StatType, Integer> buffEntry : entry.getValue().entrySet()) {
                        statType = buffEntry.getKey();
                        this.setBuffStat(statType, this.getBuffStat(statType) - buffEntry.getValue());
                    }

                    removeSet.add(time);
                }
            }

            for(long time : removeSet) {
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
        this.addItem(itemId, count, count > 0);
    }

    public void addItem(long itemId, int count, boolean print) {
        int currCount = this.getItem(itemId) + count;
        this.setItem(itemId, currCount);

        if(print && this.id.getId().equals(Id.PLAYER)) {
            Item item = Config.getData(Id.ITEM, itemId);
            ((Player) this).replyPlayer(item.getName() + " " + count + "개를 획득헀습니다\n" +
                    "현재 개수: " + currCount);
        }
    }

    public void dropItem(long itemId, int count) {
        int itemCount = this.getItem(itemId);

        if(count > itemCount || count < 1) {
            throw new NumberRangeException(count, 1, itemCount);
        }

        this.addItem(itemId, count * -1);
        this.lastDropItem.put(itemId, this.lastDropItem.getOrDefault(itemId, 0) + count);

        GameMap map = Config.loadMap(this.location);
        map.addItem(this, itemId, count);
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

        for(Map.Entry<EquipType, Long> entry : this.equipped.entrySet()) {
            if(entry.getValue().equals(equipId)) {
                EquipManager.getInstance().unEquip(this, entry.getKey());
                break;
            }
        }

        this.equipInventory.remove(equipId);
    }

    public void dropEquip(long equipId) {
        this.removeEquip(equipId);
        this.lastDropEquip.add(equipId);

        GameMap map = Config.loadMap(this.location);
        map.addEquip(this, equipId);
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

        Set<Long> enemySet = this.enemies.get(id);
        if(enemySet == null) {
            return;
        }

        enemySet.remove(objectId);

        if(enemySet.isEmpty()) {
            this.enemies.remove(id);
        }
    }

    @NonNull
    public List<Event> getEvents(@NonNull String eventName) {
        List<Event> eventList = this.events.get(eventName);

        if(eventList == null) {
            eventList = new ArrayList<>();
        } else {
            eventList = new ArrayList<>(eventList);
        }

        for(Map.Entry<EquipType, ConcurrentArrayList<Event>> entry : this.equipEvents.entrySet()) {
            for(Event event : entry.getValue()) {
                if(event.getClassName().equals(eventName)) {
                    eventList.add(event);
                }
            }
        }

        return eventList;
    }

    public void addEvent(@NonNull Event event) {
        String eventName = event.getClassName();
        ConcurrentArrayList<Event> eventSet = this.events.get(eventName);

        if(eventSet == null) {
            eventSet = new ConcurrentArrayList<>();
            eventSet.add(event);
            this.events.put(eventName, eventSet);
        } else {
            eventSet.add(event);
        }
    }

    public boolean canFight(@NonNull Entity enemy) {
        List<Doing> fightableList = Doing.fightableList();
        return fightableList.contains(this.doing) && fightableList.contains(enemy.doing);
    }

    public boolean attack(@NonNull Entity entity, boolean printOnDeath) {
        return this.damage(entity, new Int(this.getStat(StatType.ATK)), new Int(), new Int(), true, printOnDeath);
    }

    public boolean damage(@NonNull Entity entity, @NonNull Int physicDmg, @NonNull Int magicDmg, @NonNull Int staticDmg,
                          boolean canEvade, boolean printOnDeath) {
        Random random = new Random();

        this.revalidateBuff();

        if(entity instanceof Monster && this.id.getId().equals(Id.PLAYER)) {
            ((Monster) entity).getAngryPlayers().add(this.id.getObjectId());
        }

        boolean evade = false;
        if(canEvade) {
            evade = random.nextInt(100) < Math.min(entity.getStat(StatType.EVA) - this.getStat(StatType.ACC), Config.MAX_EVADE);
        }

        if(!evade) {
            int def = Math.max(entity.getStat(StatType.DEF) - this.getStat(StatType.BRE), 0);
            int mdef = Math.max(entity.getStat(StatType.MDEF) - this.getStat(StatType.MBRE), 0);

            physicDmg.set(Math.max(physicDmg.get() - def, 0));
            magicDmg.set(Math.max(magicDmg.get() - mdef, 0));

            int heal = 0;
            boolean isDefencing = entity.getObjectVariable(Variable.IS_DEFENCING, false);
            if(isDefencing) {
                heal = (int) (physicDmg.get() * (new Random().nextInt(41) + 30) / 100D);
                physicDmg.set(0);

                entity.addBasicStat(StatType.HP, heal);
                entity.setVariable(Variable.IS_DEFENCING, false);
            }

            Int dra = new Int(physicDmg.get() * Math.min(this.getStat(StatType.DRA), 100) / 100);
            Int mdra = new Int(magicDmg.get() * Math.min(this.getStat(StatType.MDRA), 100) / 100);

            Int totalDmg = new Int(physicDmg.get() + magicDmg.get() + staticDmg.get());
            Int totalDra = new Int(dra.get() + mdra.get());

            Bool isCrit = new Bool();
            int agi = this.getStat(StatType.AGI);
            if(random.nextDouble() < agi * Config.CRIT_PER_AGI) {
                isCrit.set(true);
                totalDmg.multiple(2);
            }

            boolean isCancelled = DamageEvent.handleEvent(this,
                    this.getEvents(DamageEvent.getName()), entity, totalDmg, totalDra, isCrit);

            if(!isCancelled) {
                boolean isDeath = entity.addBasicStat(StatType.HP, -1 * totalDmg.get());
                this.addBasicStat(StatType.HP, totalDra.get());

                String hp;
                if (isDeath) {
                    hp = entity.getDisplayHp(0);
                } else {
                    hp = entity.getDisplayHp();
                }

                if (!isDeath || printOnDeath) {
                    String selfHp = this.getDisplayHp();
                    String innerMsg = "총 데미지: " + totalDmg + "\n총 흡수량: " + totalDra + "\n";

                    if (this.id.getId().equals(Id.PLAYER)) {
                        String msg = "공격에 성공했습니다\n적 체력: " + hp;
                        if (isCrit.get()) {
                            msg = "[치명타!] " + msg;
                        }

                        ((Player) this).replyPlayer(msg, innerMsg + "남은 체력: " + selfHp);
                    }

                    //Used instanceof because of warning
                    if (entity instanceof Player) {
                        String msg = this.getName() + " 에게 공격당했습니다!\n남은 체력: " + hp;
                        if (isCrit.get()) {
                            msg = "[치명타!] " + msg;
                        }

                        if (isDefencing) {
                            msg += "\n방어로 인해 회복한 체력: " + heal;
                        }

                        ((Player) entity).replyPlayer(msg, innerMsg + "적 체력: " + selfHp);
                    }
                }

                if (isDeath) {
                    this.onKill(entity);
                    return true;
                }
            }
        } else {
            if (this.getId().getId().equals(Id.PLAYER)) {
                ((Player) this).replyPlayer("공격이 빗나갔습니다");
            }

            //Used instanceof because of warning
            if (entity instanceof Player) {
                ((Player) entity).replyPlayer(this.getName() + " 의 공격을 피했습니다");
            }
        }

        return false;
    }

    public int getFieldDistance(Location location) {
        return (int) Math.sqrt(Math.pow(this.location.getFieldX().get() - location.getFieldX().get(), 2) +
                Math.pow(this.location.getFieldY().get() - location.getFieldX().get(), 2));
    }

    public int getMapDistance(Location location) {
        return (int) Math.sqrt(Math.pow(this.location.getX().get() - location.getX().get(), 2) +
                Math.pow(this.location.getY().get() - location.getY().get(), 2));
    }

    public void setVariable(Variable variable, Object value) {
        this.variable.put(variable, value);
    }

    public void removeVariable(Variable variable) {
        this.variable.remove(variable);
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
    @Nullable
    public <T> T getObjectVariable(Variable variable) {
        return (T) this.variable.get(variable);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T> T getObjectVariable(Variable variable, T defaultValue) {
        return (T) this.variable.getOrDefault(variable, defaultValue);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public List<Long> getListVariable(Variable variable) {
        List<?> list = this.getObjectVariable(variable);
        List<Long> outputList = new ArrayList<>();

        if(list == null) {
            outputList = new ArrayList<>();
            this.setVariable(variable, outputList);
            return outputList;
        }

        if(!list.isEmpty()) {
            if(list.toArray()[0] instanceof Double) {
                for (Object element : list) {
                    outputList.add(((Double) element).longValue());
                }
            } else {
                return (List<Long>) list;
            }
        }

        this.setVariable(variable, outputList);
        return outputList;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public Map<Long, Integer> getMapVariable(Variable variable) {
        Map<?, ?> map = this.getObjectVariable(variable);
        Map<Long, Integer> outputMap = new HashMap<>();

        if(map == null) {
            outputMap = new HashMap<>();
            this.setVariable(variable, outputMap);
            return outputMap;
        }

        if(!map.isEmpty()) {
            if(map.keySet().toArray()[0] instanceof String) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    outputMap.put((long) Double.parseDouble((String) entry.getKey()), ((Double) entry.getValue()).intValue());
                }
            } else {
                return (Map<Long, Integer>) map;
            }
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
        return this.name + " (Lv." + this.getLv().get() + ")";
    }

    @NonNull
    public String getRealName() {
        return this.name;
    }

}
