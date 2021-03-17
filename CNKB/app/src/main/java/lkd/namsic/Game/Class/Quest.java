package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.Game.Base.RangeInteger;
import lkd.namsic.Game.Base.RangeIntegerMap;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.MapSetterException;
import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Quest implements GameObject {

    @Setter
    @NonNull
    String name;

    @Setter
    boolean isRepeatable = false;

    RangeInteger limitLv = new RangeInteger(Config.MIN_LV, Config.MAX_LV);
    RangeIntegerMap<Long> limitCloseRate = new RangeIntegerMap<>(
            new HashMap<Long, Integer>(), new HashMap<Long, Integer>()
    );
    RangeIntegerMap<StatType> limitStat = new RangeIntegerMap<>(
            new HashMap<StatType, Integer>(), new HashMap<StatType, Integer>()
    );

    @Setter
    int needMoney;
    @Setter
    int needExp;
    @Setter
    int needAdv;

    Map<Long, Integer> needItem;
    Map<StatType, Integer> needStat;
    Map<Long, Integer> needCloseRate;

    @Setter
    int rewardMoney;
    @Setter
    int rewardExp;
    @Setter
    int rewardAdv;

    Map<Long, Integer> rewardItem;
    Map<StatType, Integer> rewardStat;
    Map<Long, Integer> rewardCloseRate;

    public Quest(@NonNull String name, boolean isRepeatable) {
        new Quest(name, isRepeatable, Config.MIN_LV, Config.MAX_LV, new HashMap<Long, Integer>(), new HashMap<Long, Integer>(),
                new HashMap<StatType, Integer>(), new HashMap<StatType, Integer>(), 0, 0, 0,
                new HashMap<Long, Integer>(), new HashMap<StatType, Integer>(), new HashMap<Long, Integer>(), 0,
                0, 0, new HashMap<Long, Integer>(), new HashMap<StatType, Integer>(), new HashMap<Long, Integer>());
    }

    public Quest(@NonNull String name, boolean isRepeatable, int minLimitLv, int maxLimitLv,
                 @NonNull Map<Long, Integer> minLimitCloseRate, @NonNull Map<Long, Integer> maxLimitCloseRate,
                 @NonNull Map<StatType, Integer> minLimitStat, @NonNull Map<StatType, Integer> maxLimitStat,
                 int needMoney, int needExp, int needAdv, @NonNull Map<Long, Integer> needItem,
                 @NonNull Map<StatType, Integer> needStat, @NonNull Map<Long, Integer> needCloseRate,
                 int rewardMoney, int rewardExp, int rewardAdv, @NonNull Map<Long, Integer> rewardItem,
                 @NonNull Map<StatType, Integer> rewardStat, @NonNull Map<Long, Integer> rewardCloseRate) {
        this.id.setId(Id.QUEST);

        this.name = name;
        this.isRepeatable = isRepeatable;

        this.limitLv.set(minLimitLv, maxLimitLv);
        this.limitCloseRate.set(minLimitCloseRate, maxLimitCloseRate);
        this.limitStat.set(minLimitStat, maxLimitStat);

        this.needMoney = needMoney;
        this.needExp = needExp;
        this.needAdv = needAdv;
        this.setNeedItem(needItem);
        this.setNeedStat(needStat);
        this.setNeedCloseRate(needCloseRate);

        this.rewardMoney = rewardMoney;
        this.rewardExp = rewardExp;
        this.rewardAdv = rewardAdv;
        this.setRewardItem(rewardItem);
        this.setRewardStat(rewardStat);
        this.setRewardCloseRate(rewardCloseRate);
    }

    public void setNeedItem(Map<Long, Integer> needItem) {
        Map<Long, Integer> copy = new HashMap<>(this.needItem);

        try {
            for(Map.Entry<Long, Integer> entry : needItem.entrySet()) {
                this.setNeedItem(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            this.needItem = copy;
            throw new MapSetterException(copy, needItem, e);
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
        Integer value = this.needItem.get(itemId);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void setNeedStat(Map<StatType, Integer> needStat) {
        Map<StatType, Integer> copy = new HashMap<>(this.needStat);

        try {
            for(Map.Entry<StatType, Integer> entry : needStat.entrySet()) {
                this.setNeedStat(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            this.needStat = copy;
            throw new MapSetterException(copy, needStat, e);
        }
    }

    public void setNeedStat(StatType statType, int stat) {
        if(stat < 0) {
            throw new NumberRangeException(stat, 0);
        }

        if(stat == 0) {
            this.needStat.remove(statType);
        } else {
            this.needStat.put(statType, stat);
        }
    }

    public int getNeedStat(StatType statType) {
        Integer value = this.needStat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void setNeedCloseRate(Map<Long, Integer> needCloseRate) {
        Map<Long, Integer> copy = new HashMap<>(this.needCloseRate);

        try {
            for(Map.Entry<Long, Integer> entry : needCloseRate.entrySet()) {
                this.setNeedCloseRate(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            this.needCloseRate = copy;
            throw new MapSetterException(copy, needCloseRate, e);
        }
    }

    public void setNeedCloseRate(long npcId, int closeRate) {
        Config.checkId(Id.NPC, npcId);

        if(closeRate < 0) {
            throw new NumberRangeException(closeRate, 0);
        }

        if(closeRate == 0) {
            this.needCloseRate.remove(npcId);
        } else {
            this.needCloseRate.put(npcId, closeRate);
        }
    }

    public int getNeedCloseRate(long npcId) {
        Integer value = this.needCloseRate.get(npcId);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void setRewardItem(Map<Long, Integer> rewardItem) {
        Map<Long, Integer> copy = new HashMap<>(this.rewardItem);

        try {
            for(Map.Entry<Long, Integer> entry : rewardItem.entrySet()) {
                this.setRewardItem(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            this.rewardItem = copy;
            throw new MapSetterException(copy, rewardItem, e);
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
        Integer value = this.rewardItem.get(itemId);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void setRewardCloseRate(Map<Long, Integer> rewardCloseRate) {
        Map<Long, Integer> copy = new HashMap<>(this.rewardCloseRate);

        try {
            for(Map.Entry<Long, Integer> entry : rewardCloseRate.entrySet()) {
                this.setRewardCloseRate(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            this.rewardCloseRate = copy;
            throw new MapSetterException(copy, rewardCloseRate, e);
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
        Integer value = this.rewardCloseRate.get(npcId);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void setRewardStat(Map<StatType, Integer> rewardStat) {
        Map<StatType, Integer> copy = new HashMap<>(this.rewardStat);

        try {
            for(Map.Entry<StatType, Integer> entry : rewardStat.entrySet()) {
                this.setRewardStat(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            this.rewardStat = copy;
            throw new MapSetterException(copy, rewardStat, e);
        }
    }

    public void setRewardStat(StatType statType, int stat) {
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

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

}
