package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.Game.Base.LimitId;
import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Base.LimitLong;
import lkd.namsic.Game.Base.RangeInteger;
import lkd.namsic.Game.Base.RangeIntegerMap;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Quest extends NamedObject {

    @Setter
    boolean isRepeatable = false;

    RangeInteger limitLv = new RangeInteger(Config.MIN_LV, Config.MAX_LV);
    RangeIntegerMap<Long> limitCloseRate = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>()
    );
    RangeIntegerMap<StatType> limitStat = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>()
    );

    LimitLong needMoney = new LimitLong(0, null, Long.MAX_VALUE);
    LimitInteger needAdv = new LimitInteger(0, null, Integer.MAX_VALUE);

    Map<Long, Integer> needItem = new HashMap<>();
    Map<StatType, Integer> needStat = new HashMap<>();
    Map<Long, Integer> needCloseRate = new HashMap<>();

    LimitLong rewardMoney = new LimitLong(0, null, Long.MAX_VALUE);
    LimitInteger rewardExp = new LimitInteger(0, null, Integer.MAX_VALUE);
    LimitInteger rewardAdv = new LimitInteger(0, null, Integer.MAX_VALUE);

    Map<Long, Integer> rewardItem = new HashMap<>();
    Map<StatType, Integer> rewardStat = new HashMap<>();
    Map<Long, Integer> rewardCloseRate = new HashMap<>();

    LimitId npcId = new LimitId(0, Id.NPC);
    LimitId chatId = new LimitId(0, Id.QUEST);

    public Quest(@NonNull String name, long chatId) {
        super(name);
        this.id.setId(Id.QUEST);

        this.setChatId(chatId);
    }

    public void setChatId(long chatId) {
        Config.checkId(Id.CHAT, chatId);
        this.chatId.set(chatId);
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
        Integer value = this.needItem.get(itemId);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void setNeedStat(Map<StatType, Integer> needStat) {
        for(Map.Entry<StatType, Integer> entry : needStat.entrySet()) {
            this.setNeedStat(entry.getKey(), entry.getValue());
        }
    }

    public void setNeedStat(StatType statType, int stat) {
        Config.checkStatType(statType);

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
        Config.checkStatType(statType);
        Integer value = this.needStat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void setNeedCloseRate(Map<Long, Integer> needCloseRate) {
        for(Map.Entry<Long, Integer> entry : needCloseRate.entrySet()) {
            this.setNeedCloseRate(entry.getKey(), entry.getValue());
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
        Integer value = this.rewardItem.get(itemId);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
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
        Config.checkStatType(statType);
        Integer value = this.rewardStat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
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
        Integer value = this.rewardCloseRate.get(npcId);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

}
