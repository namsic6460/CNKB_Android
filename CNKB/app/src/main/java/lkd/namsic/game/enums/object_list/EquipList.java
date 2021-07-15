package lkd.namsic.game.enums.object_list;

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
    DRAGON_AMULET("용의 부적", 9L);

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
