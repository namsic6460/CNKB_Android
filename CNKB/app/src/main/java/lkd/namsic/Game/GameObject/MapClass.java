package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Base.ConcurrentHashSet;
import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.MapType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lkd.namsic.Game.Exception.UnhandledEnumException;
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
    boolean pvp = false;

    @Setter
    @NonNull
    MapType mapType = MapType.COUNTRY;

    LimitInteger requireLv = new LimitInteger(Config.MIN_LV, Config.MIN_LV, Config.MAX_LV);

    Location location = new Location();

    Map<Id, Map<Long, Double>> spawnMonster;

    //This part can be frequently changed
    Map<Location, Long> money = new ConcurrentHashMap<>();
    Map<Location, ConcurrentHashMap<Long, Integer>> item = new ConcurrentHashMap<>();
    Map<Location, ConcurrentHashSet<Long>> equip = new ConcurrentHashMap<>();
    Map<Id, ConcurrentHashSet<Long>> entity = new ConcurrentHashMap<>();

    public MapClass(@NonNull String name) {
        this.name = name;
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
            Double value = spawnMonster.get(monsterId);

            if(value != null) {
                return value;
            } else {
                return 0;
            }
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
