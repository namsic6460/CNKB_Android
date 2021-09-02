package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum EquipList {

    NONE("NONE", 0L),
    WOODEN_SWORD("목검", 1L),
    IRON_SWORD("철검", 2L),
    MIX_SWORD_1("합검1", 3L),
    HEART_BREAKER_1("하트 브레이커1", 4L),
    HEAD_HUNTER_1("헤드 헌터1", 5L),
    GHOST_SWORD_1("원혼의 검1", 6L),
    HEALTH_AMULET("건강의 부적", 7L),
    BLOOD_AMULET("피의 부적", 8L),
    DRAGON_AMULET("용의 부적", 9L),
    LEATHER_HELMET("가죽 투구", 10L),
    LEATHER_CHESTPLATE("가죽 갑옷", 11L),
    LEATHER_LEGGINGS("가죽 바지", 12L),
    LEATHER_SHOES("가죽 신발", 13L),
    LOW_ALLOY_HELMET("하급 합금 투구", 14L),
    LOW_ALLOY_CHESTPLATE("하급 합금 갑옷", 15L),
    LOW_ALLOY_LEGGINGS("하급 합금 바지", 16L),
    LOW_ALLOY_SHOES("하급 합금 신발", 17L),
    LOW_MANA_SWORD("하급 마나소드", 18L),
    QUARTZ_SWORD("석영 검", 19L),
    SLIME_HELMET("슬라임 투구", 20L),
    SLIME_CHESTPLATE("슬라임 갑옷", 21L),
    SLIME_LEGGINGS("슬라임 바지", 22L),
    SLIME_SHOES("슬라임 신발", 23L),
    WOOL_HELMET("양털 모자", 24L),
    HARD_IRON_CHESTPLATE("강철 갑옷", 25L),
    WEIRD_LEGGINGS("기괴한 바지", 26L),
    MINER_SHOES("광부의 신발", 27L),
    TROLL_CLUB("트롤의 몽둥이", 28L),
    BETA1_GEM("베타 보석", 38L),
    OAK_TOOTH_NECKLACE("오크 이빨 목걸이", 55L),
    BONE_SWORD("뼈검", 75L),
    BASIC_STAFF("기본 스태프", 76L),
    SEA_STAFF("바다의 스태프", 77L),
    DEMON_STAFF("마족의 스태프", 78L),
    BONE_HELMET("뼈 투구", 79L),
    BONE_CHESTPLATE("뼈 갑옷", 80L),
    BONE_LEGGINGS("뼈 바지", 81L),
    BONE_SHOES("뼈 신발", 82L),
    DEVIL_RING("악마의 반지", 83L),
    HEART_BREAKER_2("하트 브레이커2", 84L),
    GHOST_SWORD_2("원혼의 검2", 85L),
    DEMON_BONE_HELMET("마족의 뼈 투구", 86L),
    DEMON_BONE_CHESTPLATE("마족의 뼈 갑옷", 87L),
    DEMON_BONE_LEGGINGS("마족의 뼈 바지", 88L),
    DEMON_BONE_SHOES("마족의 뼈 신발", 89L),
    HEAD_HUNTER_2("헤드 헌터2", 184L),
    YIN_YANG_SWORD_1("음양검1", 185L),
    SILVER_SWORD("은검", 186L),
    SILVER_HELMET("은 투구", 187L),
    SILVER_CHESTPLATE("은 갑옷", 188L),
    SILVER_LEGGINGS("은 바지", 189L),
    SILVER_SHOES("은 신발", 190L),
    YIN_YANG_SWORD_2("음양검2", 191L),
    MIX_SWORD_2("합검2", 192L),
    ALLOY_HELMET("합금 투구", 193L),
    ALLOY_CHESTPLATE("합금 갑옷", 194L),
    ALLOY_LEGGINGS("합금 바지", 195L),
    ALLOY_SHOES("합금 신발", 196L),
    TITANIUM_HELMET("티타늄 투구", 197L),
    TITANIUM_CHESTPLATE("티타늄 갑옷", 198L),
    TITANIUM_LEGGINGS("티타늄 바지", 199L),
    TITANIUM_SHOES("티타늄 신발", 200L),
    HARPY_NAIL_GAUNTLETS("하피 손톱 건틀릿", 201L),
    OWLBEAR_LEATHER_HELMET("아울베어 가죽 투구", 202L),
    OWLBEAR_LEATHER_CHESTPLATE("아울베어 가죽 갑옷", 203L),
    OWLBEAR_LEATHER_LEGGINGS("아울베어 가죽 바지", 204L),
    OWLBEAR_LEATHER_SHOES("아울베어 가죽 신발", 205L),
    HARDENED_SLIME_HELMET("굳은 슬라임 투구", 206L),
    HARDENED_SLIME_CHESTPLATE("굳은 슬라임 갑옷", 207L),
    HARDENED_SLIME_LEGGINGS("굳은 슬라임 바지", 208L),
    HARDENED_SLIME_SHOES("굳은 슬라임 신발", 209L),
    ELEMENT_HEART_GEM("원소 심장 보석", 210L),
    GOLEM_HEART_GEM("골렘 심장 보석", 211L),
    REGENERATION_AMULET("재생의 부적", 212L),
    STRENGTH_AMULET("힘의 부적", 213L),
    PROTECTION_AMULET("보호의 부적", 214L),
    NATURE_AMULET("자연의 부적", 215L),
    DEVIL_AMULET("마족의 부적", 216L),
    SILVER_RING("은 반지", 217L),
    HARPY_NAIL_NECKLACE("하피 손톱 목걸이", 218L),
    GARNET_EARRING("가넷 귀걸이", 219L),
    AMETHYST_EARRING("자수정 귀걸이", 220L),
    AQUAMARINE_EARRING("아쿠아마린 귀걸이", 221L),
    DIAMOND_EARRING("다이아몬드 귀걸이", 222L),
    EMERALD_EARRING("에메랄드 귀걸이", 223L),
    PEARL_EARRING("진주 귀걸이", 224L),
    RUBY_EARRING("루비 귀걸이", 225L),
    PERIDOT_EARRING("페리도트 귀걸이", 226L),
    SAPPHIRE_EARRING("사파이어 귀걸이", 227L),
    OPAL_EARRING("오팔 귀걸이", 228L),
    TOPAZ_EARRING("토파즈 귀걸이", 229L),
    TURQUOISE_EARRING("터키석 귀걸이", 230L);

    public static final Map<String, EquipList> nameMap = new HashMap<>();
    public static final Map<Long, EquipList> idMap = new HashMap<>();

    static {
        for(EquipList value : EquipList.values()) {
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

    EquipList(@NonNull String displayName, long id) {
        this.displayName = displayName;
        this.id = id;
    }

    @Nullable
    public static Long findByName(@NonNull String name) {
        EquipList equip = nameMap.get(name);
        return equip == null ? null : equip.id;
    }

    @Nullable
    public static String findById(long id) {
        EquipList equip = idMap.get(id);
        return equip == null ? null : equip.displayName;
    }

}
