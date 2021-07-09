package lkd.namsic.game.enums.object_list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum EquipList {

    NONE("NONE", 0L);

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
