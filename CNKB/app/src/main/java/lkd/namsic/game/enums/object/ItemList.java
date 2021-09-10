package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum ItemList {

    NONE("NONE", 0L),

    COBBLE_STONE("돌멩이", 1L),
    BRANCH("나뭇가지", 2L),
    LEAF("나뭇잎", 3L),
    GRASS("잡초", 4L),
    SMALL_GOLD_BAG("골드 주머니", 5L),
    GOLD_BAG("골드 보따리", 6L),
    HERB("약초", 7L),
    PIECE_OF_MANA("마나 조각", 8L),
    DIRT("흙", 10L),
    SAND("모래", 11L),
    GLASS("유리", 12L),
    GLASS_BOTTLE("유리병", 13L),

    LOW_HP_POTION("하급 체력 포션", 14L),
    HP_POTION("중급 체력 포션", 15L),
    HIGH_HP_POTION("상급 체력 포션", 16L),
    LOW_MP_POTION("하급 마나 포션", 17L),
    MP_POTION("중급 마나 포션", 18L),
    HIGH_MP_POTION("상급 마나 포션", 19L),

    STONE("돌", 20L),
    COAL("석탄", 21L),
    QUARTZ("석영", 22L),
    COPPER("구리", 23L),
    LEAD("납", 24L),
    TIN("주석", 25L),
    NICKEL("니켈", 26L),
    IRON("철", 27L),
    LITHIUM("리튬", 28L),
    LAPIS("청금석", 29L),
    RED_STONE("레드스톤", 30L),
    SILVER("은", 31L),
    GOLD("금", 32L),
    GLOW_STONE("발광석", 33L),
    FIRE_QUARTZ("화염 석영", 34L),
    DARK_QUARTZ("암흑 석영", 35L),
    GLOW_LAPIS("명청석", 36L),
    GLOW_RED_STONE("명적석", 37L),
    WHITE_GOLD("백금", 38L),
    HARD_COAL("무연탄", 39L),
    TITANIUM("티타늄", 40L),
    LIQUID_STONE("투명석", 41L),
    DIAMOND("다이아몬드", 42L),
    ORICHALCON("오리하르콘", 43L),
    LAPIS_RED_STONE("적청석", 44L),
    LANDIUM("랜디움", 45L),
    AITUME("에이튬", 46L),

    GARNET("가넷", 47L),
    AMETHYST("자수정", 48L),
    AQUAMARINE("아쿠아마린", 49L),
    EMERALD("에메랄드", 50L),
    PEARL("진주", 51L),
    RUBY("루비", 52L),
    PERIDOT("페리도트", 53L),
    SAPPHIRE("사파이어", 54L),
    OPAL("오팔", 55L),
    TOPAZ("토파즈", 56L),
    TURQUOISE("터키석", 57L),

    TRASH("쓰레기", 58L),
    WATER_GRASS("물풀", 59L),
    COMMON_FISH1("(일반) 금강모치", 60L),
    COMMON_FISH2("(일반) 미꾸라지", 61L),
    COMMON_FISH3("(일반) 붕어", 62L),
    COMMON_FISH4("(일반) 송사리", 63L),
    COMMON_FISH5("(일반) 피라미", 64L),
    RARE_FISH1("(희귀) 망둑어", 65L),
    RARE_FISH2("(희귀) 미꾸라지", 66L),
    RARE_FISH3("(희귀) 배스", 67L),
    RARE_FISH4("(희귀) 살치", 68L),
    RARE_FISH5("(희귀) 쏘가리", 69L),
    RARE_FISH6("(희귀) 은어", 70L),
    SPECIAL_FISH1("(특별) 강준치", 71L),
    SPECIAL_FISH2("(특별) 망둑어", 72L),
    SPECIAL_FISH3("(특별) 메기", 73L),
    SPECIAL_FISH4("(특별) 뱀장어", 74L),
    SPECIAL_FISH5("(특별) 산천어", 75L),
    SPECIAL_FISH6("(특별) 숭어", 76L),
    SPECIAL_FISH7("(특별) 쏘가리", 77L),
    SPECIAL_FISH8("(특별) 연어", 78L),
    SPECIAL_FISH9("(특별) 은어", 79L),
    SPECIAL_FISH10("(특별) 잉어", 80L),
    UNIQUE_FISH1("(유일) 강준치", 81L),
    UNIQUE_FISH2("(유일) 메기", 82L),
    UNIQUE_FISH3("(유일) 뱀장어", 83L),
    UNIQUE_FISH4("(유일) 산천어", 84L),
    UNIQUE_FISH5("(유일) 숭어", 85L),
    UNIQUE_FISH6("(유일) 연어", 86L),
    UNIQUE_FISH7("(유일) 잉어", 87L),
    LEGENDARY_FISH1("(전설) 다금바리", 88L),
    LEGENDARY_FISH2("(전설) 돗돔", 89L),
    LEGENDARY_FISH3("(전설) 자치", 90L),
    LEGENDARY_FISH4("(전설) 쿠니마스", 91L),
    MYSTIC_FISH1("(신화) 실러캔스", 92L),
    MYSTIC_FISH2("(신화) 폐어", 93L),

    PVP_DISABLE_1("전투 비활성화권(1일)", 94L),
    PVP_DISABLE_7("전투 비활성화권(7일)", 95L),

    STAT_POINT("스텟 포인트", 96L),
    ADV_STAT("모험 스텟", 97L),
    LOW_RECIPE("하급 제작법", 98L),
    RECIPE("중급 제작법", 99L),
    HIGH_RECIPE("상급 제작법", 100L),

    RED_SPHERE("붉은색 구체", 101L),
    GREEN_SPHERE("녹색 구체", 102L),
    LIGHT_GREEN_SPHERE("연녹색 구체", 103L),
    BLUE_SPHERE("파란색 구체", 104L),
    BROWN_SPHERE("갈색 구체", 105L),
    GRAY_SPHERE("회색 구체", 106L),
    SILVER_SPHERE("은색 구체", 107L),
    LIGHT_GRAY_SPHERE("연회색 구체", 108L),
    YELLOW_SPHERE("노란색 구체", 109L),
    BLACK_SPHERE("검은색 구체", 110L),
    WHITE_SPHERE("흰색 구체", 111L),

    LOW_EXP_POTION("하급 경험치 포션", 112L),
    EXP_POTION("중급 경험치 포션", 113L),
    HIGH_EXP_POTION("상급 경험치 포션", 114L),
    LOW_ADV_TOKEN("하급 모험의 증표", 115L),
    ADV_TOKEN("중급 모험의 증표", 116L),
    HIGH_ADV_TOKEN("상급 모험의 증표", 117L),

    EMPTY_SPHERE("무색의 구체", 118L),

    LOW_REINFORCE_STONE("하급 강화석", 119L),
    REINFORCE_STONE("중급 강화석", 120L),
    HIGH_REINFORCE_STONE("상급 강화석", 121L),

    PIECE_OF_LOW_AMULET("하급 부적 파편", 122L),
    PIECE_OF_AMULET("중급 부적 파편", 123L),
    PIECE_OF_HIGH_AMULET("상급 부적 파편", 124L),

    PIECE_OF_GEM("보석 조각", 125L),

    //TODO
    GEM_ABRASIVE_MATERIAL("보석 연마제", 126L),
    GLOW_GEM_ABRASIVE_MATERIAL("빛나는 보석 연마제", 127L),

    EQUIP_SAFENER("장비 완화제", 128L),

    LOW_MINER_TOKEN("하급 광부의 증표", 129L),
    MINER_TOKEN("중급 광부의 증표", 130L),
    HIGH_MINER_TOKEN("상급 광부의 증표", 131L),
    LOW_FISH_TOKEN("하급 낚시꾼의 증표", 132L),
    FISH_TOKEN("중급 낚시꾼의 증표", 133L),
    HIGH_FISH_TOKEN("상급 낚시꾼의 증표", 134L),
    LOW_HUNTER_TOKEN("하급 사냥꾼의 증표", 135L),
    HUNTER_TOKEN("중급 사냥꾼의 증표", 136L),
    HIGH_HUNTER_TOKEN("상급 사냥꾼의 증표", 137L),

    LOW_TOKEN("하급 증표", 138L),
    TOKEN("중급 증표", 139L),
    HIGH_TOKEN("상급 증표", 140L),

    LOW_AMULET("하급 부적", 141L),
    AMULET("중급 부적", 142L),
    HIGH_AMULET("상급 부적", 143L),

    LAMB("양고기", 144L),
    SHEEP_LEATHER("양 가죽", 145L),
    WOOL("양털", 146L),

    PORK("돼지고기", 147L),
    PIG_HEAD("돼지 머리", 148L),

    BEEF("소고기", 149L),
    LEATHER("가죽", 150L),

    ZOMBIE_HEAD("좀비 머리", 151L),
    ZOMBIE_SOUL("좀비 영혼", 152L),
    ZOMBIE_HEART("좀비 심장", 153L),

    LOW_ALLOY("하급 합금", 154L),
    ALLOY("중급 합금", 155L),
    HIGH_ALLOY("상급 합금", 156L),

    PIECE_OF_SLIME("슬라임 조각", 157L),

    SPIDER_LEG("거미 다리", 158L),
    SPIDER_EYE("거미 눈", 159L),

    HARD_IRON("강철", 160L),

    ELIXIR_HERB("엘릭서 허브", 161L),

    LOW_ELIXIR("하급 엘릭서", 162L),
    ELIXIR("중급 엘릭서", 163L),
    HIGH_ELIXIR("상급 엘릭서", 164L),

    COMMON_GRILLED_FISH("(일반) 생선구이", 165L),
    RARE_GRILLED_FISH("(희귀) 생선구이", 166L),
    SPECIAL_GRILLED_FISH("(특별) 생선구이", 167L),
    UNIQUE_GRILLED_FISH("(유일) 생선구이", 168L),
    LEGENDARY_GRILLED_FISH("(전설) 생선구이", 169L),
    MYSTIC_GRILLED_FISH("(신화) 생선구이", 170L),

    COOKED_LAMB("익힌 양고기", 171L),
    COOKED_PORT("익힌 돼지고기", 172L),
    COOKED_BEEF("익힌 소고기", 173L),

    MAGIC_STONE("마정석", 177L),

    FLOWER("꽃", 178L),
    STONE_LUMP("돌 뭉텅이", 179L),

    REINFORCE_MULTIPLIER("강화 증폭제", 180L),
    GLOW_REINFORCE_MULTIPLIER("빛나는 강화 증폭제", 181L),

    OAK_TOOTH("오크 이빨", 182L),
    OAK_LEATHER("오크 가죽", 183L),
    
    SKILL_BOOK_MAGIC_BALL("스킬 북 - 매직 볼", 184L),
    SKILL_BOOK_SMITE("스킬 북 - 강타", 185L),
    SKILL_BOOK_LASER("스킬 북 - 레이저", 186L),

    PIECE_OF_BONE("뼛조각", 187L),
    HORN_OF_IMP("임프의 뿔", 188L),
    IMP_HEART("임프의 심장", 189L),
    HORN_OF_LOW_DEVIL("하급 악마의 뿔", 190L),
    LOW_DEVIL_SOUL("하급 악마의 영혼", 191L),

    GOLD_FRUIT("골드 열매", 192L),
    EXP_FRUIT("경험치 열매", 193L),
    SMALL_GOLD_SEED("작은 골드 씨앗", 194L),
    GOLD_SEED("골드 씨앗", 195L),
    BIG_GOLD_SEED("큰 골드 씨앗", 196L),
    SMALL_EXP_SEED("작은 경험치 씨앗", 197L),
    EXP_SEED("경험치 씨앗", 198L),
    BIG_EXP_SEED("큰 경험치 씨앗", 199L),

    HARPY_WING("하피의 날개", 200L),
    HARPY_NAIL("하피의 손톱", 201L),

    GOLEM_CORE("골렘 코어", 202L),

    SKILL_BOOK_SCAR("스킬 북 - 할퀴기", 203L),
    SKILL_BOOK_CHARM("스킬 북 - 매혹", 204L),
    SKILL_BOOK_STRINGS_OF_LIFE("스킬 북 - 생명의 끈", 205L),
    SKILL_BOOK_RESIST("스킬 북 - 버티기", 206L),
    
    PIECE_OF_MAGIC("마법 파편", 207L),

    CONFIRMED_LOW_RECIPE("확정 하급 제작법", 208L),
    CONFIRMED_RECIPE("확정 중급 제작법", 209L),
    CONFIRMED_HIGH_RECIPE("확정 상급 제작법", 210L),

    OWLBEAR_LEATHER("곰 가죽", 211L),

    SKILL_BOOK_RUSH("스킬 북 - 돌진", 212L),
    SKILL_BOOK_ROAR("스킬 북 - 포효", 213L),

    CORRUPTED_MAGIC_STONE("오염된 마정석", 214L),

    PURIFYING_FRUIT("정화의 열매", 215L),
    SMALL_PURIFYING_SEED("작은 정화의 씨앗", 216L),
    PURIFYING_SEED("정화의 씨앗", 217L),
    BIG_PURIFYING_SEED("큰 정화의 씨앗", 218L),

    HARDENED_SLIME("굳은 슬라임", 219L),
    OWLBEAR_HEAD("아울베어 머리", 220L),

    FARM_EXPAND_DEED("농장 확장 증서", 221L),

    TITANIUM_STEEL("티타늄 강", 222L),

    TRACE_OF_SKY("건의 흔적", 223L),
    TRACE_OF_EARTH("곤의 흔적", 224L),
    TRACE_OF_WATER("감의 흔적", 225L),
    TRACE_OF_FIRE("리의 흔적", 226L),

    LYCANTHROPE_TOOTH("라이칸스로프 이빨", 227L),
    LYCANTHROPE_LEATHER("라이칸스로프 가죽", 228L),
    LYCANTHROPE_HEAD("라이칸스로프 머리", 229L),
    LYCANTHROPE_HEART("라이칸스로프 심장", 230L),
    LYCANTHROPE_SOUL("라이칸스로프 영혼", 231L),

    MOON_STONE("월석", 232L),

    SKILL_BOOK_CUTTING_MOONLIGHT("스킬 북 - 달빛 베기", 233L);

    public static final Map<String, ItemList> nameMap = new HashMap<>();
    public static final Map<Long, ItemList> idMap = new HashMap<>();

    static {
        for(ItemList value : ItemList.values()) {
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

    ItemList(@NonNull String displayName, long id) {
        this.displayName = displayName;
        this.id = id;
    }

    @Nullable
    public static Long findByName(@NonNull String name) {
        ItemList item = nameMap.get(name);
        return item == null ? null : item.id;
    }

    @Nullable
    public static String findById(long id) {
        ItemList item = idMap.get(id);
        return item == null ? null : item.displayName;
    }

}
