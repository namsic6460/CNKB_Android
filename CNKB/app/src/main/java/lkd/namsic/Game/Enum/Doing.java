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
    REINFORCE;

    public static List<Doing> nonFightList() {
        List<Doing> doingList = new ArrayList<>();
        doingList.add(Doing.BUY);
        doingList.add(Doing.CHAT);
        doingList.add(Doing.REINFORCE);

        return doingList;
    }

    public static List<Doing> playerNonFightList() {
        List<Doing> doingList = nonFightList();
        doingList.add(Doing.FIGHT);

        return doingList;
    }

}
