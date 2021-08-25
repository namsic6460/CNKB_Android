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

public abstract class StartFightEvent implements Event {

    @NonNull
    public static String getName() {
        return "StartFightEvent";
    }

    public static void handleEvent(@NonNull Entity self, @Nullable List<Long> events, @NonNull Set<Long> eventEquipSet,
                                   @NonNull Entity enemy, boolean isOwner) {
        if (events != null) {
            for (long eventId : new ArrayList<>(events)) {
                StartFightEvent startFightEvent = EntityEvents.getEvent(eventId);

                try {
                    startFightEvent.onStartFight(self, enemy, isOwner);
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
            StartFightEvent startFightEvent = EquipEvents.getEvent(equipId, getName());

            try {
                startFightEvent.onStartFight(self, enemy, isOwner);
            } catch (EventRemoveException e) {
                Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
                self.getRemovedEquipEvent(equipment.getEquipType()).add(getName());
            }
        }
    }

    public abstract void onStartFight(@NonNull Entity self, @NonNull Entity enemy, boolean isOwner);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
