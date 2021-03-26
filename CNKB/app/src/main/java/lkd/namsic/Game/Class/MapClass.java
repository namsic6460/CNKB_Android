package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Base.ConcurrentHashSet;
import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Enum.MapType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MapClass implements Serializable {

    Location location;
    MapType mapType;

    ConcurrentHashMap<Long, Integer> dropItem;
    ConcurrentHashMap<Long, Integer> dropEquip;

    ConcurrentHashSet<Long> npc;
    ConcurrentHashSet<Long> monster;

}
