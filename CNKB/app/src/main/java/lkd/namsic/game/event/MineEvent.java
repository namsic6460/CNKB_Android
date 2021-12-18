package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lkd.namsic.game.base.Int;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.exception.EventRemoveException;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.implement.EntityEvents;
import lkd.namsic.game.object.implement.EquipEvents;

public abstract class MineEvent implements Event {
    
    @NonNull
    public static String getName() {
        return "MineEvent";
    }
    
    public static void handleEvent(@NonNull Entity self, @Nullable List<Long> events,
                                   @NonNull Set<Long> eventEquipSet, long itemId, @NonNull Int itemCount) {
        Item item = Config.getData(Id.ITEM, itemId);
        
        if(events != null) {
            for(long eventId : new ArrayList<>(events)) {
                MineEvent mineEvent = EntityEvents.getEvent(eventId);
                
                try {
                    mineEvent.onMine(self, item, itemCount);
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
            MineEvent mineEvent = EquipEvents.getEvent(equipId, getName());
            
            try {
                mineEvent.onMine(self, item, itemCount);
            } catch(EventRemoveException e) {
                Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
                self.getRemovedEquipEvent(equipment.getEquipType()).add(getName());
            }
        }
    }
    
    public abstract void onMine(@NonNull Entity self, @NonNull Item item, @NonNull Int itemCount);
    
    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }
    
}
