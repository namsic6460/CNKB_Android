package lkd.namsic.Game.Event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class MoveEvent extends Event {

    public abstract boolean onMove(boolean isCancelled, int distance, boolean isField);

    public static boolean handleEvent(@Nullable List<Event> events, Object[] args) {
        boolean isCancelled = false;

        int distance = Integer.parseInt(args[0].toString());
        boolean isField = Boolean.parseBoolean(args[1].toString());

        if (events != null) {
            List<Event> removeList = new ArrayList<>();
            for (Event moveEvent : events) {
                isCancelled = ((MoveEvent) moveEvent).onMove(isCancelled, distance, isField);

                if (moveEvent.activeCount != -1) {
                    if (--moveEvent.activeCount == 0) {
                        removeList.add(moveEvent);
                    }
                }
            }

            events.removeAll(removeList);
        }

        return isCancelled;
    }

    @NonNull
    public static String getName() {
        return "MoveEvent";
    }

}
