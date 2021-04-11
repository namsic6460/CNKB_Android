package lkd.namsic.Game.Enum;

import java.util.ArrayList;
import java.util.List;

public enum Doing {

    NONE,
    BUY,
    CHAT,
    FIGHT,
    EXPLORE,
    MINE,
    GATHER,
    HARVEST,
    REINFORCE,
    WAIT_RESPONSE;

    public static List<Doing> fightList() {
        List<Doing> doingList = new ArrayList<>();
        doingList.add(Doing.NONE);
        doingList.add(Doing.EXPLORE);
        doingList.add(Doing.MINE);
        doingList.add(Doing.GATHER);
        doingList.add(Doing.HARVEST);

        return doingList;
    }

}
