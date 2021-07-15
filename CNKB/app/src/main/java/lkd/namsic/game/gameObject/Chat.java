package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.base.LimitId;
import lkd.namsic.game.base.LimitLong;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Chat extends GameObject {

    @Setter
    @Nullable
    String name;

    final LimitLong delayTime = new LimitLong(Config.MIN_DELAY_TIME, Config.MIN_DELAY_TIME, Config.MAX_DELAY_TIME);
    final LimitLong pauseTime = new LimitLong(Config.MIN_PAUSE_TIME, Config.MIN_PAUSE_TIME, Config.MAX_PAUSE_TIME);
    final LimitId questId = new LimitId(0, Id.QUEST);

    @Nullable
    final Location tpLocation = null;

    final List<String> text = new ArrayList<>();

    @Setter
    boolean baseMsg = false;

    final Map<WaitResponse, Long> responseChat = new HashMap<>();
    final Map<String, Long> anyResponseChat = new HashMap<>();

    final LimitLong money = new LimitLong(0, 0L, null);
    final Set<Long> equipment = new HashSet<>();
    final Map<Long, Integer> item = new HashMap<>();
    final Map<Variable, Integer> variable = new HashMap<>();

    public Chat(@Nullable String name) {
        this.name = name;
        this.id.setId(Id.CHAT);
    }

    public void setResponseChat(@NonNull WaitResponse waitResponse, long chatId) {
        this.setResponseChat(waitResponse, chatId, false);
    }

    public void setResponseChat(@NonNull WaitResponse waitResponse, long chatId, boolean skip) {
        if(!skip) {
            Config.checkId(Id.CHAT, chatId);
        }

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
        this.setAnyResponseChat(response, chatId, false);
    }

    public void setAnyResponseChat(@NonNull String response, long chatId, boolean skip) {
        if(!skip) {
            Config.checkId(Id.CHAT, chatId);
        }

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
        Config.checkId(Id.ITEM, itemId);

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
