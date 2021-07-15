package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lkd.namsic.game.exception.EventSkipException;
import lkd.namsic.game.gameObject.Entity;

public abstract class MoveEvent extends Event {

    @NonNull
    public static String getName() {
        return "MoveEvent";
    }

    public static boolean handleEvent(@NonNull Entity self, @Nullable List<Event> events, int distance, boolean isField) {
        boolean isCancelled = false;

        if (events != null) {
            List<Event> removeList = new ArrayList<>();

            for (Event moveEvent : events) {
                try {
                    isCancelled = ((MoveEvent) moveEvent).onMove(self, distance, isField);

                    if (moveEvent.activeCount != -1) {
                        if (--moveEvent.activeCount == 0) {
                            removeList.add(moveEvent);
                        }
                    }
                } catch (EventSkipException ignore) {}
            }

            events.removeAll(removeList);
        }

        return isCancelled;
    }

    public MoveEvent(int activeCount) {
        this(activeCount, null);
    }

    public MoveEvent(int activeCount, @Nullable Map<String, Object> variable) {
        super(activeCount, variable);
    }

    public abstract boolean onMove(@NonNull Entity self, int distance, boolean isField);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
