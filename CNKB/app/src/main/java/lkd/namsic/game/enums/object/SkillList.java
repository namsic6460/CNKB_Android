package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

public enum SkillList {

    NONE("NONE", 0L),
    MAGIC_BALL("매직 볼", 1L),
    SMITE("강타", 2L, EventList.SKILL_SMITE_PRE_DAMAGE),
    LASER("레이저", 3L),
    SCAR("할퀴기", 4L),
    CHARM("매혹", 5L),
    STRINGS_OF_LIFE("생명의 끈", 6L, EventList.SKILL_STRINGS_OF_LIFE_START, EventList.SKILL_STRINGS_OF_LIFE_INJECT),
    RESIST("버티기", 7L),
    RUSH("돌진", 8L),
    ROAR("포효", 9L);

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
    private final List<Long> eventList = new ArrayList<>();

    SkillList(@NonNull String displayName, long id) {
        this.displayName = displayName;
        this.id = id;
    }

    SkillList(@NonNull String displayName, long id, @Nullable EventList...eventDataList) {
        this.displayName = displayName;
        this.id = id;

        if(eventDataList != null) {
            for (EventList eventData : eventDataList) {
                this.eventList.add(eventData.getId());
            }
        }
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
