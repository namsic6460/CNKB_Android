package lkd.namsic.game.object;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.game.enums.Id;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Research extends Achieve {
    
    @Setter
    int limitLv = 1;
    
    @Setter
    long needMoney = 0;
    
    final Map<Long, Integer> needItem = new HashMap<>();
    
    public Research(@NonNull String name) {
        super(name);
        this.id.setId(Id.RESEARCH);
    }
    
    public void setNeedItem(long itemId, int count) {
        if(count < 0) {
            throw new NumberRangeException(count, 0);
        }
        
        if(count == 0) {
            this.needItem.remove(itemId);
        } else {
            this.needItem.put(itemId, count);
        }
    }
    
    public int getNeedItem(long itemId) {
        return this.needItem.getOrDefault(itemId, 0);
    }
    
}
