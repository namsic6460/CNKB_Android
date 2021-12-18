package lkd.namsic.game.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.enums.object.QuestList;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Chat extends GameObject {
    
    @Setter
    @Nullable
    String name;
    
    @Setter
    long delayTime = Config.DEFAULT_DELAY_TIME;
    
    @Setter
    long pauseTime = Config.DEFAULT_PAUSE_TIME;
    
    @Setter
    long questId = QuestList.NONE.getId();
    
    @Setter
    long noneNpcId = NpcList.NONE.getId();
    
    @Nullable
    final Location tpLocation = null;
    
    final List<String> text = new ArrayList<>();
    
    @Setter
    boolean baseMsg = false;
    
    final Map<WaitResponse, Long> responseChat = new HashMap<>();
    final Map<String, Long> anyResponseChat = new HashMap<>();
    
    @Setter
    long money = 0;
    
    final Set<Long> equipment = new HashSet<>();
    final Map<Long, Integer> item = new HashMap<>();
    final Map<Variable, Integer> variable = new HashMap<>();
    
    public Chat(@Nullable String name) {
        this.name = name;
        this.id.setId(Id.CHAT);
    }
    
    public void setResponseChat(@NonNull WaitResponse waitResponse, long chatId) {
        if(chatId == 0) {
            this.responseChat.remove(waitResponse);
        } else {
            this.responseChat.put(waitResponse, chatId);
        }
    }
    
    public long getResponseChat(@NonNull WaitResponse waitResponse) {
        return this.responseChat.getOrDefault(waitResponse, 0L);
    }
    
    public void setAnyResponseChat(@NonNull String response, long chatId) {
        if(chatId == 0) {
            this.anyResponseChat.remove(response);
        } else {
            this.anyResponseChat.put(response, chatId);
        }
    }
    
    public long getAnyResponseChat(@NonNull String response) {
        return this.anyResponseChat.getOrDefault(response, 0L);
    }
    
    public void setItem(long itemId, int count) {
        if(count < 0) {
            throw new NumberRangeException(count, 0);
        }
        
        if(count == 0) {
            this.item.remove(itemId);
        } else {
            this.item.put(itemId, count);
        }
    }
    
    public int getItem(long itemId) {
        return this.item.getOrDefault(itemId, 0);
    }
    
    public void addItem(long itemId, int count) {
        this.setItem(itemId, this.getItem(itemId) + count);
    }
    
    public void setVariable(@NonNull Variable variable, int value) {
        if(value == 0) {
            this.variable.remove(variable);
        } else {
            this.variable.put(variable, value);
        }
    }
    
    public int getVariable(@NonNull Variable variable) {
        return this.variable.put(variable, 0);
    }
    
}
