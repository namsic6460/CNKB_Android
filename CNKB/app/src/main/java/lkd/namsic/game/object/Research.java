package lkd.namsic.game.object;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.LimitLong;
import lkd.namsic.game.base.RangeInteger;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;

@Getter
public class Research extends Achieve {

    final LimitInteger limitLv = new LimitInteger(Config.MIN_LV, Config.MIN_LV, Config.MAX_LV);
    final LimitLong needMoney = new LimitLong(0, 0L, Long.MAX_VALUE);

    final Map<Long, Integer> needItem = new HashMap<>();

    public Research(@NonNull String name) {
        super(name);
        this.id.setId(Id.RESEARCH);
    }

    public void setNeedItem(Map<Long, Integer> needItem) {
        for(Map.Entry<Long, Integer> entry : needItem.entrySet()) {
            this.setNeedItem(entry.getKey(), entry.getValue());
        }
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

    public void addNeedItem(long itemId, int count) {
        this.setNeedItem(itemId, this.getNeedItem(itemId) + count);
    }

}
