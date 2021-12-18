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
import lkd.namsic.game.object.implement.EntityEvents;
import lkd.namsic.game.object.implement.EquipEvents;

public abstract class PreDamageEvent implements Event {
    
    @NonNull
    public static String getName() {
        return "PreDamageEvent";
    }
    
    public static void handleEvent(@NonNull Entity self, @Nullable List<Long> events, @NonNull Set<Long> eventEquipSet,
                                   @NonNull Entity victim, @NonNull Int physicDmg, @NonNull Int magicDmg, @NonNull Int staticDmg,
                                   boolean canCrit) {
        if(events != null) {
            for(long eventId : new ArrayList<>(events)) {
                PreDamageEvent preDamageEvent = EntityEvents.getEvent(eventId);
                
                try {
                    preDamageEvent.onPreDamage(self, victim, physicDmg, magicDmg, staticDmg, canCrit);
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
            PreDamageEvent preDamageEvent = EquipEvents.getEvent(equipId, getName());
            
            try {
                preDamageEvent.onPreDamage(self, victim, physicDmg, magicDmg, staticDmg, canCrit);
            } catch(EventRemoveException e) {
                Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
                self.getRemovedEquipEvent(equipment.getEquipType()).add(getName());
            }
        }
    }
    
    public abstract void onPreDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int physicDmg,
                                     @NonNull Int magicDmg, @NonNull Int staticDmg, boolean canCrit);
    
    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }
    
}
