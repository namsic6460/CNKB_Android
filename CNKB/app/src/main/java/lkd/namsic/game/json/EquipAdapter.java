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
import java.util.ArrayList;
import java.util.List;

import lkd.namsic.game.base.EquipUse;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.gameObject.Equipment;

public class EquipAdapter implements JsonSerializer<Equipment>, JsonDeserializer<Equipment> {

    @Override
    public JsonElement serialize(Equipment equipment, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(equipment, Equipment.class).getAsJsonObject();

        EquipUse equipUse = equipment.getEquipUse();
        if(equipUse == null) {
            jsonObject.add("equipUse", new JsonPrimitive("null"));
        } else {
            jsonObject.add("equipUse", new JsonPrimitive(Config.serialize(equipUse)));
        }

        JsonArray jsonArray = new JsonArray();
        List<Event> events = equipment.getEvents();
        for(Event event : events) {
            jsonArray.add(Config.serialize(event));
        }

        jsonObject.add("events", jsonArray);

        return jsonObject;
    }

    @Override
    public Equipment deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String serialized = jsonObject.remove("equipUse").getAsString();

        EquipUse equipUse;
        if(serialized.equals("null")) {
            equipUse = null;
        } else {
            equipUse = Config.deserialize(serialized);
        }

        JsonArray jsonArray = jsonObject.remove("events").getAsJsonArray();
        jsonObject.add("events", new JsonArray());

        List<Event> events = new ArrayList<>();
        for(JsonElement element : jsonArray) {
            events.add(Config.deserialize(element.getAsString()));
        }


        Equipment equipment = gson.fromJson(jsonObject, Equipment.class);
        equipment.setEquipUse(equipUse);
        equipment.getEvents().addAll(events);

        return equipment;
    }

}
