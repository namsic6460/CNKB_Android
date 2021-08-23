package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum SkillList {

    NONE("NONE", 0L),
    MAGIC_BALL("매직 볼", 1L),
    SMITE("강타", 2L, EventList.SKILL_SMITE),
    LASER("레이저", 3L);

    public static final Map<String, SkillList> nameMap = new HashMap<>();
    public static final Map<Long, SkillList> idMap = new HashMap<>();

    static {
        for(SkillList value : SkillList.values()) {
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

    @Getter
    private final long eventId;

    SkillList(@NonNull String displayName, long id) {
        this(displayName, id, EventList.NONE);
    }

    SkillList(@NonNull String displayName, long id, @NonNull EventList eventData) {
        this.displayName = displayName;
        this.id = id;
        this.eventId = eventData.getId();
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
