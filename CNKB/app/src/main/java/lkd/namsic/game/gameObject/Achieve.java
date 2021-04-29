package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.LimitLong;
import lkd.namsic.game.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Achieve extends NamedObject {

    final LimitLong rewardMoney = new LimitLong(0, 0L, Long.MAX_VALUE);
    final LimitInteger rewardExp = new LimitInteger(0, 0, Integer.MAX_VALUE);
    final LimitInteger rewardAdv = new LimitInteger(0, 0, Integer.MAX_VALUE);

    final Map<Long, Integer> rewardCloseRate = new HashMap<>();
    final Map<Long, Integer> rewardItem = new HashMap<>();
    final Map<StatType, Integer> rewardStat = new HashMap<>();

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
        return this.rewardCloseRate.getOrDefault(npcId, 0);
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

    public int getRewardItem(long itemId) {
        Config.checkId(Id.ITEM, itemId);
        return this.rewardItem.getOrDefault(itemId, 0);
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
        return this.rewardStat.getOrDefault(statType, 0);
    }

    public void addRewardStat(StatType rewardStat, int stat) {
        this.setRewardStat(rewardStat, this.getRewardStat(rewardStat) + stat);
    }

}
