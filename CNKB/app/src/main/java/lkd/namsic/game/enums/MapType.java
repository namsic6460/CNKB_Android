package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public enum MapType {

    COUNTRY("도시"),
    FIELD("평원"),
    MOUNTAIN("산"),
    FOREST("숲"),
    RIVER("강"),
    CEMETERY("공동묘지"),
    CAVE("동굴"),
    SWAMP("늪지대"),
    HILL("언덕"),
    FOG("안개 지역"),
    SEA("바다"),
    SINKHOLE("싱크홀 구역"),
    UNDERGROUND_CITY("지하도시"),
    ABANDONED_CITY("폐허"),
    CORRUPTED_RIVER("오염된 강"),
    HUGE_CAVE("거대 동굴"),
    POISON_SWAMP("독안개 지역"),
    ANCIENT_CEMETERY("고대의 공동묘지"),
    VOLCANO("화산지대"),
    HELL("지옥"),
    HEAVEN("천국"),
    FAR_SIDE("먼 곳 어딘가");

    @Getter
    private final String mapName;

    MapType(String mapName) {
        this.mapName = mapName;
    }

    @NonNull
    public static List<MapType> cityList() {
        List<MapType> list = new ArrayList<>(2);
        list.add(COUNTRY);
        list.add(UNDERGROUND_CITY);

        return list;
    }

    @NonNull
    public static List<MapType> waterList() {
        List<MapType> list = new ArrayList<>(2);
        list.add(RIVER);
        list.add(SEA);

        return list;
    }

    @NonNull
    public static List<MapType> caveList() {
        List<MapType> list = new ArrayList<>(2);
        list.add(CAVE);
        list.add(HUGE_CAVE);

        return list;
    }

}
