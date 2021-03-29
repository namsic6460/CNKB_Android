package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

import lkd.namsic.Game.Base.LimitId;
import lkd.namsic.Game.Base.LimitLong;
import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Enum.WaitResponse;
import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Chat implements GameObject {

    LimitLong pauseTime = new LimitLong(0, 0L, Long.MAX_VALUE);

    LimitId questId = new LimitId(0, Id.QUEST);

    Location tpLocation;

    List<String> text;

    Map<WaitResponse, Long> responseChat;
    Map<String, Long> anyResponseChat;

    LimitLong needMoney = new LimitLong(0, 0L, Long.MAX_VALUE);
    Map<StatType, Integer> needStat;
    Map<Long, Integer> needItem;

    LimitLong rewardMoney = new LimitLong(0, 0L, Long.MAX_VALUE);
    Map<StatType, Integer> rewardStat;
    Map<Long, Integer> rewardItem;

    public Chat(long pauseTime, long questId, Location tpLocation, List<String> text,
                Map<WaitResponse, Long> responseChat, Map<String, Long> anyResponseChat,
                long needMoney, Map<StatType, Integer> needStat, Map<Long, Integer> needItem,
                long rewardMoney, Map<StatType, Integer> rewardStat, Map<Long, Integer> rewardItem) {
        this.id.setId(Id.CHAT);

        this.pauseTime.set(pauseTime);
        this.questId.set(questId);
        this.tpLocation = tpLocation;

        this.text = text;

        this.setResponseChat(responseChat);
        this.setAnyResponseChat(anyResponseChat);

        this.needMoney.set(needMoney);
        this.setNeedStat(needStat);
        this.setNeedItem(needItem);

        this.rewardMoney.set(rewardMoney);
        this.setRewardStat(rewardStat);
        this.setRewardItem(rewardItem);
    }

    public void setResponseChat(@NonNull Map<WaitResponse, Long> responseChat) {
        for(Map.Entry<WaitResponse, Long> entry : responseChat.entrySet()) {
            this.setResponseChat(entry.getKey(), entry.getValue());
        }
    }

    public void setResponseChat(@NonNull WaitResponse waitResponse, long chatId) {
        Config.checkId(Id.CHAT, chatId);

        if(chatId == 0) {
            this.responseChat.remove(waitResponse);
        } else {
            this.responseChat.put(waitResponse, chatId);
        }
    }

    public long getResponseChat(@NonNull WaitResponse waitResponse) {
        Long value = this.responseChat.get(waitResponse);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void setAnyResponseChat(@NonNull Map<String, Long> anyResponseChat) {
        for(Map.Entry<String, Long> entry : anyResponseChat.entrySet()) {
            this.setAnyResponseChat(entry.getKey(), entry.getValue());
        }
    }

    public void setAnyResponseChat(@NonNull String response, long chatId) {
        Config.checkId(Id.CHAT, chatId);

        if(chatId == 0) {
            this.anyResponseChat.remove(response);
        } else {
            this.anyResponseChat.put(response, chatId);
        }
    }

    public long getAnyResponseChat(@NonNull String response) {
        Long value = this.anyResponseChat.get(response);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void setNeedStat(@NonNull Map<StatType, Integer> needStat) {
        for(Map.Entry<StatType, Integer> entry : needStat.entrySet()) {
            this.setNeedStat(entry.getKey(), entry.getValue());
        }
    }

    public void setNeedStat(@NonNull StatType statType, int stat) {
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

    public int getNeedStat(@NonNull StatType statType) {
        Integer value = this.needStat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void addNeedStat(@NonNull StatType statType, int stat) {
        this.setNeedStat(statType, this.getNeedStat(statType) + stat);
    }

    public void setNeedItem(@NonNull Map<Long, Integer> needItem) {
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
        Integer value = this.needItem.get(itemId);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void addNeedItem(long itemId, int count) {
        this.setNeedItem(itemId, this.getNeedItem(itemId) + count);
    }

    public void setRewardStat(@NonNull Map<StatType, Integer> rewardStat) {
        for(Map.Entry<StatType, Integer> entry : rewardStat.entrySet()) {
            this.setRewardStat(entry.getKey(), entry.getValue());
        }
    }

    public void setRewardStat(@NonNull StatType statType, int stat) {
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

    public int getRewardStat(@NonNull StatType statType) {
        Integer value = this.rewardStat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void addRewardStat(@NonNull StatType statType, int stat) {
        this.setRewardStat(statType, this.getRewardStat(statType) + stat);
    }

    public void setRewardItem(@NonNull Map<Long, Integer> rewardItem) {
        for(Map.Entry<Long, Integer> entry : rewardItem.entrySet()) {
            this.setRewardItem(entry.getKey(), entry.getValue());
        }
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
        Integer value = this.rewardItem.get(itemId);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void addRewardItem(long itemId, int count) {
        this.setRewardItem(itemId, this.getRewardItem(itemId) + count);
    }

}
