package lkd.namsic.game.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.config.Config;

public class ChatLimitAdapter implements JsonSerializer<ChatLimit> {

    private final Gson gson = new Gson();

    @Override
    public JsonElement serialize(ChatLimit chatLimit, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonObject tempObject;
        JsonArray tempArray;

        if(chatLimit.getLimitLv().getMin() != 1 || chatLimit.getLimitLv().getMax() != Config.MAX_LV) {
            tempObject = gson.toJsonTree(chatLimit.getLimitLv() ).getAsJsonObject();
            jsonObject.add("limitLv", tempObject);
        }

        if(!(chatLimit.getLimitCloseRate().getMin().isEmpty() && chatLimit.getLimitCloseRate().getMax().isEmpty())) {
            tempObject = gson.toJsonTree(chatLimit.getLimitCloseRate()).getAsJsonObject();
            jsonObject.add("limitCloseRate", tempObject);
        }

        if(!(chatLimit.getLimitStat().getMin().isEmpty() && chatLimit.getLimitStat().getMax().isEmpty())) {
            tempObject = gson.toJsonTree(chatLimit.getLimitStat()).getAsJsonObject();
            jsonObject.add("limitStat", tempObject);
        }

        if(!(chatLimit.getLimitQuest().getMin().isEmpty() && chatLimit.getLimitQuest().getMax().isEmpty())) {
            tempObject = gson.toJsonTree(chatLimit.getLimitQuest()).getAsJsonObject();
            jsonObject.add("limitQuest", tempObject);
        }

        if(!chatLimit.getRunningQuest().isEmpty()) {
            tempArray = gson.toJsonTree(chatLimit.getRunningQuest()).getAsJsonArray();
            jsonObject.add("runningQuest", tempArray);
        }

        if(!chatLimit.getNotRunningQuest().isEmpty()) {
            tempArray = gson.toJsonTree(chatLimit.getNotRunningQuest()).getAsJsonArray();
            jsonObject.add("notRunningQuest", tempArray);
        }

        if(chatLimit.getLimitHour1().getMin() != 0 || chatLimit.getLimitHour1().getMax() != 23) {
            tempObject = gson.toJsonTree(chatLimit.getLimitHour1()).getAsJsonObject();
            jsonObject.add("limitHour1", tempObject);
        }

        if(chatLimit.getLimitHour2().getMin() != 24 || chatLimit.getLimitHour2().getMax() != 24) {
            tempObject = gson.toJsonTree(chatLimit.getLimitHour2()).getAsJsonObject();
            jsonObject.add("limitHour2", tempObject);
        }

        return jsonObject;
    }

}
