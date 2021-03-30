package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Base.ConcurrentHashSet;
import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.MapType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lkd.namsic.Game.Exception.WeirdDataException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class MapClass implements Serializable {

    @Setter
    @NonNull
    String name;

    @Setter
    boolean pvp;

    @Setter
    @NonNull
    MapType mapType;

    Location location;

    ConcurrentHashMap<Location, ConcurrentHashMap<Long, Integer>> item;
    ConcurrentHashMap<Location, ConcurrentHashSet<Long>> equip;

    ConcurrentHashSet<Long> boss;
    ConcurrentHashSet<Long> monster;
    ConcurrentHashSet<Long> npc;
    ConcurrentHashSet<Long> player;

    public MapClass(@NonNull String name, boolean pvp, @NonNull MapType mapType, @NonNull Location location,
                    @NonNull ConcurrentHashMap<Location, ConcurrentHashMap<Long, Integer>> item,
                    @NonNull ConcurrentHashMap<Location, ConcurrentHashSet<Long>> equip,
                    @NonNull ConcurrentHashSet<Long> boss, @NonNull ConcurrentHashSet<Long> monster,
                    @NonNull ConcurrentHashSet<Long> npc, @NonNull ConcurrentHashSet<Long> player) {
        this.name = name;
        this.pvp = pvp;
        this.mapType = mapType;
        this.location = location;

        this.setItem(item);
        this.addEquip(equip);
        this.addBoss(boss);
        this.addMonster(monster);
        this.addNpc(npc);
        this.addPlayer(player);
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
        for(Long bossId : boss) {
            if(location.equalsField(((Boss)Config.getData(Id.BOSS, bossId)).getLocation())) {
                bossMap.put(bossId, 1);
            }
        }
        for(Long monsterId : monster) {
            if(location.equalsField(((Monster)Config.getData(Id.MONSTER, monsterId)).getLocation())) {
                monsterMap.put(monsterId, 1);
            }
        }
        for(Long npcId : npc) {
            if(location.equalsField(((Npc)Config.getData(Id.NPC, npcId)).getLocation())) {
                npcMap.put(npcId, 1);
            }
        }
        for(Long playerId : player) {
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
        for(Long bossId : boss) {
            bossMap.put(bossId, 1);
        }
        for(Long monsterId : monster) {
            monsterMap.put(monsterId, 1);
        }
        for(Long npcId : npc) {
            npcMap.put(npcId, 1);
        }
        for(Long playerId : player) {
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

    public void addBoss(Set<Long> boss) {
        for(long bossId : boss) {
            this.addBoss(bossId);
        }
    }

    public void addBoss(long bossId) {
        Boss boss = Config.getData(Id.BOSS, bossId);
        if(!boss.getLocation().equalsMap(this.location)) {
            throw new WeirdDataException(this.location, boss.getLocation());
        }

        this.boss.add(bossId);
    }

    public void addMonster(Set<Long> monster) {
        for(long monsterId : monster) {
            this.addMonster(monsterId);
        }
    }

    public void addMonster(long monsterId) {
        Monster monster = Config.getData(Id.MONSTER, monsterId);
        if(!monster.getLocation().equalsMap(this.location)) {
            throw new WeirdDataException(this.location, monster.getLocation());
        }

        this.monster.add(monsterId);
    }

    public void addNpc(Set<Long> npc) {
        for(long npcId : npc) {
            this.addNpc(npcId);
        }
    }

    public void addNpc(long npcId) {
        Npc npc = Config.getData(Id.NPC, npcId);
        if(!npc.getLocation().equalsMap(this.location)) {
            throw new WeirdDataException(this.location, npc.getLocation());
        }

        this.npc.add(npcId);
    }

    public void addPlayer(Set<Long> player) {
        for(long playerId : player) {
            this.addPlayer(playerId);
        }
    }

    public void addPlayer(long playerId) {
        Player player = Config.getData(Id.PLAYER, playerId);
        if(!player.getLocation().equalsMap(this.location)) {
            throw new WeirdDataException(this.location, player.getLocation());
        }

        this.player.add(playerId);
    }

}
