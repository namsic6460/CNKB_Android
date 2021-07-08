package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.object_list.ItemList;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.WeirdDataException;
import lkd.namsic.game.manager.MoveManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class GameMap {

    private static final MoveManager moveManager = MoveManager.getInstance();

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

    final Set<Long> spawnMonster = new ConcurrentHashSet<>();
    final Map<Long, Integer> spawnMaxCount = new ConcurrentHashMap<>();
    final Set<Long> spawnBoss = new ConcurrentHashSet<>();
    final Map<Id, Map<Long, Double>> spawnPercent = new ConcurrentHashMap<>();

    //This part can be frequently changed
    final Map<Location, Long> money = new ConcurrentHashMap<>();
    final Map<Location, Map<Long, Integer>> item = new ConcurrentHashMap<>();
    final Map<Location, Set<Long>> equip = new ConcurrentHashMap<>();
    final Map<Id, Set<Long>> entity = new ConcurrentHashMap<>();

    public GameMap(@NonNull String name) {
        this.name = name;

        this.entity.put(Id.PLAYER, new ConcurrentHashSet<>());
        this.entity.put(Id.MONSTER, new ConcurrentHashSet<>());
        this.entity.put(Id.BOSS, new ConcurrentHashSet<>());
        this.entity.put(Id.NPC, new ConcurrentHashSet<>());

        this.spawnPercent.put(Id.MONSTER, new ConcurrentHashMap<>());
        this.spawnPercent.put(Id.BOSS, new ConcurrentHashMap<>());
    }

    @NonNull
    public String getInfo() {
        return this.name + "(요구 레벨: " + requireLv.get() + ") [" + this.mapType.getMapName() + "]\n" +
                Emoji.WORLD + ": " + this.location.toMapString() + "\n" +
                Emoji.MONSTER + ": " + this.getEntity(Id.MONSTER).size() + ", " +
                Emoji.BOSS + ": " + this.getEntity(Id.BOSS).size();
    }

    @NonNull
    public String getInnerInfo() {
        StringBuilder builder = new StringBuilder("---플레이어 목록---");

        Set<Long> playerSet = this.entity.get(Id.PLAYER);
        if(playerSet.isEmpty()) {
            builder.append("\n플레이어 없음");
        } else {
            Player player;

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
        npcSet.remove(2L);

        if(npcSet.isEmpty()) {
            builder.append("\nNPC 없음");
        } else {
            for(long npcId : npcSet) {
                Npc npc;

                npc = Config.getData(Id.NPC, npcId);
                builder.append("\n[")
                        .append(npc.getLocation().toFieldString())
                        .append("] ")
                        .append(npc.getName());
            }
        }

        builder.append("\n\n---떨어진 골드---");

        if(this.money.isEmpty()) {
            builder.append("\n떨어진 골드 없음");
        } else {
            for (Map.Entry<Location, Long> entry : this.money.entrySet()) {
                builder.append("\n[")
                        .append(entry.getKey().toFieldString())
                        .append("] ")
                        .append(entry.getValue())
                        .append("G");
            }
        }

        builder.append("\n\n---떨어진 아이템---");

        if(this.item.isEmpty()) {
            builder.append("\n떨어진 아이템 없음");
        } else {
            Item item;
            String locationStr;
            for (Map.Entry<Location, Map<Long, Integer>> entry : this.item.entrySet()) {
                locationStr = "\n[" + entry.getKey().toFieldString() + "] ";

                for(Map.Entry<Long, Integer> itemEntry : entry.getValue().entrySet()) {
                    item = Config.getData(Id.ITEM, itemEntry.getKey());
                    builder.append(locationStr)
                            .append(item.getName())
                            .append(" ")
                            .append(itemEntry.getValue())
                            .append("개");
                }
            }
        }

        builder.append("\n\n---떨어진 장비---");

        if(this.equip.isEmpty()) {
            builder.append("\n떨어진 장비 없음");
        } else {
            Equipment equipment;
            String locationStr;
            for (Map.Entry<Location, Set<Long>> entry : this.equip.entrySet()) {
                locationStr = "\n[" + entry.getKey().toFieldString() + "] ";

                for(long equipId : entry.getValue()) {
                    equipment = Config.getData(Id.EQUIPMENT, equipId);
                    builder.append(locationStr)
                            .append(equipment.getName());
                }
            }
        }

        builder.append("\n\n---몬스터 목록---");

        Set<Long> monsterSet = this.entity.get(Id.MONSTER);
        if(monsterSet.isEmpty()) {
            builder.append("\n몬스터 없음");
        } else {
            Monster monster;

            for(long monsterId : monsterSet) {
                monster = Config.getData(Id.MONSTER, monsterId);
                builder.append("\n[")
                        .append(monster.getLocation().toFieldString())
                        .append("] ")
                        .append(monster.getName());
            }
        }

        builder.append("\n\n---보스 목록---");

        Set<Long> bossSet = this.entity.get(Id.BOSS);
        if(bossSet.isEmpty()) {
            builder.append("\n보스 없음");
        } else {
            Boss boss;

            for(long bossId : bossSet) {
                boss = Config.getData(Id.BOSS, bossId);
                builder.append("\n[")
                        .append(boss.getLocation().toFieldString())
                        .append("] ")
                        .append(boss.getName());
            }
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

        Entity entity;
        Set<Entity> entitySet = new HashSet<>();
        for(Map.Entry<Id, Set<Long>> entry : this.entity.entrySet()) {
            for(long objectId : entry.getValue()) {
                entity = Config.getData(entry.getKey(), objectId);

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

        Map<Long, Integer> itemMap = this.item.get(location);
        if(count == 0) {
            if(itemMap != null) {
                itemMap.remove(itemId);
            }
        } else {
            Entity entity;
            Set<Entity> entitySet = new HashSet<>();
            for(Map.Entry<Id, Set<Long>> entry : this.entity.entrySet()) {
                for(long objectId : entry.getValue()) {
                    entity = Config.getData(entry.getKey(), objectId);

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

                if(id.equals(Id.PLAYER)) {
                    ((Player) entity).replyPlayer(ItemList.findById(itemId) + " " + count + "개를 주웠습니다");
                }
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

        Entity entity;
        Set<Entity> entitySet = new HashSet<>();
        for(Map.Entry<Id, Set<Long>> entry : this.entity.entrySet()) {
            for(long objectId : entry.getValue()) {
                entity = Config.getData(entry.getKey(), objectId);

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
    public Set<Long> getEntity(Id id) {
        return Objects.requireNonNull(this.entity.get(id));
    }

    public void addEntity(Entity entity) {
        if(!entity.getLocation().equalsMap(this.location)) {
            throw new WeirdDataException(this.location, entity.getLocation());
        }

        this.getEntity(entity.id.getId()).add(entity.id.getObjectId());
    }

    public void removeEntity(Entity entity) {
        Id id = entity.getId().getId();

        this.getEntity(id).remove(entity.id.getObjectId());
        if(id.equals(Id.MONSTER) || id.equals(Id.BOSS)) {
            if(this.canRespawn()) {
                this.respawn();
            }
        }
    }

    public void setSpawnMonster(long monsterId, double percent, int maxCount) {
        if(percent < 0 || percent > 1) {
            throw new NumberRangeException(percent, 0, 1);
        }

        if(maxCount < 1 || maxCount > Config.MAX_SPAWN_COUNT) {
            throw new NumberRangeException(maxCount, 1, Config.MAX_SPAWN_COUNT);
        }

        Config.checkId(Id.MONSTER, monsterId);

        this.spawnMonster.add(monsterId);
        this.spawnPercent.get(Id.MONSTER).put(monsterId, percent);
        this.spawnMaxCount.put(monsterId, maxCount);
    }

    public void setSpawnBoss(long bossId, double percent) {
        if(percent < 0 || percent > 1) {
            throw new NumberRangeException(percent, 0, 1);
        }

        Config.checkId(Id.BOSS, bossId);

        this.spawnBoss.add(bossId);
        this.spawnPercent.get(Id.BOSS).put(bossId, percent);
    }

    public boolean canRespawn() {
        return this.getEntity(Id.MONSTER).isEmpty() && this.getEntity(Id.BOSS).isEmpty();
    }

    public void respawn() {
        Random random = new Random();

        double percent;
        int spawnCount;

        for(long monsterId : spawnMonster) {
            percent = this.spawnPercent.get(Id.MONSTER).get(monsterId);
            spawnCount = random.nextInt(this.spawnMaxCount.get(monsterId)) + 1;

            if(random.nextDouble() < percent || percent == 1) {
                for(int count = 0; count < spawnCount; count++) {
                    int fieldX = random.nextInt(Config.MAX_FIELD_X - 4) + 5;
                    int fieldY = random.nextInt(Config.MAX_FIELD_Y - 4) + 5;

                    Monster monster = Config.newObject(Config.getData(Id.MONSTER, monsterId));
                    monster.randomLevel();
                    monster.location = new Location();
                    moveManager.setMap(monster, this.location.getX().get(), this.location.getY().get(), fieldX, fieldY);
                    this.addEntity(monster);
                    Config.unloadObject(monster);
                }
            }
        }

        for(long bossId : spawnBoss) {
            percent = this.spawnPercent.get(Id.MONSTER).get(bossId);

            if(random.nextDouble() < percent || percent == 1) {
                int fieldX = random.nextInt(Config.MAX_FIELD_X) + 1;
                int fieldY = random.nextInt(Config.MAX_FIELD_Y) + 1;

                Boss boss = Config.newObject(Config.getData(Id.BOSS, bossId));
                boss.randomLevel();
                boss.location = new Location();
                moveManager.setMap(boss, this.location.getX().get(), this.location.getY().get(), fieldX, fieldY);
                this.addEntity(boss);
                Config.unloadObject(boss);

                //Boss must exist only one
                break;
            }
        }
    }

}