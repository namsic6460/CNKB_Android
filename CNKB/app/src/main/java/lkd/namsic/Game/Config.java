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

    public static final Map<Id, Long> ID_MAP = new ConcurrentHashMap<>();

    public static final int MIN_MAP_X = 0;
    public static final int MAX_MAP_X = 10;
    public static final int MIN_MAP_Y = 0;
    public static final int MAX_MAP_Y = 10;

    public static final int MIN_HANDLE_LV = 1;
    public static final int MAX_HANDLE_LV = 20;
    public static final int MIN_LV = 1;
    public static final int MAX_LV = 999;

    public static JSONObject createConfig() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        JSONObject idObject = new JSONObject();
        for(Id id : Id.values()) {
            ID_MAP.put(id, 1L);
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
        String serialized = serialize(t);
        if(serialized == null) {
            Logger.e("saveObject", new RuntimeException("Failed to save object - " + t.getPath()));
            return;
        }

        FileManager.save(t.getPath(), serialized);
    }

    @Nullable
    public static <T extends GameClass> T getObject(Id id, long objectId) {
        String serialized = FileManager.read(FileManager.DATA_PATH_MAP.get(id) + objectId + ".txt");
        if(serialized.equals("")) {
            Logger.e("getObject", new RuntimeException("Failed to get object - " + id + ", " + objectId));
            return null;
        }

        return deserialize(serialized);
    }

}
