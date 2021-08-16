package lkd.namsic.game.enums.object;

import lombok.Getter;

public enum EventList {

    ENT_DAMAGED(1L),
    OAK_START_FIGHT(2L);

    @Getter
    private final long id;

    EventList(long id) {
        this.id = id;
    }

}
