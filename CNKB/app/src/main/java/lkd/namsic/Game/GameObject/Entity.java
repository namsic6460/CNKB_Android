package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import lkd.namsic.Game.Exception.ObjectNotFoundException;
import lkd.namsic.Game.Exception.UnhandledEnumException;
import lombok.Getter;
import lombok.Setter;

/*
    Must be Thread-Safe
    Entity class can be frequently changed
*/

@Getter
public class Entity extends NamedObject {
public abstract class Entity extends NamedObject {

    protected boolean skipRevalidate = false;

    LimitInteger lv = new LimitInteger(Config.MIN_LV, Config.MIN_LV, Config.MAX_LV);
    LimitLong money = new LimitLong(0, 0L, Long.MAX_VALUE);

    Location location = new Location();

    @Setter
    @NonNull
    Doing doing = Doing.NONE;

    Set<Long> skill = new ConcurrentHashSet<>();

    Map<StatType, Integer> stat = new ConcurrentHashMap<>();
    Map<StatType, Integer> basicStat = new ConcurrentHashMap<>();
    Map<StatType, Integer> equipStat = new ConcurrentHashMap<>();
    Map<StatType, Integer> buffStat = new ConcurrentHashMap<>();

    Map<EquipType, Long> equip = new ConcurrentHashMap<>();
    Map<Long, ConcurrentHashMap<StatType, Integer>> buff = new ConcurrentHashMap<>();
    Map<Long, Integer> inventory = new ConcurrentHashMap<>();
    Set<Long> equipInventory = new ConcurrentHashSet<>();

    Map<Id, ConcurrentHashSet<Long>> enemies = new ConcurrentHashMap<>();
    Map<String, ConcurrentArrayList<Event>> events = new ConcurrentHashMap<>();

    protected Entity(@NonNull String name) {
        super(name);
    }

    public void setSkipRevalidate(boolean skipRevalidate) {
        this.skipRevalidate = skipRevalidate;

        if(!skipRevalidate) {
            this.revalidateStat();
        }
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
        MapClass map = Config.loadMap(this.location.getX().get(), this.location.getY().get());
        map.addMoney(this.location, money);
        Config.unloadMap(map);
    }

    public boolean moveField(int fieldX, int fieldY) {
        return this.setField(this.location.getFieldX().get() + fieldX,
                this.location.getFieldY().get() + fieldY, Math.abs(fieldX) + Math.abs(fieldY));
    }

    public boolean setField(int fieldX, int fieldY) {
        int xDis = Math.abs(this.location.getFieldX().get() - fieldX);
        int yDis = Math.abs(this.location.getFieldY().get() - fieldX);
        return setField(fieldX, fieldY, xDis + yDis);
    }

    public boolean setField(int fieldX, int fieldY, int distance) {
        if(distance < 0) {
            throw new NumberRangeException(distance, 0);
        }

        boolean isCancelled = MoveEvent.handleEvent(this.events.get(MoveEvent.getName()), new Object[]{distance, true});

        if(!isCancelled) {
            this.location.setField(fieldX, fieldY);
        }

        return isCancelled;
    }

    public boolean moveMap(int x, int y) {
        return this.setMap(this.location.getX().get() + x, this.location.getY().get() + y,
                Math.abs(x) + Math.abs(y));
    }

    public boolean setMap(int x, int y) {
        int xDis = Math.abs(this.location.getX().get() - x);
        int yDis = Math.abs(this.location.getY().get() - y);
        return setMap(x, y, xDis + yDis);
    }

    public boolean setMap(int worldX, int worldY, int distance) {
        return this.setMap(worldX, worldY, this.location.getFieldX().get(), this.location.getFieldY().get(), distance);
    }

    public boolean setMap(int x, int y, int fieldX, int fieldY, int distance) {
        if(distance <= 0) {
            throw new NumberRangeException(distance, 1);
        }

        boolean isCancelled = MoveEvent.handleEvent(this.events.get(MoveEvent.getName()), new Object[]{distance, false});

        if(!isCancelled && !this.setField(fieldX, fieldY)) {
            MapClass map = Config.loadMap(this.location.getX().get(), this.location.getY().get());
            map.removeEntity(this.id.getId(), this.id.getObjectId());
            Config.unloadMap(map);

            this.location.setMap(x, y);

            map = Config.loadMap(x, y);
            map.addEntity(this.id.getId(), this.id.getObjectId());

            if(this instanceof AiEntity) {
                Config.unloadMap(map);
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
        return ((Skill) Config.getData(Id.SKILL, skillId)).getLimitStat().isInRange(this.stat);
    }

    public <T extends Entity> T setBasicStat(@NonNull Map<StatType, Integer> basicStat) {
    public void setBasicStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat == 0) {
            this.basicStat.remove(statType);
        } else {
            this.basicStat.put(statType, stat);
        }

        return (T) this;
        this.revalidateStat();
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
    public void addBasicStat(@NonNull StatType statType, int stat) {
        this.setBasicStat(statType, this.getBasicStat(statType) + stat);
    }

    public <T extends Entity> T setEquipStat(@NonNull Map<StatType, Integer> equipStat) {
        for(Map.Entry<StatType, Integer> entry : equipStat.entrySet()) {
            this.setEquipStat(entry.getKey(), entry.getValue());
        }

        return (T) this;
    }

    public <T extends Entity> T setEquipStat(@NonNull StatType statType, int stat) {
    public void setEquipStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat == 0) {
            this.equipStat.remove(statType);
        } else {
            this.equipStat.put(statType, stat);
        }

        return (T) this;
        this.revalidateStat();
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

    public void addEquipStat(@NonNull StatType statType, int stat) {
        this.setEquipStat(statType, this.getEquipStat(statType) + stat);
    }

            this.setBuffStat(entry.getKey(), entry.getValue());
        }

        return (T) this;
    }

    public <T extends Entity> T setBuffStat(@NonNull StatType statType, int stat) {
    public void setBuffStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat == 0) {
            this.buffStat.remove(statType);
        } else {
            this.buffStat.put(statType, stat);
        }

        return (T) this;
        this.revalidateStat();
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
    public void addBuffStat(@NonNull StatType statType, int stat) {
        this.setBuffStat(statType, this.getBuffStat(statType) + stat);
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
            if(isDeath) {
                this.death();
            }

            this.stat.put(statType, stat);
        }

        return isCancelled;
    }

    public void death() {
        Id id;
        Map<Id, Set<Long>> enemyCopy = new HashMap<>(this.enemies);
        for(Map.Entry<Id, Set<Long>> entry : enemyCopy.entrySet()) {
            id = entry.getKey();

            Entity entity = null;
            for(long objectId : entry.getValue()) {
                try {
                    entity = Config.loadObject(id, objectId);
                    entity.removeEnemy(id, objectId);
                } finally {
                    if(entity != null) {
                        Config.unloadObject(entity);
                    }
                }
            }
        }
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

    @NonNull
    public EquipType equip(long equipId) {
        Equipment equipment = null;
        EquipType equipType = null;
        EquipType equipType;

        try {
            equipment = Config.loadObject(Id.EQUIPMENT, equipId);
            equipType = equipment.getEquipType();

            this.setSkipRevalidate(true);

            if (equip.containsKey(equipType)) {
                this.unEquip(equipType);
            }

            StatType statType;
            for (Map.Entry<StatType, Integer> entry : equipment.stat.entrySet()) {
                statType = entry.getKey();
                this.setEquipStat(statType, this.getEquipStat(statType) + entry.getValue());
            }

            this.setSkipRevalidate(false);

            this.equip.put(equipType, equipId);
        } finally {
            if(equipment != null) {
                Config.unloadObject(equipment);
            }
        }

        return equipType;
    }

    public void unEquip(@NonNull EquipType equipType) {
        Long equipId = equip.get(equipType);
        if(equipId == null) {
            return;
            throw new ObjectNotFoundException(equipType);
        }

        Equipment equipment = null;

        try {
            equipment = Config.loadObject(Id.EQUIPMENT, equipId);

            StatType statType;
            for (Map.Entry<StatType, Integer> entry : equipment.stat.entrySet()) {
                statType = entry.getKey();
                this.setEquipStat(statType, this.getEquipStat(statType) - entry.getValue());
            }

            this.equip.remove(equipType);
        } finally {
            if(equipment != null) {
                Config.unloadObject(equipment);
            }
        }
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
        if(skipRevalidate) {
            return;
        }

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

    public void dropItem(long itemId, int count) {
        int itemCount = this.getItem(itemId);

        if(count > itemCount || count < 1) {
            throw new NumberRangeException(count, 1, itemCount);
        }

        this.addItem(itemId, -1 * count);
        MapClass map = Config.loadMap(this.location.getX().get(), this.location.getY().get());
        map.addItem(this.location, itemId, count);
        Config.unloadMap(map);
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

    public void removeEquip(long equipId) {
        if(!this.equipInventory.contains(equipId)) {
            throw new ObjectNotFoundException(Id.EQUIPMENT, equipId);
        }

        this.equipInventory.remove(equipId);
    }

    public void dropEquip(long equipId) {
        this.removeEquip(equipId);
        MapClass map = Config.loadMap(this.location.getX().get(), this.location.getY().get());
        map.addEquip(this.location, equipId);
        Config.unloadMap(map);
    }

    public void revalidateStat() {
        if(skipRevalidate) {
            return;
        }

        revalidateBuff();

        this.stat.clear();

        int stat;
        for(StatType statType : StatType.values()) {
            try {
                Config.checkStatType(statType);
            } catch (UnhandledEnumException e) {
                continue;
            }

            stat = this.getBasicStat(statType) + this.getEquipStat(statType) + this.getBuffStat(statType);
            if(statType.equals(StatType.MAXHP) || statType.equals(StatType.MAXMN)) {
                stat = Math.max(stat, 1);
            } else {
                stat = Math.max(stat, 0);
            }

            this.stat.put(statType, stat);
        }
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
        return !Doing.nonFightList().contains(this.getDoing());
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
