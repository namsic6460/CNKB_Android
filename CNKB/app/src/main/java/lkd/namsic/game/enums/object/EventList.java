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
    SKILL_SMITE(3L),
    IMP_ATTACK(4L),
    SCAR_BLOOD(5L),
    SCAR_END(6L),
    CHARM(7L),
    CHARM_END(8L),
    CHARM_ATTACK(9L),
    CHARM_REMOVE(10L),
    GOLEM_ATTACKED(11L);

    public static final Map<Long, EventList> idMap = new HashMap<>();

    static {
        for(EventList value : EventList.values()) {
            idMap.put(value.id, value);
        }

        idMap.remove(NONE.id);
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
