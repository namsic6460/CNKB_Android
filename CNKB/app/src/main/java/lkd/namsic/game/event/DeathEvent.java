package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.exception.EventRemoveException;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.implement.EntityEvents;
import lkd.namsic.game.object.implement.EquipEvents;

public abstract class DeathEvent implements Event {

    @NonNull
    public static String getName() {
        return "DeathEvent";
    }

    public static void handleEvent(@NonNull Entity self, @Nullable List<Long> events, @NonNull Set<Long> eventEquipSet,
                                   final int beforeDeathHp, final int afterDeathHp) {
        if (events != null) {
            for (long eventId : new ArrayList<>(events)) {
                DeathEvent deathEvent = EntityEvents.getEvent(eventId);

                try {
                    deathEvent.onDeath(self, beforeDeathHp, afterDeathHp);
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
            DeathEvent deathEvent = EquipEvents.getEvent(equipId, getName());

            try {
                deathEvent.onDeath(self, beforeDeathHp, afterDeathHp);
            } catch (EventRemoveException e) {
                Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
                self.getRemovedEquipEvent(equipment.getEquipType()).add(getName());
            }
        }
    }

    public abstract void onDeath(@NonNull Entity self, final int beforeDeathHp, final int afterDeathHp);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
