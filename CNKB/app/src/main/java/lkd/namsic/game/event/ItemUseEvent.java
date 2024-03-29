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
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.implement.EntityEvents;
import lkd.namsic.game.object.implement.EquipEvents;

public abstract class ItemUseEvent implements Event {
    
    @NonNull
    public static String getName() {
        return "ItemUseEvent";
    }
    
    public static void handleEvent(@NonNull Entity self, @Nullable List<Long> events, @NonNull Set<Long> eventEquipSet,
                                   long itemId, @Nullable String other, int count) {
        Item item = Config.getData(Id.ITEM, itemId);
        
        if(events != null) {
            for(long eventId : new ArrayList<>(events)) {
                ItemUseEvent itemUseEvent = EntityEvents.getEvent(eventId);
                
                try {
                    itemUseEvent.onUse(self, item, other, count);
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
            ItemUseEvent itemUseEvent = EquipEvents.getEvent(equipId, getName());
            
            try {
                itemUseEvent.onUse(self, item, other, count);
            } catch(EventRemoveException e) {
                Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
                self.getRemovedEquipEvent(equipment.getEquipType()).add(getName());
            }
        }
    }
    
    public abstract void onUse(@NonNull Entity self, @NonNull Item item, @Nullable String other, int count);
    
    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }
    
}
