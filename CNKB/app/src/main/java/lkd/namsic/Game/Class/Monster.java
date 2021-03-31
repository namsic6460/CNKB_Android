package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Base.ConcurrentArrayList;
import lkd.namsic.Game.Base.ConcurrentHashSet;
import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Enum.Doing;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Event.Event;
import lombok.ToString;

@ToString
public class Monster extends AiEntity {

    public Monster(@NonNull String name, int lv, long money, @NonNull Location location,
                   @NonNull Doing doing, @NonNull ConcurrentHashMap<StatType, Integer> basicStat,
                   @NonNull ConcurrentHashSet<Long> equip,
                   @NonNull ConcurrentHashMap<Long, ConcurrentHashMap<StatType, Integer>> buff,
                   @NonNull ConcurrentHashMap<Long, Integer> inventory,
                   @NonNull ConcurrentHashSet<Long> equipInventory,
                   @NonNull ConcurrentHashMap<Id, ConcurrentHashSet<Long>> enemies,
                   @NonNull ConcurrentHashMap<String, ConcurrentArrayList<Event>> events) {
        super(name, lv, money, location, doing, basicStat, equip, buff, inventory, equipInventory, enemies, events);

        this.id.setId(Id.MONSTER);
    }

}
