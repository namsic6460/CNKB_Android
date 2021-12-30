package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum BossList {
    
    NONE("NONE", 0L),
    WOLF_OF_MOON("달의 늑대", 1L),
    SUCCUBUS("서큐버스", 2L);
    
    public static final Map<String, BossList> nameMap = new HashMap<>();
    public static final Map<Long, BossList> idMap = new HashMap<>();
    
    static {
        for(BossList value : BossList.values()) {
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
