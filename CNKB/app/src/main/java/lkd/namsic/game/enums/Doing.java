package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public enum Doing {

    NONE,
    BUY,
    CHAT,
    FIGHT,
    FIGHT_ONE,
    FISH,
    EXPLORE,
    MINE,
    GATHER,
    HARVEST,
    REINFORCE,
    WAIT_RESPONSE;

    @NonNull
    public static List<Doing> fightList() {
        List<Doing> doingList = new ArrayList<>();
        doingList.add(Doing.NONE);
        doingList.add(Doing.EXPLORE);
        doingList.add(Doing.FIGHT);

        return doingList;
    }

}
