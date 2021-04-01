package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Base.LimitLong;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Achieve extends NamedObject {

    LimitLong rewardMoney = new LimitLong(0, 0L, Long.MAX_VALUE);
    LimitInteger rewardExp = new LimitInteger(0, 0, Integer.MAX_VALUE);
    LimitInteger rewardAdv = new LimitInteger(0, 0, Integer.MAX_VALUE);

    Map<Long, Integer> rewardCloseRate = new HashMap<>();
    Map<Long, Integer> rewardItem = new HashMap<>();
    Map<StatType, Integer> rewardStat = new HashMap<>();

    public Achieve(@NonNull String name) {
        super(name);
        this.id.setId(Id.ACHIEVE);
    }

    public void setRewardCloseRate(Map<Long, Integer> rewardCloseRate) {
        for(Map.Entry<Long, Integer> entry : rewardCloseRate.entrySet()) {
            this.setRewardCloseRate(entry.getKey(), entry.getValue());
        }
    }

    public void setRewardCloseRate(long npcId, int closeRate) {
        Config.checkId(Id.NPC, npcId);

        if(closeRate < 0) {
            throw new NumberRangeException(closeRate, 0);
        }

        if(closeRate == 0) {
            this.rewardCloseRate.remove(npcId);
        } else {
            this.rewardCloseRate.put(npcId, closeRate);
        }
    }

    public int getRewardCloseRate(long npcId) {
        Config.checkId(Id.NPC, npcId);
        Integer value = this.rewardCloseRate.get(npcId);

        if(value != null) {
            return value;
        } else {
            return 0;
        }
    }

    public void addRewardCloseRate(long npcId, int closeRate) {
        this.setRewardCloseRate(npcId, this.getRewardCloseRate(npcId) + closeRate);
    }

    public void setRewardItem(Map<Long, Integer> rewardItem) {
        for(Map.Entry<Long, Integer> entry : rewardItem.entrySet()) {
            this.setRewardItem(entry.getKey(), entry.getValue());
        }
    }

    public void setRewardItem(long itemId, int count) {
        Config.checkId(Id.ITEM, itemId);

        if(count < 0) {
            throw new NumberRangeException(count, 0);
        }

        if(count == 0) {
            this.rewardItem.remove(itemId);
        } else {
            this.rewardItem.put(itemId, count);
        }
    }

    public int getRewardItem(long npcId) {
        Config.checkId(Id.NPC, npcId);
        Integer value = this.rewardItem.get(npcId);

        if(value != null) {
            return value;
        } else {
            return 0;
        }
    }

    public void addRewardItem(long npcId, int closeRate) {
        this.setRewardItem(npcId, this.getRewardItem(npcId) + closeRate);
    }

    public void setRewardStat(Map<StatType, Integer> rewardStat) {
        for(Map.Entry<StatType, Integer> entry : rewardStat.entrySet()) {
            this.setRewardStat(entry.getKey(), entry.getValue());
        }
    }

    public void setRewardStat(StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat < 0) {
            throw new NumberRangeException(stat, 0);
        }

        if(stat == 0) {
            this.rewardStat.remove(statType);
        } else {
            this.rewardStat.put(statType, stat);
        }
    }

    public int getRewardStat(StatType statType) {
        Integer value = this.rewardStat.get(statType);

        if(value != null) {
            return value;
        } else {
            return 0;
        }
    }

    public void addRewardStat(StatType rewardStat, int stat) {
        this.setRewardStat(rewardStat, this.getRewardStat(rewardStat) + stat);
    }

}
