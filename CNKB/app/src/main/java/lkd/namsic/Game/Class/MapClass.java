package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Base.ConcurrentHashSet;
import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.MapType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lkd.namsic.Game.Exception.ObjectNotFoundException;
import lkd.namsic.Game.Exception.WeirdDataException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class MapClass {

    @Setter
    @NonNull
    String name;

    @Setter
    boolean pvp;

    @Setter
    @NonNull
    MapType mapType;

    Location location;

    ConcurrentHashMap<Location, Long> money;
    ConcurrentHashMap<Location, ConcurrentHashMap<Long, Integer>> item;
    ConcurrentHashMap<Location, ConcurrentHashSet<Long>> equip;

    ConcurrentHashMap<Id, ConcurrentHashSet<Long>> entity;

    public MapClass(@NonNull String name, boolean pvp, @NonNull MapType mapType, @NonNull Location location,
                    @NonNull ConcurrentHashMap<Location, Long> money,
                    @NonNull ConcurrentHashMap<Location, ConcurrentHashMap<Long, Integer>> item,
                    @NonNull ConcurrentHashMap<Location, ConcurrentHashSet<Long>> equip,
                    @NonNull ConcurrentHashSet<Long> boss, @NonNull ConcurrentHashSet<Long> monster,
                    @NonNull ConcurrentHashSet<Long> npc, @NonNull ConcurrentHashSet<Long> player) {
        this.name = name;
        this.pvp = pvp;
        this.mapType = mapType;
        this.location = location;

        this.setMoney(money);
        this.setItem(item);
        this.addEquip(equip);

        this.entity = new ConcurrentHashMap<>();
        this.entity.put(Id.BOSS, new ConcurrentHashSet<>());
        this.entity.put(Id.MONSTER, new ConcurrentHashSet<>());
        this.entity.put(Id.NPC, new ConcurrentHashSet<>());
        this.entity.put(Id.PLAYER, new ConcurrentHashSet<>());

        this.addEntity(Id.BOSS, boss);
        this.addEntity(Id.MONSTER, monster);
        this.addEntity(Id.NPC, npc);
        this.addEntity(Id.PLAYER, player);
    }

    @NonNull
    public Map<Id, HashMap<Long, Integer>> getFieldData(Location location) {
        Map<Id, HashMap<Long, Integer>> map = new HashMap<>();

        HashMap<Long, Integer> itemMap = new HashMap<>();
        HashMap<Long, Integer> equipMap = new HashMap<>();
        HashMap<Long, Integer> bossMap = new HashMap<>();
        HashMap<Long, Integer> monsterMap = new HashMap<>();
        HashMap<Long, Integer> npcMap = new HashMap<>();
        HashMap<Long, Integer> playerMap = new HashMap<>();

        for(Map.Entry<Location, ConcurrentHashMap<Long, Integer>> entry : item.entrySet()) {
            if(location.equalsField(entry.getKey())) {
                itemMap.putAll(entry.getValue());
            }
        }
        for(Map.Entry<Location, ConcurrentHashSet<Long>> entry : equip.entrySet()) {
            if(location.equalsField(entry.getKey())) {
                for(Long equipId : entry.getValue()) {
                    equipMap.put(equipId, 1);
                }
            }
        }
        for(Long bossId : this.getEntity(Id.BOSS)) {
            if(location.equalsField(((Boss)Config.getData(Id.BOSS, bossId)).getLocation())) {
                bossMap.put(bossId, 1);
            }
        }
        for(Long monsterId : this.getEntity(Id.MONSTER)) {
            if(location.equalsField(((Monster)Config.getData(Id.MONSTER, monsterId)).getLocation())) {
                monsterMap.put(monsterId, 1);
            }
        }
        for(Long npcId : this.getEntity(Id.NPC)) {
            if(location.equalsField(((Npc)Config.getData(Id.NPC, npcId)).getLocation())) {
                npcMap.put(npcId, 1);
            }
        }
        for(Long playerId : this.getEntity(Id.PLAYER)) {
            if(location.equalsField(((Player)Config.getData(Id.PLAYER, playerId)).getLocation())) {
                playerMap.put(playerId, 1);
            }
        }

        map.put(Id.ITEM, itemMap);
        map.put(Id.EQUIPMENT, equipMap);
        map.put(Id.BOSS, bossMap);
        map.put(Id.MONSTER, monsterMap);
        map.put(Id.NPC, npcMap);
        map.put(Id.PLAYER, playerMap);
        return map;
    }

    @NonNull
    public Map<Id, HashMap<Long, Integer>> getMapData(Location location) {
        Map<Id, HashMap<Long, Integer>> map = new HashMap<>();

        HashMap<Long, Integer> itemMap = new HashMap<>();
        HashMap<Long, Integer> equipMap = new HashMap<>();
        HashMap<Long, Integer> bossMap = new HashMap<>();
        HashMap<Long, Integer> monsterMap = new HashMap<>();
        HashMap<Long, Integer> npcMap = new HashMap<>();
        HashMap<Long, Integer> playerMap = new HashMap<>();

        for(ConcurrentHashMap<Long, Integer> item : this.item.values()) {
            for(Map.Entry<Long, Integer> entry : item.entrySet()) {
                long objectId = entry.getKey();

                Integer value = itemMap.get(objectId);
                value = value == null ? 0 : value;

                itemMap.put(objectId, value + entry.getValue());
            }
        }
        for(ConcurrentHashSet<Long> equip : this.equip.values()) {
            for(long equipId : equip) {
                equipMap.put(equipId, 1);
            }
        }
        for(Long bossId : this.getEntity(Id.BOSS)) {
            bossMap.put(bossId, 1);
        }
        for(Long monsterId : this.getEntity(Id.MONSTER)) {
            monsterMap.put(monsterId, 1);
        }
        for(Long npcId : this.getEntity(Id.NPC)) {
            npcMap.put(npcId, 1);
        }
        for(Long playerId : this.getEntity(Id.PLAYER)) {
            playerMap.put(playerId, 1);
        }

        map.put(Id.ITEM, itemMap);
        map.put(Id.EQUIPMENT, equipMap);
        map.put(Id.BOSS, bossMap);
        map.put(Id.MONSTER, monsterMap);
        map.put(Id.NPC, npcMap);
        map.put(Id.PLAYER, playerMap);
        return map;
    }

    public void setMoney(Map<Location, Long> money) {
        for(Map.Entry<Location, Long> entry : money.entrySet()) {
            this.setMoney(entry.getKey(), entry.getValue());
        }
    }

    public void setMoney(Location location, long money) {
        if(!location.equalsMap(this.location)) {
            throw new WeirdDataException(this.location, location);
        }

        if(money < 0) {
            throw new NumberRangeException(money, 0);
        }

        this.money.put(location, this.getMoney(location) + money);
    }

    public long getMoney(Location location) {
        Long value = this.money.get(location);

        if(value != null) {
            return value;
        } else {
            return 0;
        }
    }

    public void addMoney(Location location, long money) {
        this.setMoney(location, this.getMoney(location) + money);
    }

    public void setItem(Map<Location, ConcurrentHashMap<Long, Integer>> item) {
        for(Map.Entry<Location, ConcurrentHashMap<Long, Integer>> entry : item.entrySet()) {
            this.setItem(entry.getKey(), entry.getValue());
        }
    }

    public void setItem(Location location, ConcurrentHashMap<Long, Integer> item) {
        for(Map.Entry<Long, Integer> entry : item.entrySet()) {
            this.setItem(location, entry.getKey(), entry.getValue());
        }
    }

    public void setItem(Location location, long itemId, int count) {
        if(!location.equalsMap(this.location)) {
            throw new WeirdDataException(this.location, location);
        }

        Config.checkId(Id.ITEM, itemId);

        if(count < 0) {
            throw new NumberRangeException(count, 0);
        }

        ConcurrentHashMap<Long, Integer> itemMap = this.item.get(location);
        if(count == 0) {
            if(itemMap != null) {
                itemMap.remove(itemId);
            }
        } else {
            if(itemMap != null) {
                itemMap.put(itemId, count);
            } else {
                itemMap = new ConcurrentHashMap<>();
                itemMap.put(itemId, count);
                this.item.put(location, itemMap);
            }
        }
    }

    public int getItem(Location location, long itemId) {
        Map<Long, Integer> itemMap = this.item.get(location);

        if(itemMap != null) {
            Integer value = itemMap.get(itemId);

            if(value != null) {
                return value;
            }
        }

        return 0;
    }

    public void addItem(Location location, long itemId, int count) {
        this.setItem(location, itemId, this.getItem(location, itemId) + count);
    }

    public void addEquip(Map<Location, ConcurrentHashSet<Long>> equip) {
        for(Map.Entry<Location, ConcurrentHashSet<Long>> entry : equip.entrySet()) {
            this.addEquip(entry.getKey(), entry.getValue());
        }
    }

    public void addEquip(Location location, Set<Long> equip) {
        for(long equipId : equip) {
            this.addEquip(location, equipId);
        }
    }

    public void addEquip(Location location, long equipId) {
        if(!location.equalsMap(this.location)) {
            throw new WeirdDataException(this.location, location);
        }

        Config.checkId(Id.EQUIPMENT, equipId);

        ConcurrentHashSet<Long> equipSet = this.equip.get(location);
        if(equipSet == null) {
            equipSet = new ConcurrentHashSet<>();
            equipSet.add(equipId);
            this.equip.put(location, equipSet);
        } else {
            equipSet.add(equipId);
        }
    }

    @NonNull
    public Set<Long> getEntity(Id id) {
        return Objects.requireNonNull(this.entity.get(id));
    }

    public void addEntity(Id id, Set<Long> entity) {
        for(long objectId : entity) {
            this.addEntity(id, objectId);
        }
    }

    public void addEntity(Id id, long objectId) {
        Id.checkEntityId(id);
        Config.checkId(id, objectId);

        this.getEntity(id).add(objectId);
    }

    public void removeEntity(Id id, long objectId) {
        Id.checkEntityId(id);
        Config.checkId(id, objectId);

        Set<Long> entitySet = this.getEntity(id);
        if(entitySet.contains(objectId)) {
            entitySet.remove(objectId);
        } else {
            throw new ObjectNotFoundException(id, objectId);
        }
    }

}
