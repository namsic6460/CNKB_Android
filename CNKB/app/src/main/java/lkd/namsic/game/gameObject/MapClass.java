package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.Emoji;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.UnhandledEnumException;
import lkd.namsic.game.exception.WeirdDataException;
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
    boolean pvp = false;

    @Setter
    @NonNull
    MapType mapType = MapType.COUNTRY;

    final LimitInteger requireLv = new LimitInteger(Config.MIN_LV, Config.MIN_LV, Config.MAX_LV);

    final Location location = new Location();

    Map<Id, Map<Long, Double>> spawnMonster = new ConcurrentHashMap<>();

    //This part can be frequently changed
    final Map<Location, Long> money = new ConcurrentHashMap<>();
    final Map<Location, ConcurrentHashMap<Long, Integer>> item = new ConcurrentHashMap<>();
    final Map<Location, ConcurrentHashSet<Long>> equip = new ConcurrentHashMap<>();
    final Map<Id, ConcurrentHashSet<Long>> entity = new ConcurrentHashMap<>();

    public MapClass(@NonNull String name) {
        this.name = name;

        this.entity.put(Id.PLAYER, new ConcurrentHashSet<>());
        this.entity.put(Id.MONSTER, new ConcurrentHashSet<>());
        this.entity.put(Id.BOSS, new ConcurrentHashSet<>());
        this.entity.put(Id.NPC, new ConcurrentHashSet<>());
    }

    @NonNull
    public String getInfo() {
        return this.name + "(요구 레벨: " + requireLv.get() + ") [" + this.mapType.toString() + "]\n" +
                Emoji.WORLD + ": " + this.location.toMapString() + "\n" +
                Emoji.MONSTER + ": " + this.getEntity(Id.MONSTER).size() + ", " +
                Emoji.BOSS + ": " + this.getEntity(Id.BOSS).size();
    }

    @NonNull
    public String getInnerInfo() {
        StringBuilder builder = new StringBuilder("---플레이어 목록---");
        
        Set<Long> playerSet = this.entity.get(Id.PLAYER);
        Player player;
        if(playerSet.isEmpty()) {
            builder.append("\n플레이어 없음");
        } else {
            for (long playerId : playerSet) {
                player = Config.getData(Id.PLAYER, playerId);
                builder.append("\n[")
                        .append(player.getLocation().toFieldString())
                        .append("] ")
                        .append(player.getName());
            }
        }

        builder.append("\n\n---NPC 목록---");

        Set<Long> npcSet = this.entity.get(Id.NPC);
        npcSet.remove(1L);

        Npc npc;
        if(npcSet.isEmpty()) {
            builder.append("\nNPC 없음");
        } else {
            for(long npcId : npcSet) {
                npc = Config.getData(Id.NPC, npcId);
                builder.append("\n[")
                        .append(npc.getLocation().toFieldString())
                        .append("] ")
                        .append(npc.getName());
            }
        }

        builder.append("\n\n---무언가 감지된 좌표---");

        Set<Location> locationSet = new HashSet<>();
        locationSet.addAll(this.money.keySet());
        locationSet.addAll(this.item.keySet());
        locationSet.addAll(this.equip.keySet());

        Entity entity;
        for(long monsterId : this.getEntity(Id.MONSTER)) {
            entity = Config.getData(Id.MONSTER, monsterId);
            locationSet.add(entity.getLocation());
        }

        for(long bossId : this.getEntity(Id.BOSS)) {
            entity = Config.getData(Id.BOSS, bossId);
            locationSet.add(entity.getLocation());
        }

        if(locationSet.isEmpty()) {
            return builder.toString() + "\n감지된 물체 없음";
        }

        for(Location location : locationSet) {
            builder.append("\n")
                    .append(location.toFieldString());
        }

        return builder.toString();
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
        return this.money.getOrDefault(location, 0L);
    }

    public void addMoney(Location location, long money) {
        this.setMoney(location, this.getMoney(location) + money);
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
            return itemMap.getOrDefault(itemId, 0);
        }

        return 0;
    }

    public void addItem(Location location, long itemId, int count) {
        this.setItem(location, itemId, this.getItem(location, itemId) + count);
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

    public void addEntity(Entity entity) {
        if(!entity.getLocation().equalsMap(this.location)) {
            throw new WeirdDataException(this.location, location);
        }

        this.getEntity(entity.id.getId()).add(entity.id.getObjectId());
    }

    public void removeEntity(Entity entity) {
        this.getEntity(entity.id.getId()).remove(entity.id.getObjectId());
    }

    public void setSpawnMonster(Id id, long monsterId, double percent) {
        if(!(id.equals(Id.MONSTER) || id.equals(Id.BOSS))) {
            throw new UnhandledEnumException(id);
        }

        if(percent < 0 || percent > 1) {
            throw new NumberRangeException(percent, 0, 1);
        }

        Config.checkId(id, monsterId);

        Map<Long, Double> spawnMonster = this.spawnMonster.get(id);
        if(spawnMonster == null) {
            spawnMonster = new HashMap<>();
            spawnMonster.put(monsterId, percent);
            this.spawnMonster.put(id, spawnMonster);
        } else {
            spawnMonster.put(monsterId, percent);
        }
    }

    public double getSpawnMonster(Id id, long monsterId) {
        Map<Long, Double> spawnMonster = this.spawnMonster.get(id);
        if(spawnMonster == null) {
            return 0;
        } else {
            return spawnMonster.getOrDefault(monsterId, 0D);
        }
    }

    public void addSpawnMonster(Id id, long monsterId, double percent) {
        this.setSpawnMonster(id, monsterId, this.getSpawnMonster(id, monsterId) + percent);
    }

    public void spawnMonster() {
        if(!(this.getEntity(Id.MONSTER).isEmpty() && this.getEntity(Id.BOSS).isEmpty())) {
            return;
        }

        Random random = new Random();

        Id id;
        long monsterId;
        double percent;

        for(Map.Entry<Id, Map<Long, Double>> entry : this.spawnMonster.entrySet()) {
            id = entry.getKey();

            for(Map.Entry<Long, Double> monsterEntry : entry.getValue().entrySet()) {
                monsterId = monsterEntry.getKey();
                percent = monsterEntry.getValue();

                if(random.nextDouble() < percent || percent == 1) {
                    int fieldX = random.nextInt(Config.MAX_FIELD_X + 1);
                    int fieldY = random.nextInt(Config.MAX_FIELD_Y + 1);

                    AiEntity newEntity = Config.newObject(Config.getData(id, monsterId));
                    newEntity.setMap(this.location.getX().get(), this.location.getY().get(), fieldX, fieldY);
                    this.addEntity(newEntity);
                    Config.unloadObject(newEntity);

                    if(id.equals(Id.BOSS)) {
                        break;
                    }
                }
            }
        }
    }

}
