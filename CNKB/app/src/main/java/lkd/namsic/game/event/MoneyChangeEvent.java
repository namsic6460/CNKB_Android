package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lkd.namsic.game.exception.EventSkipException;
import lkd.namsic.game.gameObject.Entity;

public abstract class MoneyChangeEvent extends Event {

    @NonNull
    public static String getName() {
        return "MoneyChangeEvent";
    }

    public static boolean handleEvent(@NonNull Entity self, @Nullable List<Event> events, long money) {
        boolean isCancelled = false;

        if (events != null) {
            List<Event> removeList = new ArrayList<>();

            for (Event earnEvent : events) {
                try {
                    isCancelled = ((MoneyChangeEvent) earnEvent).onMoneyChange(self, money);

                    if (earnEvent.activeCount != -1) {
                        if (--earnEvent.activeCount == 0) {
                            removeList.add(earnEvent);
                        }
                    }
                } catch (EventSkipException ignore) {}
            }

            events.removeAll(removeList);
        }

        return isCancelled;
    }

    public MoneyChangeEvent(int activeCount) {
        this(activeCount, null);
    }

    public MoneyChangeEvent(int activeCount, @Nullable Map<String, Object> variable) {
        super(activeCount, variable);
    }

    public abstract boolean onMoneyChange(@NonNull Entity self, long money);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
