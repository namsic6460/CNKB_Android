package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.util.Map;

import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Base.LimitLong;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Research extends Achieve {

    LimitInteger limitLv = new LimitInteger(Config.MIN_LV, Config.MIN_LV, Config.MAX_LV);
    LimitLong needMoney = new LimitLong(0, 0L, Long.MAX_VALUE);

    Map<Long, Integer> needItem;

    public Research(@NonNull String name, long rewardMoney, int rewardExp, int rewardAdv,
                    @NonNull Map<Long, Integer> rewardCloseRate,
                    @NonNull Map<Long, Integer> rewardItem,
                    @NonNull Map<StatType, Integer> rewardStat,
                    int limitLv, long needMoney, @NonNull Map<Long, Integer> needItem) {
        super(name, rewardMoney, rewardExp, rewardAdv, rewardCloseRate, rewardItem, rewardStat);

        this.limitLv.set(limitLv);
        this.needMoney.set(needMoney);
        this.setNeedItem(needItem);
    }

    public void setNeedItem(Map<Long, Integer> needItem) {
        for(Map.Entry<Long, Integer> entry : needItem.entrySet()) {
            this.setNeedItem(entry.getKey(), entry.getValue());
        }
    }

    public void setNeedItem(long itemId, int count) {
        Config.checkId(Id.ITEM, itemId);

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
        Config.checkId(Id.ITEM, itemId);
        Integer value = this.needItem.get(itemId);

        if(value != null) {
            return value;
        } else {
            return 0;
        }
    }

    public void addNeedItem(long itemId, int count) {
        this.setNeedItem(itemId, this.getNeedItem(itemId) + count);
    }

}
