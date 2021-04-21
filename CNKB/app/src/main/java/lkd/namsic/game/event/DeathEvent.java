package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class DeathEvent extends Event {

    public abstract boolean onDeath(boolean isCancelled, int beforeDeathHp, int afterDeathHp);

    public static boolean handleEvent(@Nullable List<Event> events, @NonNull Object[] args) {
        boolean isCancelled = false;

        int beforeDeathHp = Integer.parseInt(args[0].toString());
        int afterDeathHp = Integer.parseInt(args[1].toString());

        if (events != null) {
            List<Event> removeList = new ArrayList<>();
            for (Event deathEvent : events) {
                isCancelled = ((DeathEvent) deathEvent).onDeath(isCancelled, beforeDeathHp, afterDeathHp);

                if (deathEvent.activeCount != -1) {
                    if (--deathEvent.activeCount == 0) {
                        removeList.add(deathEvent);
                    }
                }
            }

            events.removeAll(removeList);
        }

        return isCancelled;
    }

    @NonNull
    public static String getName() {
        return "DeathEvent";
    }

}
