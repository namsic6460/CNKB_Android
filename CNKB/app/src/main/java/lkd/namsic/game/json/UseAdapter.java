package lkd.namsic.game.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.gameObject.Use;

public class UseAdapter implements JsonSerializer<Use>, JsonDeserializer<Use> {

    @Override
    public JsonElement serialize(Use use, Type typeOfSrc, JsonSerializationContext context) {
        if(use != null) {
            return new JsonPrimitive(Config.serialize(use));
        } else {
            return new JsonPrimitive("null");
        }
    }

    @Override
    public Use deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        String serialized = jsonElement.getAsString();
        if(!serialized.equals("null")) {
            return Config.deserialize(serialized);
        } else {
            return null;
        }
    }

}
