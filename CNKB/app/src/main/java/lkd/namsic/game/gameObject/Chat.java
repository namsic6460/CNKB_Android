package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.Variable;
import lkd.namsic.game.base.LimitId;
import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.LimitLong;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.WaitResponse;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Chat extends GameObject {

    final LimitLong delayTime = new LimitLong(Config.MIN_DELAY_TIME, Config.MIN_DELAY_TIME, Config.MAX_DELAY_TIME);
    final LimitLong pauseTime = new LimitLong(Config.MIN_PAUSE_TIME, Config.MIN_PAUSE_TIME, Config.MAX_PAUSE_TIME);
    final LimitId questId = new LimitId(0, Id.QUEST);
    final Location tpLocation = new Location();

    final List<String> text = new ArrayList<>();

    final Map<WaitResponse, Long> responseChat = new HashMap<>();
    final Map<String, Long> anyResponseChat = new HashMap<>();

    final LimitInteger money = new LimitInteger(0, 0, null);
    final Set<Long> equipment = new HashSet<>();
    final Map<Long, Integer> item = new HashMap<>();
    final Map<Variable, Integer> variable = new HashMap<>();

    public Chat() {
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

    public void setVariable(@NonNull Variable variable, int value) {
        this.variable.put(variable, value);
    }

    public int getVariable(@NonNull Variable variable) {
        return this.variable.put(variable, 0);
    }

}
