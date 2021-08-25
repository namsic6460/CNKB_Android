package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum MonsterList {

    NONE("NONE", 0L),

    SHEEP("양", 1L),
    PIG("돼지", 2L),
    COW("소", 3L),
    ZOMBIE("좀비", 4L),
    SLIME("슬라임", 5L),
    SPIDER("거미", 6L),
    TROLL("트롤", 7L),
    ENT("엔트", 8L),
    OAK("오크", 9L),
    SKELETON("해골", 10L),
    IMP("임프", 11L),
    LOW_DEVIL("하급 악마", 12L),
    HARPY("하피", 13L),
    GOLEM("골렘", 14L);

    public static final Map<String, MonsterList> nameMap = new HashMap<>();
    public static final Map<Long, MonsterList> idMap = new HashMap<>();

    static {
        for(MonsterList value : MonsterList.values()) {
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

    MonsterList(@NonNull String displayName, long id) {
        this.displayName = displayName;
        this.id = id;
    }

    @Nullable
    public static Long findByName(@NonNull String name) {
        MonsterList monster = nameMap.get(name);
        return monster == null ? null : monster.id;
    }

    @Nullable
    public static String findById(long id) {
        MonsterList monster = idMap.get(id);
        return monster == null ? null : monster.displayName;
    }

}
