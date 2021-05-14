package lkd.namsic.game.json;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.gameObject.Npc;

public class NpcSerializer implements JsonSerializer<Npc> {

    @Override
    public JsonElement serialize(Npc npc, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new GsonBuilder().serializeNulls().create();

        JsonObject jsonObject = gson.toJsonTree(npc).getAsJsonObject();

        JsonArray commonChatArray = new JsonArray();
        JsonArray chatArray = new JsonArray();

        JsonArray wrapArray;
        JsonObject chatLimitObject;
        JsonArray setArray;

        for(Map.Entry<ChatLimit, ConcurrentHashSet<Long>> entry : npc.getCommonChat().entrySet()) {
            wrapArray = new JsonArray();

            chatLimitObject = gson.toJsonTree(entry.getKey()).getAsJsonObject();
            setArray = gson.toJsonTree(entry.getValue()).getAsJsonArray();

            wrapArray.add(chatLimitObject);
            wrapArray.add(setArray);

            commonChatArray.add(wrapArray);
        }

        for(Map.Entry<ChatLimit, ConcurrentHashSet<Long>> entry : npc.getChat().entrySet()) {
            wrapArray = new JsonArray();

            chatLimitObject = gson.toJsonTree(entry.getKey()).getAsJsonObject();
            setArray = gson.toJsonTree(entry.getValue()).getAsJsonArray();

            wrapArray.add(chatLimitObject);
            wrapArray.add(setArray);

            chatArray.add(wrapArray);
        }

        jsonObject.add("commonChat", commonChatArray);
        jsonObject.add("chat", chatArray);

        return jsonObject;
    }

}
