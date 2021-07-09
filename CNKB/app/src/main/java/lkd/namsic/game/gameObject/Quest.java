package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lkd.namsic.game.base.LimitId;
import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.LimitLong;
import lkd.namsic.game.base.RangeInteger;
import lkd.namsic.game.base.RangeIntegerMap;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object_list.QuestList;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;

@Getter
public class Quest extends NamedObject {

    final RangeInteger limitLv = new RangeInteger(Config.MIN_LV, Config.MAX_LV);
    final RangeIntegerMap<Long> limitCloseRate = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>(), Long.class
    );
    final RangeIntegerMap<StatType> limitStat = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>(), StatType.class
    );

    final LimitLong needMoney = new LimitLong(0, 0L, null);
    final Map<Long, Integer> needItem = new HashMap<>();
    final Map<StatType, Integer> needStat = new HashMap<>();
    final Map<Long, Integer> needCloseRate = new HashMap<>();

    final LimitLong rewardMoney = new LimitLong(0, 0L, null);
    final LimitLong rewardExp = new LimitLong(0, 0L, null);
    final LimitInteger rewardAdv = new LimitInteger(0, 0, null);
    final Map<Long, Integer> rewardItem = new HashMap<>();
    final Map<StatType, Integer> rewardStat = new HashMap<>();
    final Map<Long, Integer> rewardCloseRate = new HashMap<>();

    final LimitId npcId = new LimitId(0, Id.NPC);
    final LimitId chatId = new LimitId(0, Id.CHAT);

    public Quest(@NonNull String name, long npcId, long chatId) {
        super(name);
        this.id.setId(Id.QUEST);
        this.id.setObjectId(Objects.requireNonNull(QuestList.findByName(name)));

        this.setNpcId(npcId);
        this.setChatId(chatId);
    }

    //Only use in constructor
    private void setNpcId(long npcId) {
        if(npcId < 1) {
            throw new NumberRangeException(npcId, 0L);
        }

        //Since npc creating is after creating quests
        this.npcId.set(npcId);
    }

    public void setChatId(long chatId) {
        Config.checkId(Id.CHAT, chatId);
        this.chatId.set(chatId);
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
        return this.needItem.getOrDefault(itemId, 0);
    }

    public void addNeedItem(long itemId, int count) {
        this.setNeedItem(itemId, this.getNeedItem(itemId) + count);
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
        return this.needStat.getOrDefault(statType, 0);
    }

    public void addNeedStat(StatType statType, int stat) {
        this.setNeedStat(statType, this.getNeedStat(statType) + stat);
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
        Config.checkId(Id.NPC, npcId);
        return this.needCloseRate.getOrDefault(npcId, 0);
    }

    public void addNeedCloseRate(long npcId, int closeRate) {
        this.setNeedCloseRate(npcId, this.getNeedCloseRate(npcId) + closeRate);
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
        return this.rewardItem.getOrDefault(itemId, 0);
    }

    public void addRewardItem(long itemId, int count) {
        this.setRewardItem(itemId, this.getRewardItem(itemId) + count);
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
        return this.rewardStat.getOrDefault(statType, 0);
    }

    public void addRewardStat(StatType statType, int stat) {
        this.setRewardStat(statType, this.getRewardStat(statType) + stat);
    }

    public void setRewardCloseRate(long npcId, int closeRate) {
        this.setRewardCloseRate(npcId, closeRate, false);
    }

    public void setRewardCloseRate(long npcId, int closeRate, boolean skip) {
        if(!skip) {
            Config.checkId(Id.NPC, npcId);
        }

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

}
