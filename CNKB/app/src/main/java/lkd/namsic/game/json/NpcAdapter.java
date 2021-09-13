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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.object.Npc;

public class NpcAdapter implements JsonSerializer<Npc>, JsonDeserializer<Npc> {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ChatLimit.class, new ChatLimitAdapter())
            .create();

    @Override
    public JsonElement serialize(Npc npc, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = gson.toJsonTree(npc).getAsJsonObject();

        JsonArray commonChatArray = new JsonArray();
        JsonArray chatArray = new JsonArray();

        JsonArray wrapArray;
        JsonObject chatLimitObject;
        JsonArray setArray;

        for(Map.Entry<ChatLimit, Set<Long>> entry : npc.getBaseChat().entrySet()) {
            wrapArray = new JsonArray(2);

            chatLimitObject = gson.toJsonTree(entry.getKey()).getAsJsonObject();
            setArray = gson.toJsonTree(entry.getValue()).getAsJsonArray();

            wrapArray.add(chatLimitObject);
            wrapArray.add(setArray);

            commonChatArray.add(wrapArray);
        }

        for(Map.Entry<ChatLimit, Set<Long>> entry : npc.getChat().entrySet()) {
            wrapArray = new JsonArray(2);

            chatLimitObject = gson.toJsonTree(entry.getKey()).getAsJsonObject();
            setArray = gson.toJsonTree(entry.getValue()).getAsJsonArray();

            wrapArray.add(chatLimitObject);
            wrapArray.add(setArray);

            chatArray.add(wrapArray);
        }

        jsonObject.add("baseChat", commonChatArray);
        jsonObject.add("chat", chatArray);

        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Npc deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonArray commonChatArray = jsonObject.remove("baseChat").getAsJsonArray();
        JsonArray chatArray = jsonObject.remove("chat").getAsJsonArray();

        Npc npc = gson.fromJson(jsonObject, Npc.class);

        Map<ChatLimit, Set<Long>> commonChat = new HashMap<>();
        Map<ChatLimit, Set<Long>> chat = new HashMap<>();

        JsonArray wrapArray;
        JsonObject chatLimitObject;
        JsonArray setArray;

        ChatLimit chatLimit;
        Set<Double> tempSet;
        Set<Long> chatSet;

        for(JsonElement element : commonChatArray) {
            wrapArray = element.getAsJsonArray();

            chatLimitObject = wrapArray.get(0).getAsJsonObject();
            setArray = wrapArray.get(1).getAsJsonArray();

            chatLimit = gson.fromJson(chatLimitObject, ChatLimit.class);
            tempSet = gson.fromJson(setArray, HashSet.class);

            chatSet = new HashSet<>();
            for(Double chatId : tempSet) {
                chatSet.add(chatId.longValue());
            }

            commonChat.put(chatLimit, chatSet);
        }

        for(JsonElement element : chatArray) {
            wrapArray = element.getAsJsonArray();

            chatLimitObject = wrapArray.get(0).getAsJsonObject();
            setArray = wrapArray.get(1).getAsJsonArray();

            tempSet = gson.fromJson(setArray, HashSet.class);
            chatLimit = gson.fromJson(chatLimitObject, ChatLimit.class);

            chatSet = new HashSet<>();
            for(Double chatId : tempSet) {
                chatSet.add(chatId.longValue());
            }

            chat.put(chatLimit, chatSet);
        }

        npc.setBaseChat(commonChat);
        npc.setChat(chat);

        return npc;
    }

}
