package lkd.namsic.Game.Enum;

import java.util.ArrayList;
import java.util.List;

public enum Doing {

    IDLE,
    MOVE,
    BUY,
    CHAT,
    FIGHT,
    EXPLORE,
    MINE,
    GATHER,
    HARVEST,
    REINFORCE;

    public static List<Doing> nonFightDoing() {
        List<Doing> doingList = new ArrayList<>();
        doingList.add(Doing.BUY);
        doingList.add(Doing.CHAT);
        doingList.add(Doing.REINFORCE);

        return doingList;
    }

}
