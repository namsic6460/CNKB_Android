package lkd.namsic.Game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Class.GameClass;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Setting.FileManager;
import lkd.namsic.Setting.Logger;

public class Config {

    public static final Map<Id, Map<Long, GameClass>> CURRENT_OBJECT = new ConcurrentHashMap<>();
    public static final Map<Id, Map<Long, Long>> CURRENT_OBJECT_COUNT = new ConcurrentHashMap<>();
    public static final Map<Id, Long> ID_MAP = new ConcurrentHashMap<>();

    public static final int MIN_MAP_X = 0;
    public static final int MAX_MAP_X = 10;
    public static final int MIN_MAP_Y = 0;
    public static final int MAX_MAP_Y = 10;

    public static final int MIN_HANDLE_LV = 1;
    public static final int MAX_HANDLE_LV = 20;
    public static final int MIN_REINFORCE_COUNT = 0;
    public static final int MAX_REINFORCE_COUNT = 19;
    public static final int MIN_LV = 1;
    public static final int MAX_LV = 999;

    public static void init() {
        if(!CURRENT_OBJECT.isEmpty()) {
            return;
        }

        for(Id id : Id.values()) {
            CURRENT_OBJECT.put(id, new ConcurrentHashMap<Long, GameClass>());
            CURRENT_OBJECT_COUNT.put(id, new ConcurrentHashMap<Long, Long>());
            ID_MAP.put(id, 1L);
        }
    }

    public static JSONObject createConfig() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        JSONObject idObject = new JSONObject();
        for(Id id : Id.values()) {
            idObject.put(id.toString(), 1L);
        }
        jsonObject.put("id", idObject);

        return jsonObject;
    }

    public static void parseConfig(JSONObject jsonObject) throws JSONException {
        JSONObject idObject = jsonObject.getJSONObject("id");

        String idName;
        for(Id id : Id.values()) {
            idName = id.toString();
            idObject.put(idName, idObject.getLong(idName));
        }
    }

    @Nullable
    public static <T extends GameClass> String serialize(@NonNull T t) {
        byte[] serialized;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(t);
            serialized = baos.toByteArray();
        } catch (IOException e) {
            Logger.e("serialize", e);
            return null;
        }

        return Base64.getEncoder().encodeToString(serialized);
    }

    @Nullable
    public static <T extends GameClass> T deserialize(String byteStr) {
        byte[] serialized = Base64.getDecoder().decode(byteStr);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(serialized)) {
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object object = ois.readObject();
            return (T) object;
        } catch (IOException | ClassNotFoundException e) {
            Logger.e("deserialize", e);
            return null;
        }
    }

    public static <T extends GameClass> void saveObject(T t) {
        Id id = t.id.getId();
        long objectId = t.id.getObjectId();
        Long objectCount = CURRENT_OBJECT_COUNT.get(id).get(objectId);

        if(objectCount != 1) {
            CURRENT_OBJECT_COUNT.get(id).put(objectId, objectCount - 1);
        } else {
            String serialized = serialize(t);
            String path = getPath(id, objectId);
            if (serialized == null) {
                Logger.e("saveObject", new RuntimeException("Failed to save object - " + path));
                return;
            }

            FileManager.save(path, serialized);
            CURRENT_OBJECT.get(id).remove(objectId);
            CURRENT_OBJECT_COUNT.get(id).remove(objectId);
        }
    }

    @Nullable
    public static <T extends GameClass> T getObject(Id id, long objectId) {
        Long objectCount = CURRENT_OBJECT_COUNT.get(id).get(objectId);

        if(objectCount == null) {
            String serialized = FileManager.read(getPath(id, objectId));
            if (serialized.equals("")) {
                Logger.e("getObject", new RuntimeException("Failed to get object - " + id + ", " + objectId));
                return null;
            }

            T t = deserialize(serialized);
            CURRENT_OBJECT.get(id).put(objectId, t);
            CURRENT_OBJECT_COUNT.get(id).put(objectId, 1L);
            return t;
        } else {
            CURRENT_OBJECT_COUNT.get(id).put(objectId, objectCount + 1);
            return (T) CURRENT_OBJECT.get(id).get(objectId);
        }
    }

    private static String getPath(Id id, long objectId) {
        return FileManager.DATA_PATH_MAP.get(id) + objectId + ".txt";
    }

}
