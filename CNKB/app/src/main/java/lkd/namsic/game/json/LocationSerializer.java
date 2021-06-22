package lkd.namsic.game.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import lkd.namsic.game.base.Location;

public class LocationSerializer implements JsonSerializer<Location> {

    @Override
    public JsonElement serialize(Location location, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(location.toString());
    }

}
