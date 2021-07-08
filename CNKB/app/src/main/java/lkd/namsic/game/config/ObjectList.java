package lkd.namsic.game.config;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public class ObjectList {

    public final static BiMap<String, Long> equipList = new ImmutableBiMap.Builder<String, Long>()
            .build();

    public final static BiMap<String, Long> monsterList = new ImmutableBiMap.Builder<String, Long>()
            .put("양", 1L)
            .build();

    public final static BiMap<String, Long> bossList = new ImmutableBiMap.Builder<String, Long>()
            .build();

    public final static BiMap<String, Long> questList = new ImmutableBiMap.Builder<String, Long>()
            .put("광부의 일", 1L)
            .put("쓰레기 수거", 2L)
            .put("불이 필요해!", 3L)
            .put("불이 너무 강했나...?", 4L)
            .build();

    public final static BiMap<String, Long> npcList = new ImmutableBiMap.Builder<String, Long>()
            .put("???", 1L)
            .put("아벨", 2L)
            .put("노아", 3L)
            .put("형석", 4L)
            .put("봄이", 5L)
            .put("엘", 6L)
            .put("준식", 7L)
            .put("강태공", 8L)
            .put("페드로", 9L)
            .put("무명", 10L)
            .put("셀리나", 11L)
            .build();

    public final static BiMap<String, String> mapList = new ImmutableBiMap.Builder<String, String>()
            .put("0-0", "시작의 마을")
            .put("0-1", "조용한 바닷가")
            .put("1-0", "모험의 평원")
            .put("1-1", "평화로운 강")
            .build();

}
