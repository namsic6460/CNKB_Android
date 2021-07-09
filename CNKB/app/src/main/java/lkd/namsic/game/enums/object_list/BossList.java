package lkd.namsic.game.enums.object_list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum BossList {

    NONE("NONE", 0L);

    public static final Map<String, BossList> nameMap = new HashMap<>();
    public static final Map<Long, BossList> idMap = new HashMap<>();

    static {
        for(BossList value : BossList.values()) {
            nameMap.put(value.displayName, value);
            idMap.put(value.id, value);
        }
    }

    @Getter
    @NonNull
    private final String displayName;

    @Getter
    private final long id;

    BossList(@NonNull String displayName, long id) {
        this.displayName = displayName;
        this.id = id;
    }

    @Nullable
    public static Long findByName(@NonNull String name) {
        BossList boss = nameMap.get(name);
        return boss == null ? null : boss.id;
    }

    @Nullable
    public static String findById(long id) {
        BossList boss = idMap.get(id);
        return boss == null ? null : boss.displayName;
    }

}
