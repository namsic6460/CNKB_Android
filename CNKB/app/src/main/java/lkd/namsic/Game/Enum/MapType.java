package lkd.namsic.Game.Enum;

public enum MapType {

    COUNTRY("도시"),
    FIELD("평원"),
    MOUNTAIN("산"),
    RIVER("강"),
    CEMETRY("공동묘지"),
    CAVE("동굴"),
    SWAMP("늪지대"),
    FOG("안개 지역"),
    SEA("바다"),
    SINKHOLE("싱크홀 구역"),
    UNDERGROUND_CITY("지하도시"),
    ABANDONED_CITY("폐허"),
    CORRUPTED_RIVER("오염된 강"),
    HUGE_CAVE("거대 동굴"),
    POISON_SWAMP("독안개 지역"),
    ANCIENT_CEMETRY("고대의 공동묘지"),
    VOLCANO("화산지대"),
    HELL("지옥"),
    HEAVEN("천국"),
    FAR_SIDE("먼 곳 어딘가");

    public String name;

    MapType(String name) {
        this.name = name;
    }

}
