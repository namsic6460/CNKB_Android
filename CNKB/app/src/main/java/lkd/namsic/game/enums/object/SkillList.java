package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum SkillList {

    NONE("NONE", 0L),
    MAGIC_BALL("마력 구체", 1L);

    public static final Map<String, SkillList> nameMap = new HashMap<>();
    public static final Map<Long, SkillList> idMap = new HashMap<>();

    static {
        for(SkillList value : SkillList.values()) {
            nameMap.put(value.displayName, value);
            idMap.put(value.id, value);
        }
    }

    @Getter
    @NonNull
    private final String displayName;

    @Getter
    private final long id;

    @Getter
    private final long eventId;

    SkillList(@NonNull String displayName, long id) {
        this(displayName, id, EventList.NONE.getId());
    }

    SkillList(@NonNull String displayName, long id, long eventId) {
        this.displayName = displayName;
        this.id = id;
        this.eventId = eventId;
    }

    @Nullable
    public static Long findByName(@NonNull String name) {
        SkillList boss = nameMap.get(name);
        return boss == null ? null : boss.id;
    }

    @Nullable
    public static String findById(long id) {
        SkillList skillData = idMap.get(id);
        return skillData == null ? null : skillData.displayName;
    }

}
