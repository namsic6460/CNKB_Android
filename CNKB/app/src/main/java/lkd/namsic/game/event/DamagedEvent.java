package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lkd.namsic.game.base.Bool;
import lkd.namsic.game.base.Int;
import lkd.namsic.game.exception.EventSkipException;
import lkd.namsic.game.gameObject.Entity;

public abstract class DamagedEvent extends Event {

    private static final long serialVersionUID = 1L;

    @NonNull
    public static String getName() {
        return "DamagedEvent";
    }

    public static void handleEvent(@NonNull Entity self, @Nullable List<Event> events, @NonNull Entity attacker,
                                      @NonNull Int totalDmg, @NonNull Int totalDra, Bool isCrit) {
        if (events != null) {
            List<Event> removeList = new ArrayList<>();

            for (Event damagedEvent : events) {
                try {
                    ((DamagedEvent) damagedEvent).onDamaged(self, attacker, totalDmg, totalDra, isCrit);

                    if (damagedEvent.activeCount != -1) {
                        if (--damagedEvent.activeCount == 0) {
                            removeList.add(damagedEvent);
                        }
                    }
                } catch (EventSkipException ignore) {}
            }

            events.removeAll(removeList);
        }
    }

    public DamagedEvent(int activeCount) {
        this(activeCount, null);
    }

    public DamagedEvent(int activeCount, @Nullable Map<String, Object> variable) {
        super(activeCount, variable);
    }

    public abstract void onDamaged(@NonNull Entity self, @NonNull Entity attacker, Int totalDmg, Int totalDra, Bool isCrit);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
