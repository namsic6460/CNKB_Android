package lkd.namsic.game.json;

import com.google.gson.Gson;
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
import java.util.stream.Collectors;

import lkd.namsic.game.object.Shop;

public class ShopAdapter implements JsonSerializer<Shop>, JsonDeserializer<Shop> {

    private final Gson gson = new Gson();

    @Override
    public JsonElement serialize(Shop shop, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = gson.toJsonTree(shop).getAsJsonObject();

        JsonObject simpleMapObject = new JsonObject();
        JsonObject keyObject = new JsonObject();
        Map<Set<Long>, Long> keys = new HashMap<>();

        long id = 1;
        Long key;

        Set<Long> value;
        for(Map.Entry<String, Set<Long>> entry : shop.getSimpleMap().entrySet()) {
            value = entry.getValue();
            key = keys.get(value);

            if(key == null) {
                key = id++;
                keyObject.addProperty(key.toString(), gson.toJson(value));
                keys.put(value, key);
            }

            simpleMapObject.addProperty(entry.getKey(), key);
        }

        jsonObject.add("simpleMap", simpleMapObject);
        jsonObject.add("simpleMapKey", keyObject);

        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Shop deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        JsonObject simpleMapObject = jsonObject.remove("simpleMap").getAsJsonObject();
        JsonObject keyObject = jsonObject.remove("simpleMapKey").getAsJsonObject();

        Shop shop = gson.fromJson(jsonObject, Shop.class);

        Map<String, Set<Long>> simpleMap = new HashMap<>();

        long key;
        Set<Double> tempSet;
        Set<Long> idSet;
        for(Map.Entry<String, JsonElement> entry : simpleMapObject.entrySet()) {
            key = entry.getValue().getAsLong();

            tempSet = gson.fromJson(keyObject.get(Long.toString(key)).getAsString(), HashSet.class);

            idSet = tempSet.stream().map(Double::longValue).collect(Collectors.toSet());
            simpleMap.put(entry.getKey(), idSet);
        }

        shop.setSimpleMap(simpleMap);

        return shop;
    }

}
