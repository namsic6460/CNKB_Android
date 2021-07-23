package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lkd.namsic.game.base.Int;
import lkd.namsic.game.base.LoNg;
import lkd.namsic.game.exception.EventSkipException;
import lkd.namsic.game.gameObject.Entity;

public abstract class MineEvent extends Event {

    private static final long serialVersionUID = 1L;

    @NonNull
    public static String getName() {
        return "MineEvent";
    }

    public static void handleEvent(@NonNull Entity self, @Nullable List<Event> events, @NonNull LoNg itemId, @NonNull Int mineCount) {
        if (events != null) {
            List<Event> removeList = new ArrayList<>();

            for (Event mineEvents : events) {
                try {
                    ((MineEvent) mineEvents).onMine(self, itemId, mineCount);

                    if (mineEvents.activeCount != -1) {
                        if (--mineEvents.activeCount == 0) {
                            removeList.add(mineEvents);
                        }
                    }
                } catch (EventSkipException ignore) {}
            }

            events.removeAll(removeList);
        }
    }

    public MineEvent(int activeCount) {
        this(activeCount, null);
    }

    public MineEvent(int activeCount, @Nullable Map<String, Object> variable) {
        super(activeCount, variable);
    }

    public abstract void onMine(@NonNull Entity self, @NonNull LoNg itemId, @NonNull Int mineCount);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
