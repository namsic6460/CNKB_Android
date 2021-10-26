package lkd.namsic.game.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.base.Bool;
import lkd.namsic.game.base.ConcurrentArrayList;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.IdClass;
import lkd.namsic.game.base.Int;
import lkd.namsic.game.base.LoNg;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.EventList;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.event.DamageEvent;
import lkd.namsic.game.event.DamagedEvent;
import lkd.namsic.game.event.DeathEvent;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.event.MoneyChangeEvent;
import lkd.namsic.game.event.PreDamageEvent;
import lkd.namsic.game.event.PreDamagedEvent;
import lkd.namsic.game.event.PreEvadeEvent;
import lkd.namsic.game.exception.InvalidNumberException;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.UnhandledEnumException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.implement.EntityEvents;
import lombok.Getter;
import lombok.Setter;

/*
    Must be Thread-Safe
    Entity class can be frequently changed
*/

@Getter
public abstract class Entity extends NamedObject {

    @Setter
    double version = Config.VERSION;

    @Setter
    int lv = 1;

    long money = 0;

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

    final Map<String, ConcurrentArrayList<Long>> event = new ConcurrentHashMap<>();
    final Map<EquipType, ConcurrentHashSet<String>> removedEquipEvent = new ConcurrentHashMap<>();

    final Map<Variable, Object> variable = new ConcurrentHashMap<>();

    final Location lastDeathLoc = new Location();
    long lastDropMoney = 0L;
    final Map<Long, Integer> lastDropItem = new HashMap<>();
    final Set<Long> lastDropEquip = new HashSet<>();
    boolean lastAttackSuccess = false;

    int lastDamage = 0;
    int lastDrain = 0;
    boolean isLastCrit = false;
    int lastHeal = 0;

    @Setter
    @Nullable
    IdClass killer = null;

    protected Entity(@NonNull String name) {
        super(name);
        this.setBasicStat(StatType.ATS, 100);
    }

    public int getMovableDistance() {
        return (int) Math.sqrt(2 + this.getStat(StatType.AGI) / 4D);
    }

    @NonNull
    public String getDisplayHp() {
        int maxHp = this.getStat(StatType.MAXHP);

        if(this.getKiller() == null) {
            return Config.getBar(Math.max(this.getStat(StatType.HP), 0), maxHp);
        } else {
            return Config.getBar(0, maxHp);
        }
    }

    @NonNull
    public String getDisplayMn() {
        return Config.getBar(this.getStat(StatType.MN), this.getStat(StatType.MAXMN));
    }

    public void setMoney(long money) {
        LoNg wrappedMoney = new LoNg(money);

        String eventName = MoneyChangeEvent.class.getName();
        MoneyChangeEvent.handleEvent(this, this.event.get(eventName), this.getEquipEvents(eventName), wrappedMoney);

        this.money = wrappedMoney.get();
    }

    public void addMoney(long money) {
        money *= Config.MONEY_BOOST;
        this.setMoney(this.getMoney() + money);
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
        SkillList skillData = Objects.requireNonNull(SkillList.idMap.get(skillId));
        this.skill.add(skillData.getId());

        List<Long> eventList = skillData.getEventList();
        if(!eventList.isEmpty()) {
            for(long eventId : eventList) {
                this.addEvent(EventList.findById(eventId));
            }
        }
    }

    public boolean setBasicStat(@NonNull StatType statType, int stat) {
        this.revalidateBuff();

        boolean isDeath = false;
        if(statType.equals(StatType.HP)) {
            int maxHp = this.getStat(StatType.MAXHP);

            if(stat > maxHp) {
                stat = maxHp;
            } else if(stat <= 0) {
                Int afterHp = new Int(stat);

                String eventName = DeathEvent.getName();
                DeathEvent.handleEvent(this, this.event.get(eventName), this.getEquipEvents(eventName),
                        this.getStat(StatType.HP), afterHp);

                stat = afterHp.get();
                if(stat <= 0) {
                    isDeath = true;
                }
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

        this.basicStat.put(statType, stat);

        return isDeath;
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

    public void revalidateEquipStat() {
        this.equipStat.clear();

        int stat;
        Equipment equipment;
        for(long equipId : this.getEquipped().values()) {
            equipment = Config.getData(Id.EQUIPMENT, equipId);

            for(StatType statType : StatType.values()) {
                try {
                    Config.checkStatType(statType);
                } catch (UnhandledEnumException e) {
                    continue;
                }

                if ((stat = equipment.getStat(statType)) != 0) {
                    this.addEquipStat(statType, stat);
                }
            }
        }
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
        } else if(statType.equals(StatType.AGI)) {
            value = Math.min(Config.MAX_AGI, value);
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

    public boolean isEquipped(@NonNull EquipType equipType, long equipId) {
        return this.getEquipped(equipType) == equipId;
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
            ((Player) this).replyPlayer(item.getName() + " " + count + "개를 획득했습니다\n" +
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

    public void addBasicEquip(long equipId) {
        this.equipInventory.add(equipId);
    }

    public void addEquip(long equipId) {
        Equipment newEquip = Config.newObject(Config.getData(Id.EQUIPMENT, equipId), true);
        long newEquipId = newEquip.getId().getObjectId();
        this.equipInventory.add(newEquipId);
        Config.unloadObject(newEquip);
    }

    public void removeEquip(long equipId) {
        if(!this.equipInventory.contains(equipId)) {
            return;
        }

        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
        if(this.getEquipped(equipment.getEquipType()) == equipId) {
            return;
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

    public long getEquipIdByIndex(int index) {
        List<Long> list = new ArrayList<>(this.getEquipInventory());

        int size = list.size();
        if(index <= 0 || index > size) {
            throw new WeirdCommandException("알 수 없는 장비입니다");
        }

        Collections.sort(list);

        return list.get(index - 1);
    }

    public void addEvent(@NonNull EventList eventData) {
        this.addEvent(eventData, false);
    }

    public void addEvent(@NonNull EventList eventData, boolean ignore) {
        long eventId = eventData.getId();
        Event event = EntityEvents.getEvent(eventId);
        String eventName = event.getClassName();

        ConcurrentArrayList<Long> eventList = this.event.get(eventName);
        if(eventList == null) {
            eventList = new ConcurrentArrayList<>();
            eventList.add(eventId);
            this.event.put(eventName, eventList);
        } else {
            if(ignore || !eventList.contains(eventId)) {
                eventList.add(eventId);
            }
        }
    }

    public void removeEvent(@NonNull EventList eventData) {
        this.removeEvent(eventData, false);
    }

    public void removeEvent(@NonNull EventList eventData, boolean removeAll) {
        String eventName = EntityEvents.getEvent(eventData.getId()).getClassName();

        List<Long> events = this.event.get(eventName);
        long eventId = eventData.getId();

        if(removeAll) {
            while(events.remove(eventData.getId())) {}
        } else {
            events.remove(eventId);
        }
    }

    @NonNull
    public Set<String> getRemovedEquipEvent(@NonNull EquipType equipType) {
        return Objects.requireNonNull(this.removedEquipEvent.get(equipType));
    }

    @NonNull
    public Set<Long> getEquipEvents(@NonNull String eventName) {
        Set<Long> eventSet = new HashSet<>();

        for(long equipId : this.equipped.values()) {
            Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
            Map<String, Event> eventMap = equipment.getEvent();

            if(eventMap != null && eventMap.containsKey(eventName) && !Objects.requireNonNull(
                    this.removedEquipEvent.get(equipment.getEquipType())).contains(eventName)) {
                eventSet.add(equipment.getOriginalId());
            }
        }

        return eventSet;
    }

    public boolean canFight(@NonNull Entity enemy) {
        List<Doing> fightableList = Doing.fightableList();

        if(fightableList.contains(this.doing) && fightableList.contains(enemy.doing)) {
            if(enemy.doing.equals(Doing.FIGHT)) {
                long fightId;

                try {
                    fightId = FightManager.getInstance().getFightId(enemy.getId());
                } catch (NullPointerException e) {
                    return false;
                }

                Set<Entity> entitySet = FightManager.getInstance().getEntitySet(fightId);

                int count = 0;
                for(Entity entity : new HashSet<>(entitySet)) {
                    if(Doing.fightList().contains(entity.getDoing()) && entity.getKiller() == null &&
                            FightManager.getInstance().fightId.containsKey(entity.getId())) {
                        count++;
                    }
                }

                return count >= 2;
            } else {
                return true;
            }
        }

        return false;
    }

    public void attack(@NonNull Entity entity, boolean print) {
        this.damage(entity, new Int(this.getStat(StatType.ATK)), new Int(), new Int(), true, true, print);
    }

    public void damage(@NonNull Entity entity, double physicDmg, double magicDmg, double staticDmg,
                          boolean canEvade, boolean canCrit, boolean print) {
        damage(entity, (int) physicDmg, (int) magicDmg, (int) staticDmg, canEvade, canCrit, print);
    }

    public void damage(@NonNull Entity entity, int physicDmg, int magicDmg, int staticDmg,
                          boolean canEvade, boolean canCrit, boolean print) {
        damage(entity, new Int(physicDmg), new Int(magicDmg), new Int(staticDmg), canEvade, canCrit, print);
    }

    public void damage(@NonNull Entity target, @NonNull Int physicDmg, @NonNull Int magicDmg, @NonNull Int staticDmg,
                          boolean canEvade, boolean canCrit, boolean print) {
        Random random = new Random();

        this.revalidateBuff();

        if(target instanceof Monster && this.id.getId().equals(Id.PLAYER)) {
            ((Monster) target).getAngryPlayers().add(this.id.getObjectId());
        }

        Bool evade = new Bool();
        if(canEvade) {
            evade.set(random.nextInt(100) < Math.min(target.getStat(StatType.EVA) - this.getStat(StatType.ACC), Config.MAX_EVADE));
        }

        Bool wrappedCrit = new Bool(canCrit);

        String eventName = PreEvadeEvent.getName();
        PreEvadeEvent.handleEvent(target, target.event.get(eventName), target.getEquipEvents(eventName),
                this, physicDmg, magicDmg, staticDmg, evade, wrappedCrit);

        canCrit = wrappedCrit.get();

        if(!evade.get()) {
            lastAttackSuccess = true;

            eventName = PreDamageEvent.getName();
            PreDamageEvent.handleEvent(this, this.event.get(eventName), this.getEquipEvents(eventName),
                    target, physicDmg, magicDmg, staticDmg, canCrit);

            eventName = PreDamagedEvent.getName();
            PreDamagedEvent.handleEvent(target, target.event.get(eventName), target.getEquipEvents(eventName),
                    this, physicDmg, magicDmg, staticDmg, canCrit);

            int def = Math.max(target.getStat(StatType.DEF) - this.getStat(StatType.BRE), 0);
            int mdef = Math.max(target.getStat(StatType.MDEF) - this.getStat(StatType.MBRE), 0);

            physicDmg.set(Math.max(physicDmg.get() - def, 0));
            magicDmg.set(Math.max(magicDmg.get() - mdef, 0));

            int heal = 0;
            boolean isDefencing = target.getObjectVariable(Variable.IS_DEFENCING, false);
            if(isDefencing) {
                heal = (int) (physicDmg.get() * (new Random().nextInt(41) + 30) / 100D);
                physicDmg.set(0);

                target.addBasicStat(StatType.HP, heal);
                target.setVariable(Variable.IS_DEFENCING, false);
            }

            Int dra = new Int(physicDmg.get() * Math.min(this.getStat(StatType.DRA), 100) / 100);
            Int mdra = new Int(magicDmg.get() * Math.min(this.getStat(StatType.MDRA), 100) / 100);

            Int totalDmg = new Int(physicDmg.get() + magicDmg.get() + staticDmg.get());
            Int totalDra = new Int(dra.get() + mdra.get());

            Bool isCrit = new Bool();
            if(canCrit) {
                int agi = this.getStat(StatType.AGI);
                if (random.nextDouble() < agi * Config.CRIT_PER_AGI) {
                    isCrit.set(true);
                    totalDmg.multiple(3);
                }
            }

            eventName = DamageEvent.getName();
            DamageEvent.handleEvent(this, this.event.get(eventName), this.getEquipEvents(eventName),
                    target, totalDmg, totalDra, isCrit, canCrit);

            eventName = DamagedEvent.getName();
            DamagedEvent.handleEvent(target, target.event.get(eventName), target.getEquipEvents(eventName),
                    this, totalDmg, totalDra, isCrit);

            boolean isDeath = target.addBasicStat(StatType.HP, -1 * Math.max(totalDmg.get(), 1));
            this.addBasicStat(StatType.HP, totalDra.get());

            this.lastDamage = totalDmg.get();
            this.lastDrain = totalDra.get();
            this.isLastCrit = isCrit.get();
            target.lastHeal = heal;

            if(print) {
                if(this.id.getId().equals(Id.PLAYER)) {
                    ((Player) this).printDamageMsg(target);
                }

                //Change to instanceof because of warning
                if(target instanceof Player) {
                    ((Player) target).printDamagedMsg(this);
                }
            }

            if (isDeath) {
                target.setKiller(this.id);
                target.onDeath();
                this.onKill(target);
            }
        } else {
            lastAttackSuccess = false;

            if (this.getId().getId().equals(Id.PLAYER)) {
                ((Player) this).replyPlayer("공격이 빗나갔습니다");
            }

            //Used instanceof because of warning
            if (target instanceof Player) {
                ((Player) target).replyPlayer(this.getName() + " 의 공격을 피했습니다");
            }
        }
    }

    public int getFieldDistance(@NonNull Location location) {
        return (int) Math.sqrt(Math.pow(this.location.getFieldX() - location.getFieldX(), 2) +
                Math.pow(this.location.getFieldY() - location.getFieldX(), 2));
    }

    public int getMapDistance(@NonNull Location location) {
        return (int) Math.sqrt(Math.pow(this.location.getX() - location.getX(), 2) +
                Math.pow(this.location.getY() - location.getY(), 2));
    }

    public Map<Id, Map<Long, Integer>> getEntityDistant() {
        Map<Id, Map<Long, Integer>> output = new HashMap<>();
        Map<Long, Integer> distantMap;

        GameMap map = Config.getMapData(this.location);

        Id id;
        Entity entity;
        for(Map.Entry<Id, Set<Long>> entry : map.getEntity().entrySet()) {
            id = entry.getKey();

            distantMap = new HashMap<>();
            output.put(id, distantMap);

            for(long objectId : entry.getValue()) {
                entity = Config.getData(id, objectId);

                if(entity.equals(this)) {
                    continue;
                }

                distantMap.put(objectId, getFieldDistance(entity.getLocation()));
            }
        }

        return output;
    }

    @Nullable
    public Object[] getNearestEntity(@NonNull Id id) {
        Map<Id, Map<Long, Integer>> distantMap = this.getEntityDistant();

        IdClass idClass = null;
        int minDistant = Integer.MAX_VALUE;

        Id key;
        int distant;
        for(Map.Entry<Id, Map<Long, Integer>> entry : distantMap.entrySet()) {
            key = entry.getKey();

            if(key != null && !key.equals(id)) {
                continue;
            }

            for(Map.Entry<Long, Integer> entry_ : entry.getValue().entrySet()) {
                distant = entry_.getValue();

                if(distant < minDistant) {
                    minDistant = distant;
                    idClass = new IdClass(id, entry_.getKey());
                }
            }
        }

        if(idClass == null) {
            return null;
        } else {
            return new Object[]{idClass, minDistant};
        }
    }

    public void setVariable(@NonNull Variable variable, @Nullable Object value) {
        this.variable.put(variable, value);
    }

    public void removeVariable(@NonNull Variable variable) {
        this.variable.remove(variable);
    }

    public int getVariable(@NonNull Variable variable) {
        return this.getVariable(variable, 0);
    }

    public int getVariable(@NonNull Variable variable, int defaultValue) {
        Object value = this.variable.getOrDefault(variable, defaultValue);

        if(value instanceof Double) {
            return ((Double) value).intValue();
        } else {
            return (int) value;
        }
    }

    public long getVariable(@NonNull Variable variable, long defaultValue) {
        Object value = this.variable.getOrDefault(variable, defaultValue);

        if(value instanceof Double) {
            return ((Double) value).longValue();
        } else {
            return (long) value;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getObjectVariable(@NonNull Variable variable) {
        return (T) this.variable.get(variable);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T> T getObjectVariable(@NonNull Variable variable, @NonNull T defaultValue) {
        return (T) this.variable.getOrDefault(variable, defaultValue);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public List<Long> getListVariable(@NonNull Variable variable) {
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
    public Set<Long> getSetVariable(Variable variable) {
        Set<?> set = this.getObjectVariable(variable);
        Set<Long> outputSet = new HashSet<>();

        if(set == null) {
            outputSet = new HashSet<>();
            this.setVariable(variable, outputSet);
            return outputSet;
        }

        if(!set.isEmpty()) {
            if(set.toArray()[0] instanceof Double) {
                for (Object element : set) {
                    outputSet.add(((Double) element).longValue());
                }
            } else {
                return (Set<Long>) set;
            }
        }

        this.setVariable(variable, outputSet);
        return outputSet;
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
        return this.name + " (Lv." + this.getLv() + ")";
    }

    @NonNull
    public abstract String getFightName();

    @NonNull
    public String getRealName() {
        return this.name;
    }

}
