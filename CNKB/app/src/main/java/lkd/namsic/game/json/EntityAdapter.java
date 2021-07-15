package lkd.namsic.game.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import lkd.namsic.game.base.ConcurrentArrayList;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.gameObject.Entity;
import lkd.namsic.game.gameObject.Npc;

public class EntityAdapter<T extends Entity> implements JsonSerializer<T>, JsonDeserializer<T> {

    final Class<T> c;

    public EntityAdapter(Class<T> c) {
        super();

        if(c == Npc.class) {
            throw new RuntimeException();
        }

        this.c = c;
    }

    @Override
    public JsonElement serialize(T entity, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(entity, entity.getClass()).getAsJsonObject();

        JsonObject tempObject = new JsonObject();
        JsonArray jsonArray;

        Map<String, ConcurrentArrayList<Event>> events = entity.getEvents();
        for(Map.Entry<String, ConcurrentArrayList<Event>> entry : events.entrySet()) {
            jsonArray = new JsonArray();

            for(Event event : entry.getValue()) {
                jsonArray.add(new JsonPrimitive(Config.serialize(event)));
            }

            tempObject.add(entry.getKey(), jsonArray);
        }

        jsonObject.add("events", tempObject);

        tempObject = new JsonObject();

        Map<EquipType, ConcurrentArrayList<Event>> equipEvents = entity.getEquipEvents();
        for(Map.Entry<EquipType, ConcurrentArrayList<Event>> entry : equipEvents.entrySet()) {
            jsonArray = new JsonArray();

            for(Event event : entry.getValue()) {
                jsonArray.add(new JsonPrimitive(Config.serialize(event)));
            }

            tempObject.add(entry.getKey().name(), jsonArray);
        }

        jsonObject.add("equipEvents", tempObject);

        return jsonObject;
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        System.out.println("!");

        Gson gson = new Gson();
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonObject tempObject = jsonObject.remove("events").getAsJsonObject();
        jsonObject.add("events", new JsonObject());

        ConcurrentArrayList<Event> list;
        Map<String, ConcurrentArrayList<Event>> events = new HashMap<>();
        for(Map.Entry<String, JsonElement> entry : tempObject.entrySet()) {
            list = new ConcurrentArrayList<>();
            events.put(entry.getKey(), list);

            for(JsonElement serialized : entry.getValue().getAsJsonArray()) {
                list.add(Config.deserialize(serialized.getAsString()));
            }
        }

        tempObject = jsonObject.remove("equipEvents").getAsJsonObject();
        jsonObject.add("equipEvents", new JsonObject());

        Map<EquipType, ConcurrentArrayList<Event>> equipEvents = new HashMap<>();
        for(Map.Entry<String, JsonElement> entry : tempObject.entrySet()) {
            list = new ConcurrentArrayList<>();
            equipEvents.put(EquipType.valueOf(entry.getKey()), list);

            for(JsonElement serialized : entry.getValue().getAsJsonArray()) {
                list.add(Config.deserialize(serialized.getAsString()));
            }
        }

        T entity = gson.fromJson(jsonObject, c);
        entity.getEvents().putAll(events);
        entity.getEquipEvents().putAll(equipEvents);

        return entity;
    }

}
