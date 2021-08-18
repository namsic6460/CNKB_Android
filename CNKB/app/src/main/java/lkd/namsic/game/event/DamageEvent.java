package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lkd.namsic.game.base.Bool;
import lkd.namsic.game.base.Int;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.exception.EventRemoveException;
import lkd.namsic.game.exception.EventSkipException;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.implement.EntityEvents;
import lkd.namsic.game.object.implement.EquipEvents;

public abstract class DamageEvent implements Event {

    @NonNull
    public static String getName() {
        return "DamageEvent";
    }

    public static void handleEvent(@NonNull Entity self, @Nullable List<Long> events, @NonNull Set<Long> eventEquipSet,
                                   @NonNull Entity victim, @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit,
                                   boolean canCrit) {
        if (events != null) {
            List<Long> removeList = new ArrayList<>();

            for (long eventId : events) {
                DamageEvent event = EntityEvents.getEvent(eventId);

                try {
                    event.onDamage(self, victim, totalDmg, totalDra, isCrit, canCrit);
                } catch (EventRemoveException e) {
                    removeList.add(eventId);
                } catch (EventSkipException ignore) {}
            }

            events.removeAll(removeList);
        }

        for(long equipId : eventEquipSet) {
            DamageEvent damageEvent = EquipEvents.getEvent(equipId, getName());

            try {
                damageEvent.onDamage(self, victim, totalDmg, totalDra, isCrit, canCrit);
            } catch (EventRemoveException e) {
                Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
                self.getRemovedEquipEvent(equipment.getEquipType()).add(getName());
            } catch (EventSkipException ignore) {}
        }
    }

    public abstract void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                  @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
