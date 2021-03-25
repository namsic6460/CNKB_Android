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
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Class.GameObject;
import lkd.namsic.Game.Class.MapClass;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lkd.namsic.Game.Exception.ObjectNotFoundException;
import lkd.namsic.Game.Exception.UnhandledEnumException;
import lkd.namsic.Setting.FileManager;
import lkd.namsic.Setting.Logger;

public class Config {

    /*
    TODO
    1. moveMap - load, unload map
    2. save player count on a map
    3. create/parse config
     */

    public static final Map<String, MapClass> MAP = new ConcurrentHashMap<>();

    public static final Map<Id, Map<Long, GameObject>> CURRENT_OBJECT = new ConcurrentHashMap<>();
    public static final Map<Id, Map<Long, Long>> CURRENT_OBJECT_COUNT = new ConcurrentHashMap<>();
    public static final Map<Id, Long> ID_MAP = new ConcurrentHashMap<>();

    public static final int MIN_MAP_X = 0;
    public static final int MAX_MAP_X = 10;
    public static final int MIN_MAP_Y = 0;
    public static final int MAX_MAP_Y = 10;
    public static final int MIN_FIELD_X = 1;
    public static final int MAX_FIELD_X = 64;
    public static final int MIN_FIELD_Y = 1;
    public static final int MAX_FIELD_Y = 64;

    public static final int MIN_HANDLE_LV = 1;
    public static final int MAX_HANDLE_LV = 20;
    public static final int MIN_REINFORCE_COUNT = 0;
    public static final int MAX_REINFORCE_COUNT = 19;
    public static final int MIN_LV = 1;
    public static final int MAX_LV = 999;
    public static final int MIN_SP = 0;
    public static final int MAX_SP = 100;

    public static void init() {
        if(!CURRENT_OBJECT.isEmpty()) {
            return;
        }

        for(Id id : Id.values()) {
            CURRENT_OBJECT.put(id, new ConcurrentHashMap<>());
            CURRENT_OBJECT_COUNT.put(id, new ConcurrentHashMap<>());
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

    public static <T extends GameObject> T newObject(@NonNull T t) {
        Id id = t.id.getId();
        long objectId = ID_MAP.get(id);

        t.id.setObjectId(objectId);
        ID_MAP.put(id, objectId + 1);

        return t;
    }

    public static void checkId(@NonNull Id id, long objectId) throws NumberRangeException {
        if(objectId < 1 || ID_MAP.get(id) < objectId) {
            throw new NumberRangeException(objectId, 1L, ID_MAP.get(id));
        }
    }

    public static void checkStatType(@NonNull StatType statType) throws UnhandledEnumException {
        if(statType.equals(StatType.HP) || statType.equals(StatType.MN)) {
            throw new UnhandledEnumException(statType);
        }
    }

    @Nullable
    public static <T extends Serializable> String serialize(@NonNull T t) {
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
    public static <T extends Serializable> T deserialize(@NonNull String byteStr) {
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

    public static void saveObject(@NonNull GameObject gameObject) {
        Id id = gameObject.id.getId();
        long objectId = gameObject.id.getObjectId();
        Long objectCount = CURRENT_OBJECT_COUNT.get(id).get(objectId);

        if(objectCount > 1) {
            CURRENT_OBJECT_COUNT.get(id).put(objectId, objectCount - 1);
        } else {
            String serialized = serialize(gameObject);
            String path = getPath(id, objectId);
            if (serialized == null) {
                Logger.e("saveObject", new RuntimeException("Failed to save object - " + path));
                return;
            }

            FileManager.save(path, serialized);

            if(objectCount == 1) {
                CURRENT_OBJECT.get(id).remove(objectId);
                CURRENT_OBJECT_COUNT.get(id).remove(objectId);
            }
        }
    }

    public static void unloadMap(@NonNull MapClass map) {
        String serialized = serialize(map);
        String locStr = map.getLocation().getFileName();
        String path = FileManager.MAP_PATH + "/" + locStr;

        FileManager.save(path, serialized);
        MAP.remove(locStr);
    }

    public static void discardObject(@NonNull GameObject gameObject) {
        Id id = gameObject.id.getId();
        long objectId = gameObject.id.getObjectId();
        Long objectCount = CURRENT_OBJECT_COUNT.get(id).get(objectId);

        if (objectCount > 1) {
            CURRENT_OBJECT_COUNT.get(id).put(objectId, objectCount - 1);
        } else {
            CURRENT_OBJECT.get(id).remove(objectId);
            CURRENT_OBJECT_COUNT.get(id).remove(objectId);
        }
    }

    @NonNull
    public static <T extends GameObject> T getObject(@NonNull Id id, long objectId) {
        checkId(id, objectId);

        Long objectCount = CURRENT_OBJECT_COUNT.get(id).get(objectId);

        if(objectCount == null) {
            String path = getPath(id, objectId);
            String serialized = FileManager.read(path);
            if (serialized.equals("")) {
                Logger.e("getObject", new RuntimeException("Failed to get object - " + id + ", " + objectId));
                throw new ObjectNotFoundException(path);
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

    public static MapClass loadMap(@NonNull String locStr) {
        MapClass map = MAP.get(locStr);

        if(map == null) {
            String path = FileManager.MAP_PATH + "/" + locStr;
            String serialized = FileManager.read(path);
            if (serialized.equals("")) {
                Logger.e("getObject", new RuntimeException("Failed to load map - " + locStr));
                throw new ObjectNotFoundException(path);
            }

            map = deserialize(serialized);
            MAP.put(locStr, map);
        }

        return map;
    }

    @NonNull
    public static String mapsToString(@NonNull Map<?, ?> map1, @NonNull Map<?, ?> map2) {
        String str1, str2;

        if(map1 instanceof AbstractMap) {
            str1 = map1.toString();
        } else {
            str1 = new HashMap<>(map1).toString();
        }

        if(map2 instanceof AbstractMap) {
            str2 = map2.toString();
        } else {
            str2 = new HashMap<>(map2).toString();
        }

        return str1 + ", " + str2;
    }

    @NonNull
    public static String collectionToString(@NonNull Collection<?> collection) {
        if(collection instanceof AbstractCollection) {
            return collection.toString();
        }

        if(collection instanceof List) {
            return new ArrayList<>(collection).toString();
        } else if(collection instanceof Set) {
            return new HashSet<>(collection).toString();
        } else {
            return "Unsupported type (" + collection.toString() + ")";
        }
    }

    public static <T> boolean compareMap(@NonNull Map<T, Integer> map1, @NonNull Map<T, Integer> map2, boolean firstIsBig) {
        return compareMap(map1, map2, firstIsBig, true);
    }

    public static <T> boolean compareMap(@NonNull Map<T, Integer> map1, @NonNull Map<T, Integer> map2,
                                         boolean firstIsBig, boolean regardZero) {
        Integer value;
        for(Map.Entry<T, Integer> entry : map1.entrySet()) {
            value = map2.get(entry.getKey());
            if(value == null) {
                if(regardZero) {
                    value = 0;
                } else {
                    return false;
                }
            }

            if(firstIsBig) {
                if(entry.getValue() < value) {
                    return false;
                }
            } else if(entry.getValue() > value) {
                return false;
            }
        }

        return true;
    }

    private static String getPath(@NonNull Id id, long objectId) {
        return FileManager.DATA_PATH_MAP.get(id) + objectId + ".txt";
    }

    public static String errorString(@NonNull Throwable throwable) {
        StringBuilder builder = new StringBuilder(throwable.getClass().getName());
        builder.append(": ");
        builder.append(throwable.getMessage());
        builder.append("\n");
        for(StackTraceElement element : throwable.getStackTrace()) {
            builder.append("\tat");
            builder.append(element.toString());
        }

        return builder.toString();
    }

}
