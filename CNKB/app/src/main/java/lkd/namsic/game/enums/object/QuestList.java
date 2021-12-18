package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum QuestList {
    
    NONE("NONE", 0L),
    
    WORK_OF_MINER("광부의 일", 1L),
    TRASH_COLLECTING("쓰레기 수거", 2L),
    NEED_FIRE("불이 필요해!", 3L),
    TOO_STRONG_FIRE("불이 너무 강했나?", 4L),
    PROVE_EXPERIENCE("경험을 증명해라", 5L),
    NEED_FISHING_ROD_ITEM("낚싯대 재료 구해오기", 6L),
    POWER_OF_TOKEN("증표의 힘?", 7L),
    GOLD_RING_GIFT("금반지 선물", 8L),
    NEED_COAL("석탄이 다 어디갔지?", 9L),
    ANOTHER_PRESENT("또 다른 선물", 10L),
    LV50("50레벨 달성", 11L),
    LV100("100레벨 달성", 12L),
    MEMORIAL_CEREMONY("제사용 돼지머리", 13L),
    MAGIC_OF_SPIDER_EYE("거미 눈의 마법", 14L),
    STICKY_SLIME("끈적한 슬라임", 15L),
    HERB_COLLECTING("약초 모으기", 16L),
    GEM_COLLECTING_QUARTZ("보석 수집 - 석영", 17L),
    GEM_COLLECTING_GOLD("보석 수집 - 금", 18L),
    GEM_COLLECTING_WHITE_GOLD("보석 수집 - 백금", 19L),
    GEM_COLLECTING_GARNET("보석 수집 - 가넷", 20L),
    GEM_COLLECTING_AMETHYST("보석 수집 - 자수정", 21L),
    GEM_COLLECTING_AQUAMARINE("보석 수집 - 아쿠아마린", 22L),
    GEM_COLLECTING_DIAMOND("보석 수집 - 다이아몬드", 23L),
    GEM_COLLECTING_EMERALD("보석 수집 - 에메랄드", 24L),
    GEM_COLLECTING_PEARL("보석 수집 - 진주", 25L),
    GEM_COLLECTING_RUBY("보석 수집 - 루비", 26L),
    GEM_COLLECTING_PERIDOT("보석 수집 - 페리도트", 27L),
    GEM_COLLECTING_SAPPHIRE("보석 수집 - 사파이어", 28L),
    GEM_COLLECTING_OPAL("보석 수집 - 오팔", 29L),
    GEM_COLLECTING_TOPAZ("보석 수집 - 토파즈", 30L),
    GEM_COLLECTING_TURQUOISE("보석 수집 - 터키석", 31L),
    HEALING_ELF1("엘프 치료하기1", 33L),
    HEALING_ELF2("엘프 치료하기2", 34L),
    LEATHER_COLLECTING1("가죽 모으기1", 35L),
    LEATHER_COLLECTING2("가죽 모으기2", 36L),
    LEATHER_COLLECTING3("가죽 모으기3", 37L),
    INCREASING_ZOMBIE("불어나는 좀비들", 38L),
    BONE_IN_THE_SEA("바닷속의 뼛조각", 39L),
    SOUND_IN_THE_SINKHOLE("싱크홀에서의 소리", 40L);
    
    public static final Map<String, QuestList> nameMap = new HashMap<>();
    public static final Map<Long, QuestList> idMap = new HashMap<>();
    
    static {
        for(QuestList value : QuestList.values()) {
            nameMap.put(value.displayName, value);
            idMap.put(value.id, value);
        }
        
        nameMap.remove(NONE.displayName);
        idMap.remove(NONE.id);
    }
    
    @Getter
    @NonNull
    private final String displayName;
    
    @Getter
    private final long id;
    
    QuestList(@NonNull String displayName, long id) {
        this.displayName = displayName;
        this.id = id;
    }
    
    @Nullable
    public static Long findByName(@NonNull String name) {
        QuestList quest = nameMap.get(name);
        return quest == null ? null : quest.id;
    }
    
    @Nullable
    public static String findById(long id) {
        QuestList quest = idMap.get(id);
        return quest == null ? null : quest.displayName;
    }
    
}
