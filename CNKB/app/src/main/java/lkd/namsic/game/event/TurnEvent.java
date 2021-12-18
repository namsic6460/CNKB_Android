package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lkd.namsic.game.base.WrappedObject;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.exception.EventRemoveException;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.implement.EntityEvents;
import lkd.namsic.game.object.implement.EquipEvents;

public abstract class TurnEvent implements Event {
    
    @NonNull
    public static String getName() {
        return "TurnEvent";
    }
    
    public static void handleEvent(@NonNull Entity self, @Nullable List<Long> events,
                                   @NonNull Set<Long> eventEquipSet, @NonNull WrappedObject<Entity> attacker) {
        if(events != null) {
            for(long eventId : new ArrayList<>(events)) {
                TurnEvent turnEvent = EntityEvents.getEvent(eventId);
                
                try {
                    turnEvent.onTurn(self, attacker);
                } catch(EventRemoveException e) {
                    if(events.size() == 1) {
                        self.getEvent().remove(getName());
                    } else {
                        events.remove(eventId);
                    }
                }
            }
        }
        
        for(long equipId : eventEquipSet) {
            TurnEvent turnEvent = EquipEvents.getEvent(equipId, getName());
            
            try {
                turnEvent.onTurn(self, attacker);
            } catch(EventRemoveException e) {
                Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
                self.getRemovedEquipEvent(equipment.getEquipType()).add(getName());
            }
        }
    }
    
    public abstract void onTurn(@NonNull Entity self, @NonNull WrappedObject<Entity> attacker);
    
    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }
    
}
