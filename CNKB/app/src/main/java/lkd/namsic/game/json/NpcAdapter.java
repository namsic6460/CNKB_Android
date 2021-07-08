package lkd.namsic.game.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.gameObject.Npc;

public class NpcAdapter implements JsonSerializer<Npc>, JsonDeserializer<Npc> {

    @Override
    public JsonElement serialize(Npc npc, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new GsonBuilder().create();

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

    @SuppressWarnings("unchecked")
    @Override
    public Npc deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonArray commonChatArray = jsonObject.getAsJsonArray("commonChat");
        JsonArray chatArray = jsonObject.getAsJsonArray("chat");

        jsonObject.add("commonChat", null);
        jsonObject.add("chat", null);

        Npc npc = gson.fromJson(jsonObject, Npc.class);

        Map<ChatLimit, ConcurrentHashSet<Long>> commonChat = new ConcurrentHashMap<>();
        Map<ChatLimit, ConcurrentHashSet<Long>> chat = new ConcurrentHashMap<>();

        JsonArray wrapArray;
        JsonObject chatLimitObject;
        JsonArray setArray;

        ChatLimit chatLimit;
        ConcurrentHashSet<Double> tempSet;
        ConcurrentHashSet<Long> chatSet;

        for(JsonElement element : commonChatArray) {
            wrapArray = element.getAsJsonArray();

            chatLimitObject = wrapArray.get(0).getAsJsonObject();
            setArray = wrapArray.get(1).getAsJsonArray();

            tempSet = gson.fromJson(setArray, ConcurrentHashSet.class);
            chatLimit = gson.fromJson(chatLimitObject, ChatLimit.class);

            chatSet = new ConcurrentHashSet<>();
            for(Double chatId : tempSet) {
                chatSet.add(chatId.longValue());
            }

            commonChat.put(chatLimit, chatSet);
        }

        for(JsonElement element : chatArray) {
            wrapArray = element.getAsJsonArray();

            chatLimitObject = wrapArray.get(0).getAsJsonObject();
            setArray = wrapArray.get(1).getAsJsonArray();

            tempSet = gson.fromJson(setArray, ConcurrentHashSet.class);
            chatLimit = gson.fromJson(chatLimitObject, ChatLimit.class);

            chatSet = new ConcurrentHashSet<>();
            for(Double chatId : tempSet) {
                chatSet.add(chatId.longValue());
            }

            chat.put(chatLimit, chatSet);
        }

        npc.setCommonChat(commonChat);
        npc.setChat(chat);

        return npc;
    }

}
