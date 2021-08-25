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
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.implement.EntityEvents;
import lkd.namsic.game.object.implement.EquipEvents;

public abstract class DamagedEvent implements Event {

    @NonNull
    public static String getName() {
        return "DamagedEvent";
    }

    public static void handleEvent(@NonNull Entity self, @Nullable List<Long> events, @NonNull Set<Long> eventEquipSet,
                                   @NonNull Entity attacker, @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
        if (events != null) {
            for (long eventId : new ArrayList<>(events)) {
                DamagedEvent event = EntityEvents.getEvent(eventId);

                try {
                    event.onDamaged(self, attacker, totalDmg, totalDra, isCrit);
                } catch (EventRemoveException e) {
                    if(events.size() == 1) {
                        self.getEvent().remove(getName());
                    } else {
                        events.remove(eventId);
                    }
                }
            }
        }

        for(long equipId : eventEquipSet) {
            DamagedEvent damagedEvent = EquipEvents.getEvent(equipId, getName());

            try {
                damagedEvent.onDamaged(self, attacker, totalDmg, totalDra, isCrit);
            } catch (EventRemoveException e) {
                Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
                self.getRemovedEquipEvent(equipment.getEquipType()).add(getName());
            }
        }
    }

    public abstract void onDamaged(@NonNull Entity self, @NonNull Entity attacker,
                                   @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
