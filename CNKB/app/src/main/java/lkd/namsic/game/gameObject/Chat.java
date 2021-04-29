package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lkd.namsic.game.base.LimitId;
import lkd.namsic.game.base.LimitLong;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Chat extends GameObject {

    final LimitLong pauseTime = new LimitLong(Config.MIN_PAUSE_TIME, Config.MIN_PAUSE_TIME, Config.MAX_PAUSE_TIME);
    final LimitId questId = new LimitId(0, Id.QUEST);
    final Location tpLocation = new Location();

    final List<String> text = new ArrayList<>();

    final Map<WaitResponse, Long> responseChat = new HashMap<>();
    final Map<String, Long> anyResponseChat = new HashMap<>();

    final LimitLong needMoney = new LimitLong(0, 0L, Long.MAX_VALUE);
    final Map<StatType, Integer> needStat = new HashMap<>();
    final Map<Long, Integer> needItem = new HashMap<>();

    final LimitLong rewardMoney = new LimitLong(0, 0L, Long.MAX_VALUE);
    final Map<StatType, Integer> rewardStat = new HashMap<>();
    final Map<Long, Integer> rewardItem = new HashMap<>();

    public Chat() {
        this.id.setId(Id.CHAT);
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
        return this.responseChat.getOrDefault(waitResponse, 0L);
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
        return this.anyResponseChat.getOrDefault(response, 0L);
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
        return this.needStat.getOrDefault(statType, 0);
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
        return this.needItem.getOrDefault(itemId, 0);
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
        return this.rewardStat.getOrDefault(statType, 0);
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
        return this.rewardItem.getOrDefault(itemId, 0);
    }

    public void addRewardItem(long itemId, int count) {
        this.setRewardItem(itemId, this.getRewardItem(itemId) + count);
    }

}
