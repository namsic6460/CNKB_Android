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

public abstract class MoveEvent implements Event {

    @NonNull
    public static String getName() {
        return "MoveEvent";
    }

    public static void handleEvent(@NonNull Entity self, @Nullable List<Long> events,
                                   @NonNull Set<Long> eventEquipSet, int distance, boolean isField) {
        if (events != null) {
            for (long eventId : new ArrayList<>(events)) {

                MoveEvent moveEvent = EntityEvents.getEvent(eventId);
                try {
                    moveEvent.onMove(self, distance, isField);
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
            MoveEvent moveEvent = EquipEvents.getEvent(equipId, getName());

            try {
                moveEvent.onMove(self, distance, isField);
            } catch (EventRemoveException e) {
                Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
                self.getRemovedEquipEvent(equipment.getEquipType()).add(getName());
            }
        }
    }

    public abstract void onMove(@NonNull Entity self, int distance, boolean isField);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
