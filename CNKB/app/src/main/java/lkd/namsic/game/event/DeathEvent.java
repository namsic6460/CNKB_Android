package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lkd.namsic.game.exception.EventSkipException;
import lkd.namsic.game.gameObject.Entity;

public abstract class DeathEvent extends Event {

    public static boolean handleEvent(@NonNull Entity self, @Nullable List<Event> events, int beforeDeathHp, int afterDeathHp) {
        boolean isCancelled = false;

        if (events != null) {
            List<Event> removeList = new ArrayList<>();

            for (Event deathEvent : events) {
                try {
                    isCancelled = ((DeathEvent) deathEvent).onDeath(self, beforeDeathHp, afterDeathHp);

                    if (deathEvent.activeCount != -1) {
                        if (--deathEvent.activeCount == 0) {
                            removeList.add(deathEvent);
                        }
                    }
                } catch (EventSkipException ignore) {}
            }

            events.removeAll(removeList);
        }

        return isCancelled;
    }

    public abstract boolean onDeath(@NonNull Entity self, int beforeDeathHp, int afterDeathHp);

    @NonNull
    public static String getName() {
        return "DeathEvent";
    }

}
