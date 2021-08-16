 package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lombok.Getter;

public enum MapList {

    START_VILLAGE("시작의 마을", 0, 0),
    QUITE_SEASHORE("조용한 바닷가", 0, 1),
    DARK_CAVE("어두운 동굴", 0, 2),
    ADVENTURE_FIELD("모험의 평원", 1, 0),
    PEACEFUL_RIVER("평화로운 강", 1, 1),
    SLIME_SWAMP("슬라임 늪지", 1, 2),
    FERTILE_FARM("비옥한 농장", 2, 0),
    BLUE_FIELD("푸른 초원", 2, 1),
    VILLAGE_CEMETERY("마을 공동묘지", 2, 2),
    GLOOMY_FIELD("스산한 평야", 3, 0),
    OVERGROWN_FOREST("우거진 숲", 3, 1),
    OAK_MOUNTAIN("오크 산", 3, 2);

    public static final Map<String, MapList> nameMap = new HashMap<>();
    public static final Map<Location, MapList> locationMap = new HashMap<>();

    static {
        for(MapList value : MapList.values()) {
            nameMap.put(value.displayName, value);
            locationMap.put(value.location, value);
        }
    }

    @Getter
    @NonNull
    private final String displayName;

    @Getter
    @NonNull
    private final Location location;

    MapList(@NonNull String displayName, int x, int y) {
        this.displayName = displayName;
        this.location = new Location(x, y);
    }

    @Nullable
    public static Location findByName(@NonNull String name) {
        MapList map = nameMap.get(name);
        return map == null ? null : map.location;
    }

    @NonNull
    public static String findByLocation(int x, int y) {
        MapList map = locationMap.get(new Location(x, y));
        return map == null ? Config.INCOMPLETE : map.displayName;
    }

}
