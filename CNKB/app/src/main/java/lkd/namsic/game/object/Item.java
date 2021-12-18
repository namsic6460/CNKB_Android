package lkd.namsic.game.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.base.Use;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.object.implement.ItemUses;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Item extends NamedObject {
    
    @Setter
    @NonNull
    String description;
    
    @Setter
    private boolean canEat = false;
    
    private final Map<Long, HashMap<StatType, Integer>> eatBuff = new HashMap<>();
    
    final Set<Map<Long, Integer>> recipe = new LinkedHashSet<>();
    
    protected Item(@NonNull String name) {
        super(name);
    }
    
    public Item(@NonNull ItemList itemData, @NonNull String description) {
        super(itemData.getDisplayName());
        this.id.setId(Id.ITEM);
        this.id.setObjectId(itemData.getId());
        
        this.description = description;
    }
    
    @Nullable
    public Use getUse() {
        return ItemUses.MAP.get(this.id.getObjectId());
    }
    
    public void setEatBuff(long time, @NonNull StatType statType, int stat) {
        if(time != -1) {
            Config.checkStatType(statType);
            
            if(time < 1) {
                throw new NumberRangeException(time, 1, Long.MAX_VALUE);
            }
        }
        
        HashMap<StatType, Integer> buff = this.eatBuff.get(time);
        
        if(buff == null) {
            if(stat == 0) {
                return;
            }
            
            buff = new HashMap<>();
            buff.put(statType, stat);
            this.eatBuff.put(time, buff);
        } else {
            if(stat == 0) {
                if(buff.size() == 1) {
                    this.eatBuff.remove(time);
                } else {
                    buff.remove(statType);
                }
            } else {
                buff.put(statType, stat);
            }
        }
    }
    
    public void addRecipe(@NonNull Map<Long, Integer> recipe) {
        for(int itemCount : recipe.values()) {
            if(itemCount < 1) {
                throw new NumberRangeException(itemCount, 1, Integer.MAX_VALUE);
            }
        }
        
        this.recipe.add(recipe);
    }
    
}
