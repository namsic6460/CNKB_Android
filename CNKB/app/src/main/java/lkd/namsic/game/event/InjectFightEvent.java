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

public abstract class InjectFightEvent implements Event {

    @NonNull
    public static String getName() {
        return "InjectFightEvent";
    }

    public static void handleEvent(@NonNull Entity self, @Nullable List<Long> events,
                                   @NonNull Set<Long> eventEquipSet, @NonNull Entity enemy) {
        if (events != null) {
            for (long eventId : new ArrayList<>(events)) {
                InjectFightEvent injectFightEvent = EntityEvents.getEvent(eventId);

                try {
                    injectFightEvent.onInjectFight(self, enemy);
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
            InjectFightEvent injectFightEvent = EquipEvents.getEvent(equipId, getName());

            try {
                injectFightEvent.onInjectFight(self, enemy);
            } catch (EventRemoveException e) {
                Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
                self.getRemovedEquipEvent(equipment.getEquipType()).add(getName());
            }
        }
    }

    public abstract void onInjectFight(@NonNull Entity self, @NonNull Entity enemy);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
