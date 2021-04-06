package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Base.ConcurrentHashSet;
import lkd.namsic.Game.Base.LimitInteger;
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

    public void addEntity(Id id, long objectId) {
        Id.checkEntityId(id);
        Config.checkId(id, objectId);

        this.getEntity(id).add(objectId);
    }

    public void removeEntity(Id id, long objectId) {
        Id.checkEntityId(id);
        Config.checkId(id, objectId);

        this.getEntity(id).remove(objectId);
    }

}
