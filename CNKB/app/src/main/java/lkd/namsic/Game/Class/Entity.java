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
import lombok.AccessLevel;
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

    ConcurrentHashMap<Integer, Integer> variable;

    ConcurrentHashMap<String, ConcurrentArrayList<Event>> events;

    protected Entity(@NonNull String name, int lv, long money, @NonNull Location location,
                     @NonNull Doing doing, @NonNull ConcurrentHashMap<StatType, Integer> basicStat,
                     @NonNull ConcurrentHashSet<Long> equip,
                     @NonNull ConcurrentHashMap<Long, ConcurrentHashMap<StatType, Integer>> buff,
                     @NonNull ConcurrentHashMap<Long, Integer> inventory,
                     @NonNull ConcurrentHashSet<Long> equipInventory,
                     @NonNull ConcurrentHashMap<Integer, Integer> variable,
                     @NonNull ConcurrentHashMap<String, ConcurrentArrayList<Event>> events) {
        this.name = name;
        this.lv.set(lv);
        this.money.set(money);
        this.location = location;
        this.doing = doing;

        this.setBasicStat(basicStat);
        this.equip(equip);
        this.addBuff(buff);
        this.revalidateStat();

        this.setInventory(inventory);
        this.setEquipInventory(equipInventory);

        this.variable = variable;

        this.events = events;
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

    public <T extends Entity> T setBuffStat(@NonNull Map<StatType, Integer> buffStat) {
        for(Map.Entry<StatType, Integer> entry : basicStat.entrySet()) {
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

    public int getStat(StatType statType) {
        Integer value = this.stat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void addStat(StatType statType, int stat) {
        this.setStat(statType, this.getStat(statType) + stat);
    }

    public void equip(Set<Long> equip) {
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
        Equipment equipment = Config.getObject(Id.EQUIPMENT, equipId);
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

    public void unEquip(EquipType equipType) {
        Long equipId = equip.get(equipType);
        if(equipId == null) {
            return;
        }

        Equipment equipment = Config.getObject(Id.EQUIPMENT, equipId);

        StatType statType;
        for(Map.Entry<StatType, Integer> entry : equipment.stat.entrySet()) {
            statType = entry.getKey();
            this.setEquipStat(statType, this.getEquipStat(statType) - entry.getValue());
        }

        this.equip.remove(equipType);
    }

    public void addBuff(Map<Long, ConcurrentHashMap<StatType, Integer>> buff) {
        for(Map.Entry<Long, ConcurrentHashMap<StatType, Integer>> entry : buff.entrySet()) {
            this.addBuff(entry.getKey(), entry.getValue());
        }
    }

    public void addBuff(long time, Map<StatType, Integer> buff) {
        for(Map.Entry<StatType, Integer> entry : buff.entrySet()) {
            this.addBuff(time, entry.getKey(), entry.getValue());
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void addBuff(long time, StatType statType, int stat) {
        long currentTime = System.currentTimeMillis();

        if(time < currentTime) {
            throw new NumberRangeException(time, currentTime);
        } else if(stat == 0) {
            throw new InvalidNumberException(stat);
        }

        revalidateBuff();

        if(this.buff.containsKey(time)) {
            Integer buffStat = this.buff.get(time).get(statType);

            if(buffStat == null) {
                this.buff.get(time).put(statType, stat);
            } else {
                this.buff.get(time).put(statType, buffStat + stat);
            }
        } else {
            this.buff.put(time, new ConcurrentHashMap<StatType, Integer>());
            this.buff.get(time).put(statType, stat);

            this.setBuffStat(statType, this.getBuffStat(statType) + stat);
        }
    }

    public void setInventory(Map<Long, Integer> inventory) {
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

    public void setEquipInventory(Set<Long> equipInventory) {
        for(long equipId : equipInventory) {
            this.addEquipInventory(equipId);
        }
    }

    public void addEquipInventory(long equipId) {
        Config.checkId(Id.EQUIPMENT, equipId);

        this.equipInventory.add(equipId);
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

}
