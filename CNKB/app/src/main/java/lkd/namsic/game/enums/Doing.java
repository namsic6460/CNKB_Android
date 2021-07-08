package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public enum Doing {

    NONE,
    APPRAISE,
    BUY,
    CHAT,
    FIGHT,
    FIGHT_ONE,
    FISH,
    ADVENTURE,
    MINE,
    GATHER,
    HARVEST,
    REINFORCE,
    WAIT_RESPONSE;

    @NonNull
    public static List<Doing> fightableList() {
        List<Doing> doingList = new ArrayList<>(3);
        doingList.add(Doing.NONE);
        doingList.add(Doing.ADVENTURE);
        doingList.add(Doing.FIGHT);

        return doingList;
    }

    @NonNull
    public static List<Doing> fightList() {
        List<Doing> doingList = new ArrayList<>(2);
        doingList.add(Doing.FIGHT);
        doingList.add(Doing.FIGHT_ONE);

        return doingList;
    }

}
