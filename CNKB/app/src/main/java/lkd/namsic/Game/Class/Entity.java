package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Base.ConcurrentArrayList;
import lkd.namsic.Game.Base.ConcurrentHashSet;
import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Base.LimitLong;
import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Doing;
import lkd.namsic.Game.Enum.EquipType;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Event.DeathEvent;
import lkd.namsic.Game.Event.EarnEvent;
import lkd.namsic.Game.Event.Event;
import lkd.namsic.Game.Event.MoveEvent;
import lkd.namsic.Game.Exception.CollectionAddFailedException;
import lkd.namsic.Game.Exception.InvalidNumberException;
import lkd.namsic.Game.Exception.NumberRangeException;
import lkd.namsic.Game.Exception.UnhandledEnumException;
import lombok.Getter;
import lombok.Setter;

/*
    Must be Thread-Safe
    Entity class can be frequently changed
*/

@Getter
public abstract class Entity implements GameObject {

    @Setter
    @NonNull
    String name;

    LimitInteger lv = new LimitInteger(Config.MIN_LV, Config.MIN_LV, Config.MAX_LV);
    LimitLong money = new LimitLong(0, 0L, Long.MAX_VALUE);

    Location location;

    @Setter
    @NonNull
    Doing doing;

    ConcurrentHashSet<Long> skill;

    ConcurrentHashMap<StatType, Integer> stat;
    ConcurrentHashMap<StatType, Integer> basicStat;
    ConcurrentHashMap<StatType, Integer> equipStat = new ConcurrentHashMap<>();
    ConcurrentHashMap<StatType, Integer> buffStat = new ConcurrentHashMap<>();

    ConcurrentHashMap<EquipType, Long> equip;
    ConcurrentHashMap<Long, ConcurrentHashMap<StatType, Integer>> buff;
    ConcurrentHashMap<Long, Integer> inventory;
    ConcurrentHashSet<Long> equipInventory;

    ConcurrentHashMap<Id, ConcurrentHashSet<Long>> enemies;

    ConcurrentHashMap<String, ConcurrentArrayList<Event>> events;

    protected Entity(@NonNull String name, int lv, long money, @NonNull Location location,
                     @NonNull Doing doing, @NonNull ConcurrentHashMap<StatType, Integer> basicStat,
                     @NonNull ConcurrentHashSet<Long> equip,
                     @NonNull ConcurrentHashMap<Long, ConcurrentHashMap<StatType, Integer>> buff,
                     @NonNull ConcurrentHashMap<Long, Integer> inventory,
                     @NonNull ConcurrentHashSet<Long> equipInventory,
                     @NonNull ConcurrentHashMap<Id, ConcurrentHashSet<Long>> enemies,
                     @NonNull ConcurrentHashMap<String, ConcurrentArrayList<Event>> events) {
        this.name = name;
        this.lv.set(lv);
        this.money.set(money);
        this.location = location;
        this.doing = doing;

        this.setBasicStat(basicStat);
        this.equip(equip);
        this.setBuff(buff);
        this.revalidateStat();

        this.setInventory(inventory);
        this.addEquip(equipInventory);

        this.addEnemy(enemies);
        this.addEvent(events);
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

    public void moveField(int fieldX, int fieldY) {
        this.setField(this.location.getFieldX().get() + fieldX,
                this.location.getFieldY().get() + fieldY, Math.abs(fieldX) + Math.abs(fieldY));
    }

    public void setField(int fieldX, int fieldY) {
        int xDis = Math.abs(this.location.getFieldX().get() - fieldX);
        int yDis = Math.abs(this.location.getFieldY().get() - fieldX);
        setField(fieldX, fieldY, xDis + yDis);
    }

    public boolean setField(int fieldX, int fieldY, int distance) {
        if(distance <= 0) {
            throw new NumberRangeException(distance, 1);
        }

        boolean isCancelled = MoveEvent.handleEvent(this.events.get(MoveEvent.getName()), new Object[]{distance, true});

        if(!isCancelled) {
            this.location.getFieldX().set(fieldX);
            this.location.getFieldY().set(fieldY);
        }

        return isCancelled;
    }

    public void moveMap(int worldX, int worldY) {
        this.setMap(this.location.getX().get() + worldX, this.location.getY().get() + worldY,
                Math.abs(worldX) + Math.abs(worldY));
    }

    public void setMap(int worldX, int worldY) {
        int xDis = Math.abs(this.location.getX().get() - worldX);
        int yDis = Math.abs(this.location.getY().get() - worldY);
        setMap(worldX, worldY, xDis + yDis);
    }

    public boolean setMap(int worldX, int worldY, int distance) {
        return this.setMap(worldX, worldY, this.location.getFieldX().get(), this.location.getFieldY().get(), distance);
    }

    public boolean setMap(int worldX, int worldY, int fieldX, int fieldY, int distance) {
        if(distance <= 0) {
            throw new NumberRangeException(distance, 1);
        }

        boolean isCancelled = MoveEvent.handleEvent(this.events.get(MoveEvent.getName()), new Object[]{distance, false});

        if(!isCancelled) {
            this.location.getFieldX().set(worldX);
            this.location.getFieldY().set(worldY);
            this.setField(fieldX, fieldY);

            this.loadOnSetMap(worldX, worldY, fieldX, fieldY);
        }

        return isCancelled;
    }

    public abstract void loadOnSetMap(int worldX, int worldY, int fieldX, int fieldY);

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
        return ((Skill) Config.getData(Id.SKILL, skillId)).getLimitStat().isInRange(this.stat);
    }

    public <T extends Entity> T setBasicStat(@NonNull Map<StatType, Integer> basicStat) {
        for(Map.Entry<StatType, Integer> entry : basicStat.entrySet()) {
            this.setBasicStat(entry.getKey(), entry.getValue());
        }

        return (T) this;
    }

    public <T extends Entity> T setBasicStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat < 0) {
            throw new NumberRangeException(stat, 0);
        }

        if(stat == 0) {
            this.basicStat.remove(statType);
        } else {
            this.basicStat.put(statType, stat);
        }

        return (T) this;
    }

    public int getBasicStat(@NonNull StatType statType) {
        Config.checkStatType(statType);
        Integer value = this.basicStat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public <T extends Entity> T addBasicStat(@NonNull StatType statType, int stat) {
        return this.setBasicStat(statType, this.getBasicStat(statType) + stat);
    }

    public <T extends Entity> T setEquipStat(@NonNull Map<StatType, Integer> equipStat) {
        for(Map.Entry<StatType, Integer> entry : equipStat.entrySet()) {
            this.setEquipStat(entry.getKey(), entry.getValue());
        }

        return (T) this;
    }

    public <T extends Entity> T setEquipStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat == 0) {
            this.equipStat.remove(statType);
        } else {
            this.equipStat.put(statType, stat);
        }

        return (T) this;
    }

    public int getEquipStat(@NonNull StatType statType) {
        Config.checkStatType(statType);
        Integer value = this.equipStat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public <T extends Entity> T addEquipStat(@NonNull StatType statType, int stat) {
        return this.setEquipStat(statType, this.getEquipStat(statType) + stat);
    }

    public <T extends Entity> T setBuffStat(@NonNull Map<StatType, Integer> buffStat) {
        for(Map.Entry<StatType, Integer> entry : buffStat.entrySet()) {
            this.setBuffStat(entry.getKey(), entry.getValue());
        }

        return (T) this;
    }

    public <T extends Entity> T setBuffStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat == 0) {
            this.buffStat.remove(statType);
        } else {
            this.buffStat.put(statType, stat);
        }

        return (T) this;
    }

    public int getBuffStat(@NonNull StatType statType) {
        Config.checkStatType(statType);
        Integer value = this.buffStat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public <T extends Entity> T addBuffStat(@NonNull StatType statType, int stat) {
        return this.setBuffStat(statType, this.getBuffStat(statType) + stat);
    }

    public boolean setStat(StatType statType, int stat) {
        boolean flag = false;

        try {
            Config.checkStatType(statType);
        } catch (UnhandledEnumException e) {
            flag = true;
        }

        if(!flag) {
            throw new UnhandledEnumException(statType);
        }

        boolean isCancelled = false;

        if(statType.equals(StatType.HP)) {
            int maxHp = this.getStat(StatType.MAXHP);

            if(stat > maxHp) {
                stat = maxHp;
            } else if(stat <= 0) {
                isCancelled = DeathEvent.handleEvent(this.events.get(DeathEvent.getName()), new Object[]{this.getStat(StatType.HP), stat});
            }
        } else if(statType.equals(StatType.MN)) {
            int maxMn = this.getStat(StatType.MAXMN);

            if(stat > maxMn) {
                stat = maxMn;
            }
        }

        if(!isCancelled) {
            this.stat.put(statType, stat);
        }

        return isCancelled;
    }

    public int getStat(@NonNull StatType statType) {
        Integer value = this.stat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public boolean addStat(@NonNull StatType statType, int stat) {
        return this.setStat(statType, this.getStat(statType) + stat);
    }

    public void equip(@NonNull Set<Long> equip) {
        List<EquipType> typeList = new ArrayList<>();

        EquipType equipType;
        for(long equipId : equip) {
            equipType = this.equip(equipId);

            if(typeList.contains(equipType)) {
                throw new CollectionAddFailedException("Equipment must exist only one per one EquipType", equip);
            } else {
                typeList.add(equipType);
            }
        }
    }

    public EquipType equip(long equipId) {
        Equipment equipment = Config.loadObject(Id.EQUIPMENT, equipId);
        EquipType equipType = equipment.getEquipType();

        if(equip.containsKey(equipType)) {
            this.unEquip(equipType);
        }

        StatType statType;
        for(Map.Entry<StatType, Integer> entry : equipment.stat.entrySet()) {
            statType = entry.getKey();
            this.setEquipStat(statType,  this.getEquipStat(statType) + entry.getValue());
        }

        this.equip.put(equipType, equipId);
        return equipType;
    }

    public void unEquip(@NonNull EquipType equipType) {
        Long equipId = equip.get(equipType);
        if(equipId == null) {
            return;
        }

        Equipment equipment = Config.loadObject(Id.EQUIPMENT, equipId);

        StatType statType;
        for(Map.Entry<StatType, Integer> entry : equipment.stat.entrySet()) {
            statType = entry.getKey();
            this.setEquipStat(statType, this.getEquipStat(statType) - entry.getValue());
        }

        this.equip.remove(equipType);
    }

    public void setBuff(@NonNull Map<Long, ConcurrentHashMap<StatType, Integer>> buff) {
        for(Map.Entry<Long, ConcurrentHashMap<StatType, Integer>> entry : buff.entrySet()) {
            this.setBuff(entry.getKey(), entry.getValue());
        }
    }

    public void setBuff(long time, @NonNull Map<StatType, Integer> buff) {
        for(Map.Entry<StatType, Integer> entry : buff.entrySet()) {
            this.setBuff(time, entry.getKey(), entry.getValue());
        }
    }

    public void setBuff(long time, @NonNull StatType statType, int stat) {
        long currentTime = System.currentTimeMillis();

        if(time < currentTime) {
            throw new NumberRangeException(time, currentTime);
        } else if(stat == 0) {
            throw new InvalidNumberException(stat);
        }

        revalidateBuff();

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
            Integer value = buffMap.get(statType);

            if(value != null) {
                return value;
            }
        }

        return 0;
    }

    public void addBuff(long time, @NonNull StatType statType, int stat) {
        this.setBuff(time, statType, this.getBuff(time, statType) + stat);
    }

    public void revalidateBuff() {
        long currentTime = System.currentTimeMillis();

        long time;
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

    public void setInventory(@NonNull Map<Long, Integer> inventory) {
        for(Map.Entry<Long, Integer> entry : inventory.entrySet()) {
            this.setItem(entry.getKey(), entry.getValue());
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
        Integer value = this.inventory.get(itemId);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void addItem(long itemId, int count) {
        this.setItem(itemId, this.getItem(itemId) + count);
    }

    public void addEquip(@NonNull Set<Long> equipInventory) {
        for(long equipId : equipInventory) {
            this.addEquip(equipId);
        }
    }

    public void addEquip(long equipId) {
        Config.checkId(Id.EQUIPMENT, equipId);
        this.equipInventory.add(equipId);
    }

    public void revalidateStat() {
        revalidateBuff();

        this.stat.clear();

        for(StatType statType : StatType.values()) {
            try {
                Config.checkStatType(statType);
            } catch (UnhandledEnumException e) {
                continue;
            }

            this.stat.put(statType, this.getBasicStat(statType) + this.getEquipStat(statType) + this.getBuffStat(statType));
        }
    }

    public void addEnemy(@NonNull Map<Id, ConcurrentHashSet<Long>> enemy) {
        for(Map.Entry<Id, ConcurrentHashSet<Long>> entry : enemy.entrySet()) {
            this.addEnemy(entry.getKey(), entry.getValue());
        }
    }

    public void addEnemy(@NonNull Id id, @NonNull ConcurrentHashSet<Long> enemy) {
        for(long enemyId : enemy) {
            this.addEnemy(id, enemyId);
        }
    }

    public void addEnemy(@NonNull Id id, long enemyId) {
        if(!(id.equals(Id.BOSS) || id.equals(Id.MONSTER) || id.equals(Id.PLAYER) || id.equals(Id.NPC))) {
            throw new UnhandledEnumException(id);
        }

        Config.checkId(id, enemyId);

        ConcurrentHashSet<Long> enemySet = this.enemies.get(id);
        if(enemySet == null) {
            enemySet = new ConcurrentHashSet<>();
            enemySet.add(enemyId);

            this.enemies.put(id, enemySet);
        } else {
            enemySet.add(enemyId);
        }
    }

    public void addEvent(@NonNull Map<String, ConcurrentArrayList<Event>> event) {
        for(Map.Entry<String, ConcurrentArrayList<Event>> entry : event.entrySet()) {
            this.addEvent(entry.getKey(), entry.getValue());
        }
    }

    public void addEvent(@NonNull String eventName, @NonNull List<Event> event) {
        for(Event e : event) {
            this.addEvent(eventName, e);
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

    public boolean canFight(@NonNull Entity enemy) {
        if(enemy instanceof Player) {
            if(!((Player) enemy).isPvp()) {
                return false;
            }
        }

        List<Doing> doingList = new ArrayList<>();
        doingList.add(Doing.BUY);
        doingList.add(Doing.CHAT);
        doingList.add(Doing.REINFORCE);

        if(this instanceof Player) {
            if(!((Player) this).isPvp()) {
                return false;
            }

            doingList.add(Doing.FIGHT);
        }

        return !doingList.contains(this.getDoing());
    }

    public boolean startFight(@NonNull Set<Entity> enemies) {
        for(Entity enemy : enemies) {
            if(!enemy.canFight(enemy)) {
                return false;
            }
        }

        this.setDoing(Doing.FIGHT);

        Id id = this.id.getId();
        long objectId = this.id.getObjectId();

        //noinspection SimplifyStreamApiCallChains
        enemies.stream().forEach(enemy -> {
            enemy.setDoing(Doing.FIGHT);
            this.addEnemy(enemy.id.getId(), enemy.id.getObjectId());

            enemy.addEnemy(id, objectId);
        });

        return true;
    }

}
