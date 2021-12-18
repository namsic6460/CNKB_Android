package lkd.namsic.game.object;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.QuestList;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Quest extends NamedObject {
    
    @Setter
    int clearLimitLv = 1;
    
    @Setter
    long needMoney = 0;
    
    final Map<Long, Integer> needItem = new HashMap<>();
    final Map<StatType, Integer> needStat = new HashMap<>();
    final Map<Long, Integer> needCloseRate = new HashMap<>();
    
    @Setter
    long rewardMoney = 0;
    
    @Setter
    long rewardExp = 0;
    
    @Setter
    int rewardAdv = 0;
    
    final Map<Long, Integer> rewardItem = new HashMap<>();
    final Map<StatType, Integer> rewardStat = new HashMap<>();
    final Map<Long, Integer> rewardCloseRate = new HashMap<>();
    final Set<Long> rewardItemRecipe = new HashSet<>();
    final Set<Long> rewardEquipRecipe = new HashSet<>();
    
    @Setter
    long clearNpcId;
    
    @Setter
    long clearChatId;
    
    public Quest(@NonNull QuestList questData, long clearNpcId, long clearChatId) {
        super(questData.getDisplayName());
        
        this.id.setId(Id.QUEST);
        this.id.setObjectId(questData.getId());
        
        this.clearNpcId = clearNpcId;
        this.clearChatId = clearChatId;
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
    
    public void setNeedCloseRate(long npcId, int closeRate) {
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
        return this.needCloseRate.getOrDefault(npcId, 0);
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
    
}
