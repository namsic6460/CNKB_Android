package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lkd.namsic.game.base.LoNg;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.exception.EventRemoveException;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.implement.EntityEvents;
import lkd.namsic.game.object.implement.EquipEvents;

public abstract class MoneyChangeEvent implements Event {
    
    @NonNull
    public static String getName() {
        return "MoneyChangeEvent";
    }
    
    public static void handleEvent(@NonNull Entity self, @Nullable List<Long> events,
                                   @NonNull Set<Long> eventEquipSet, @NonNull LoNg money) {
        if(events != null) {
            for(long eventId : new ArrayList<>(events)) {
                MoneyChangeEvent moneyChangeEvent = EntityEvents.getEvent(eventId);
                
                try {
                    moneyChangeEvent.onChange(self, money);
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
            MoneyChangeEvent moneyChangeEvent = EquipEvents.getEvent(equipId, getName());
            
            try {
                moneyChangeEvent.onChange(self, money);
            } catch(EventRemoveException e) {
                Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
                self.getRemovedEquipEvent(equipment.getEquipType()).add(getName());
            }
        }
    }
    
    public abstract void onChange(@NonNull Entity self, @NonNull LoNg money);
    
    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }
    
}
