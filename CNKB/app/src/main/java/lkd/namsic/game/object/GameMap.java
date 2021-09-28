package lkd.namsic.game.object;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.IdClass;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.object.BossList;
import lkd.namsic.game.enums.object.MapList;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.WeirdDataException;
import lkd.namsic.setting.Logger;
import lombok.Getter;
import lombok.Setter;

@Getter
public class GameMap {

    public static final Map<Long, Location> MONSTER_SPAWN_MAP = new HashMap<>();

    @Setter
    @NonNull
    String name;

    @Setter
    boolean pvp = false;

    @Setter
    @NonNull
    MapType mapType = MapType.COUNTRY;

    @Setter
    int requireLv = 1;

    final Map<Long, Integer> limitQuest = new HashMap<>();

    final Location location;

    final Set<Long> spawnMonster = new HashSet<>();
    final Map<Long, Integer> spawnMaxCount = new HashMap<>();
    final Map<Long, Double> spawnPercent = new HashMap<>();

    //This part can be frequently changed
    final Map<Location, Long> money = new ConcurrentHashMap<>();
    final Map<Location, Map<Long, Integer>> item = new ConcurrentHashMap<>();
    final Map<Location, Set<Long>> equip = new ConcurrentHashMap<>();
    final Map<Id, Set<Long>> entity = new ConcurrentHashMap<>();

    public GameMap(@NonNull MapList mapData) {
        this.name = mapData.getDisplayName();
        this.location = new Location(mapData.getLocation().getX(), mapData.getLocation().getY(), true);

        this.entity.put(Id.PLAYER, new ConcurrentHashSet<>());
        this.entity.put(Id.MONSTER, new ConcurrentHashSet<>());
        this.entity.put(Id.BOSS, new ConcurrentHashSet<>());
        this.entity.put(Id.NPC, new ConcurrentHashSet<>());
    }

    public void setMoney(@NonNull Entity self, @NonNull Location location, long money) {
        if(!location.equalsMap(this.location)) {
            throw new WeirdDataException(this.location, location);
        }

        if(money < 0) {
            throw new NumberRangeException(money, 0);
        }

        Entity entity;
        Set<Entity> entitySet = new HashSet<>();
        for(Map.Entry<Id, Set<Long>> entry : this.entity.entrySet()) {
            if(entry.getKey().equals(Id.NPC)) {
                continue;
            }

            for(long objectId : entry.getValue()) {
                entity = Config.getData(entry.getKey(), objectId);
                if(entity.equals(self)) {
                    continue;
                }

                if(entity.getLocation().equalsField(location)) {
                    entitySet.add(entity);
                }
            }
        }

        if(entitySet.isEmpty()) {
            this.money.put(location, money);
        } else {
            entity = (Entity) entitySet.toArray()[new Random().nextInt(entitySet.size())];
            Id id = entity.getId().getId();

            Config.loadObject(id, entity.getId().getObjectId());
            entity.addMoney(money);
            Config.unloadObject(entity);

            if(id.equals(Id.PLAYER)) {
                ((Player) entity).replyPlayer(money + "G 를 주웠습니다");
            }
        }
    }

    public long getMoney(@NonNull Location location) {
        return this.money.getOrDefault(location, 0L);
    }

    public void addMoney(@NonNull Entity self, long money) {
        Location location = new Location(self.getLocation());
        this.setMoney(self, location, this.getMoney(location) + money);
    }

    public void setItem(@NonNull Entity self, @NonNull Location location, long itemId, int count) {
        if(!location.equalsMap(this.location)) {
            throw new WeirdDataException(this.location, location);
        }

        if(count < 0) {
            throw new NumberRangeException(count, 0);
        }

        Map<Long, Integer> itemMap = this.item.get(location);
        if(count == 0) {
            if(itemMap != null) {
                itemMap.remove(itemId);

                if(itemMap.size() == 0) {
                    this.item.remove(location);
                }
            }
        } else {
            Entity entity;
            Set<Entity> entitySet = new HashSet<>();
            for(Map.Entry<Id, Set<Long>> entry : this.entity.entrySet()) {
                if(!entry.getKey().equals(Id.PLAYER)) {
                    continue;
                }

                for(long objectId : entry.getValue()) {
                    entity = Config.getData(entry.getKey(), objectId);
                    if(entity.equals(self)) {
                        continue;
                    }

                    if(entity.getLocation().equalsField(location)) {
                        entitySet.add(entity);
                    }
                }
            }

            if(entitySet.isEmpty()) {
                if (itemMap != null) {
                    itemMap.put(itemId, count);
                } else {
                    itemMap = new ConcurrentHashMap<>();
                    itemMap.put(itemId, count);
                    this.item.put(location, itemMap);
                }
            } else {
                entity = (Entity) entitySet.toArray()[new Random().nextInt(entitySet.size())];
                Id id = entity.getId().getId();

                Config.loadObject(id, entity.getId().getObjectId());
                entity.addItem(itemId, count);
                Config.unloadObject(entity);
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

    public void addItem(@NonNull Entity self, long itemId, int count) {
        Location location = new Location(self.getLocation());
        this.setItem(self, location, itemId, this.getItem(location, itemId) + count);
    }

    public void addEquip(@NonNull Entity self, long equipId) {
        Location location = new Location(self.getLocation());

        if(!location.equalsMap(this.location)) {
            throw new WeirdDataException(this.location, location);
        }

        Entity entity;
        Set<Entity> entitySet = new HashSet<>();
        for(Map.Entry<Id, Set<Long>> entry : this.entity.entrySet()) {
            if(entry.getKey().equals(Id.NPC)) {
                continue;
            }

            for(long objectId : entry.getValue()) {
                entity = Config.getData(entry.getKey(), objectId);
                if(entity.equals(self)) {
                    continue;
                }

                if(entity.getLocation().equalsField(location)) {
                    entitySet.add(entity);
                }
            }
        }

        if(entitySet.isEmpty()) {
            Set<Long> equipSet = this.equip.get(location);
            if (equipSet == null) {
                equipSet = new ConcurrentHashSet<>();
                equipSet.add(equipId);
                this.equip.put(location, equipSet);
            } else {
                equipSet.add(equipId);
            }
        } else {
            entity = (Entity) entitySet.toArray()[new Random().nextInt(entitySet.size())];
            Id id = entity.getId().getId();

            Config.loadObject(id, entity.getId().getObjectId());
            entity.addEquip(equipId);
            Config.unloadObject(entity);

            if(id.equals(Id.PLAYER)) {
                ((Player) entity).replyPlayer(((Equipment) Config.getData(Id.EQUIPMENT, equipId)).getName() + " (을/를) 주웠습니다");
            }
        }
    }

    @NonNull
    public Set<Long> getEntity(@NonNull Id id) {
        return Objects.requireNonNull(this.entity.get(id));
    }

    public void addEntity(@NonNull Entity entity) {
        if(!entity.getLocation().equalsMap(this.location)) {
            throw new WeirdDataException(this.location, entity.getLocation());
        }

        this.getEntity(entity.id.getId()).add(entity.id.getObjectId());
    }

    public void removeEntity(@NonNull Entity entity) {
        this.removeEntity(entity.getId());
    }

    public void removeEntity(@NonNull IdClass idClass) {
        Id id = idClass.getId();

        this.getEntity(id).remove(idClass.getObjectId());
        if(id.equals(Id.MONSTER) || id.equals(Id.BOSS)) {
            this.respawn();
        }
    }

    public void setSpawnMonster(long monsterId, double percent, int maxCount) {
        if(percent < 0 || percent > 1) {
            throw new NumberRangeException(percent, 0, 1);
        }

        if(maxCount < 1) {
            throw new NumberRangeException(maxCount, 1, null);
        }

        this.spawnMonster.add(monsterId);
        this.spawnPercent.put(monsterId, percent);
        this.spawnMaxCount.put(monsterId, maxCount);

        MONSTER_SPAWN_MAP.put(monsterId, this.location);
    }

    public void respawn() {
        Random random = new Random();

        double percent;
        int spawnCount;

        Set<Long> existSet = new HashSet<>();
        for(long objectId : this.getEntity(Id.MONSTER)) {
            Monster monster = Config.getData(Id.MONSTER, objectId);
            existSet.add(monster.getOriginalId());
        }

        boolean created = false;

        for(long monsterId : spawnMonster) {
            if(existSet.contains(monsterId)) {
                continue;
            }

            percent = this.spawnPercent.get(monsterId);
            spawnCount = random.nextInt(this.spawnMaxCount.get(monsterId)) + 1;

            if(random.nextDouble() < percent || percent == 1) {
                for(int count = 0; count < spawnCount; count++) {
                    created = true;

                    int fieldX = random.nextInt(Config.MAX_FIELD_X - 4) + 5;
                    int fieldY = random.nextInt(Config.MAX_FIELD_Y - 4) + 5;

                    Monster monster = Config.newObject(Config.getData(Id.MONSTER, monsterId), false);
                    monster.randomLevel();
                    monster.location = new Location(this.location.getX(), this.location.getY(), fieldX, fieldY);
                    this.addEntity(monster);
                    Config.unloadObject(monster);
                }
            }
        }

        if(created) {
            Config.saveConfig();
        }
    }

    @NonNull
    public Boss spawnBoss(@NonNull BossList bossData) {
        Boss boss = Config.newObject(Config.getData(Id.BOSS, bossData.getId()), true);
        boss.location = new Location(this.location.getX(), this.location.getY(), 32, 32);
        this.addEntity(boss);
        Config.unloadObject(boss);

        Set<Player> playerSet = new HashSet<>();
        for(long playerId : new HashSet<>(this.getEntity(Id.PLAYER))) {
            playerSet.add(Config.getData(Id.PLAYER, playerId));
        }

        String prefix = boss.getName() + ": ";
        for(String msg : boss.getSpawnMsg()) {
            Player.replyPlayers(playerSet, prefix + msg);

            try {
                Thread.sleep(Config.BOSS_SPAWN_MSG_WAIT_TIME);
            } catch (InterruptedException e) {
                Logger.e("GameMap.summonBoss", Config.errorString(e));
            }
        }

        return boss;
    }

    @NonNull
    public String getLocationName() {
        return getLocationName(false);
    }

    @NonNull
    public String getLocationName(boolean includeField) {
        if(includeField) {
            return this.name + "(" + this.location.toString() + ")";
        } else {
            return this.name + "(" + this.location.toMapString() + ")";
        }
    }

}
