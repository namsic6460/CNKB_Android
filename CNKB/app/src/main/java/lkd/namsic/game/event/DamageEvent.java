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

public abstract class DamageEvent extends Event {

    @NonNull
    public static String getName() {
        return "DamageEvent";
    }

    public static boolean handleEvent(@NonNull Entity self, @Nullable List<Event> events, @NonNull Entity victim,
                                      @NonNull Int totalDmg, @NonNull Int totalDra, Bool isCrit) {
        boolean isCancelled = false;

        if (events != null) {
            List<Event> removeList = new ArrayList<>();

            for (Event damageEvent : events) {
                try {
                    isCancelled = ((DamageEvent) damageEvent)
                            .onDamage(self, victim, totalDmg, totalDra, isCrit);

                    if (damageEvent.activeCount != -1) {
                        if (--damageEvent.activeCount == 0) {
                            removeList.add(damageEvent);
                        }
                    }
                } catch (EventSkipException ignore) {}
            }

            events.removeAll(removeList);
        }

        return isCancelled;
    }

    public DamageEvent(int activeCount) {
        this(activeCount, null);
    }

    public DamageEvent(int activeCount, @Nullable Map<String, Object> variable) {
        super(activeCount, variable);
    }

    public abstract boolean onDamage(@NonNull Entity self, @NonNull Entity victim, Int totalDmg, Int totalDra, Bool isCrit);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
