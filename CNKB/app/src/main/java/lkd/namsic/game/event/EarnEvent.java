package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class EarnEvent extends Event {

    public abstract boolean onEarn(boolean isCancelled, int money);

    public static boolean handleEvent(@Nullable List<Event> events, Object[] args) {
        boolean isCancelled = false;

        int money = Integer.parseInt(args[0].toString());

        if (events != null) {
            List<Event> removeList = new ArrayList<>();
            for (Event earnEvent : events) {
                isCancelled = ((EarnEvent) earnEvent).onEarn(isCancelled, money);

                if (earnEvent.activeCount != -1) {
                    if (--earnEvent.activeCount == 0) {
                        removeList.add(earnEvent);
                    }
                }
            }

            events.removeAll(removeList);
        }

        return isCancelled;
    }

    @NonNull
    public static String getName() {
        return "EarnEvent";
    }

}
