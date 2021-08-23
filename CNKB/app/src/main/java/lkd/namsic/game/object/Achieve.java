package lkd.namsic.game.object;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Achieve extends NamedObject {

    @Setter
    long rewardMoney = 0;

    @Setter
    long rewardExp = 0;

    @Setter
    int rewardAdv = 0;

    final Map<Long, Integer> rewardCloseRate = new HashMap<>();
    final Map<Long, Integer> rewardItem = new HashMap<>();
    final Map<StatType, Integer> rewardStat = new HashMap<>();

    public Achieve(@NonNull String name) {
        super(name);
        this.id.setId(Id.ACHIEVE);
    }

    public void setRewardCloseRate(long npcId, int closeRate) {
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
        return this.rewardCloseRate.getOrDefault(npcId, 0);
    }

    public void addRewardCloseRate(long npcId, int closeRate) {
        this.setRewardCloseRate(npcId, this.getRewardCloseRate(npcId) + closeRate);
    }

    public void setRewardItem(long itemId, int count) {
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
        return this.rewardItem.getOrDefault(itemId, 0);
    }

    public void addRewardItem(long npcId, int closeRate) {
        this.setRewardItem(npcId, this.getRewardItem(npcId) + closeRate);
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
