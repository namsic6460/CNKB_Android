package lkd.namsic.game.enums.object_list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum QuestList {

    NONE("NONE", 0L),

    WORK_OF_MINER("광부의 일", 1L),
    TRASH_COLLECTING("쓰레기 수거", 2L),
    NEED_FIRE("불이 필요해!", 3L),
    TOO_STRONG_FIRE("불이 너무 강했나?", 4L),
    PROVE_EXPERIENCE("경험을 증명해라", 5L),
    NEED_FISHING_ROD_ITEM("낚싯대 재료 구해오기", 6L),
    HUNTER_TALENT("증표의 힘?", 7L),
    GOLD_RING("금반지 선물", 8L);

    public static final Map<String, QuestList> nameMap = new HashMap<>();
    public static final Map<Long, QuestList> idMap = new HashMap<>();

    static {
        for(QuestList value : QuestList.values()) {
            nameMap.put(value.displayName, value);
            idMap.put(value.id, value);
        }
    }

    @Getter
    @NonNull
    private final String displayName;

    @Getter
    private final long id;

    QuestList(@NonNull String displayName, long id) {
        this.displayName = displayName;
        this.id = id;
    }

    @Nullable
    public static Long findByName(@NonNull String name) {
        QuestList quest = nameMap.get(name);
        return quest == null ? null : quest.id;
    }

    @Nullable
    public static String findById(long id) {
        QuestList quest = idMap.get(id);
        return quest == null ? null : quest.displayName;
    }

}
