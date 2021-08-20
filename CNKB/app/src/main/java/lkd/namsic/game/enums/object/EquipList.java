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
    MIX_SWORD("합검", 3L),
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
    DEMON_BONE_SHOES("마족의 뼈 신발", 89L);

    public static final Map<String, EquipList> nameMap = new HashMap<>();
    public static final Map<Long, EquipList> idMap = new HashMap<>();

    static {
        for(EquipList value : EquipList.values()) {
            nameMap.put(value.displayName, value);
            idMap.put(value.id, value);
        }
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
