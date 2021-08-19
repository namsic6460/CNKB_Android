package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;

public enum EventList {

    NONE(0L),
    ENT_DAMAGED(1L),
    OAK_START_FIGHT(2L),
    SKILL_SMITE(3L);

    public static final Map<Long, EventList> idMap = new HashMap<>();

    static {
        for(EventList value : EventList.values()) {
            idMap.put(value.id, value);
        }
    }

    @Getter
    private final long id;

    EventList(long id) {
        this.id = id;
    }

    @NonNull
    public static EventList findById(long id) {
        return Objects.requireNonNull(idMap.get(id));
    }

}
