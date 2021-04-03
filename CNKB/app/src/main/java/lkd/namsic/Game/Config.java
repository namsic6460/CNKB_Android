package lkd.namsic.Game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.GameObject.Achieve;
import lkd.namsic.Game.GameObject.Boss;
import lkd.namsic.Game.GameObject.Chat;
import lkd.namsic.Game.GameObject.Equipment;
import lkd.namsic.Game.GameObject.GameObject;
import lkd.namsic.Game.GameObject.Item;
import lkd.namsic.Game.GameObject.MapClass;
import lkd.namsic.Game.GameObject.Monster;
import lkd.namsic.Game.GameObject.Npc;
import lkd.namsic.Game.GameObject.Player;
import lkd.namsic.Game.GameObject.Quest;
import lkd.namsic.Game.GameObject.Research;
import lkd.namsic.Game.GameObject.Skill;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lkd.namsic.Game.Exception.ObjectNotFoundException;
import lkd.namsic.Game.Exception.UnhandledEnumException;
import lkd.namsic.Setting.FileManager;
import lkd.namsic.Setting.Logger;

public class Config {

    public static final Map<Id, Long> ID_COUNT = new ConcurrentHashMap<>();
    public static final Map<Id, Class<?>> ID_CLASS = new HashMap<>();

    public static final Map<Id, ConcurrentHashMap<Long, GameObject>> OBJECT = new ConcurrentHashMap<>();
    public static final Map<Id, ConcurrentHashMap<Long, Long>> OBJECT_COUNT = new ConcurrentHashMap<>();

    public static final Map<String, MapClass> MAP = new ConcurrentHashMap<>();
    public static final Map<String, Long> PLAYER_COUNT = new ConcurrentHashMap<>();

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
    public static final long MIN_PAUSE_TIME = 1000;
    public static final long MAX_PAUSE_TIME = 5000;

    public static void init() {
        ID_CLASS.put(Id.ACHIEVE, Achieve.class);
        ID_CLASS.put(Id.BOSS, Boss.class);
        ID_CLASS.put(Id.CHAT, Chat.class);
        ID_CLASS.put(Id.EQUIPMENT, Equipment.class);
        ID_CLASS.put(Id.ITEM, Item.class);
        ID_CLASS.put(Id.MONSTER, Monster.class);
        ID_CLASS.put(Id.NPC, Npc.class);
        ID_CLASS.put(Id.PLAYER, Player.class);
        ID_CLASS.put(Id.QUEST, Quest.class);
        ID_CLASS.put(Id.RESEARCH, Research.class);
        ID_CLASS.put(Id.SKILL, Skill.class);

        if(!OBJECT.isEmpty()) {
            return;
        }

        for(Id id : Id.values()) {
            ID_COUNT.put(id, 1L);

            OBJECT.put(id, new ConcurrentHashMap<>());
            OBJECT_COUNT.put(id, new ConcurrentHashMap<>());
        }
    }

    public static JSONObject createConfig() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        JSONObject idJson = new JSONObject();
        String idName;
        for(Id id : Id.values()) {
            idName = id.toString();
            idJson.put(idName, ID_COUNT.get(id));
        }

        jsonObject.put("id", idJson);

        return jsonObject;
    }

    public static void parseConfig(JSONObject jsonObject) throws JSONException {
        JSONObject idObject = jsonObject.getJSONObject("id");

        String idName;
        for(Id id : Id.values()) {
            idName = id.toString();

            ID_COUNT.put(id, idObject.getLong(idName));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void onTerminate() {
        try {
            for(Id id : Id.values()) {
                List<GameObject> objectCopy = new ArrayList<>(OBJECT.get(id).values());
                for (GameObject gameObject : objectCopy) {
                    unloadObject(gameObject);
                }

                List<MapClass> mapCopy = new ArrayList<>(MAP.values());
                for(MapClass map : mapCopy) {
                    unloadMap(map);
                }
            }

            FileManager.save(FileManager.CONFIG_PATH, createConfig().toString());
        } catch (Exception e) {
            Logger.e("onTerminate", e);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static <T extends GameObject> T newObject(@NonNull T t) {
        Id id = t.id.getId();
        long objectId = ID_COUNT.get(id);

        t.id.setObjectId(objectId);
        ID_COUNT.put(id, objectId + 1);

        return t;
    }

    @SuppressWarnings("ConstantConditions")
    public static void checkId(@NonNull Id id, long objectId) throws NumberRangeException {
        if(objectId < 1 || ID_COUNT.get(id) < objectId) {
            throw new NumberRangeException(objectId, 1L, ID_COUNT.get(id));
        }
    }

    public static void checkStatType(@NonNull StatType statType) throws UnhandledEnumException {
        if(statType.equals(StatType.HP) || statType.equals(StatType.MN)) {
            throw new UnhandledEnumException(statType);
        }
    }

    @NonNull
    public static <T> String toJson(@NonNull T t) {
        return new Gson().toJson(t);
    }

    @Nullable
    public static <T> T fromJson(@NonNull String jsonString, @NonNull Class<?> clazz) {
        return (T) new Gson().fromJson(jsonString, clazz);
    }

    @SuppressWarnings("ConstantConditions")
    public static void unloadObject(@NonNull GameObject gameObject) {
        Id id = gameObject.id.getId();
        long objectId = gameObject.id.getObjectId();
        long objectCount = OBJECT_COUNT.get(id).get(objectId);

        if(objectCount > 1) {
            OBJECT_COUNT.get(id).put(objectId, objectCount - 1);
        } else {
            String jsonString = toJson(gameObject);
            String path = getPath(id, objectId);

            FileManager.save(path, jsonString);

            if(objectCount == 1) {
                OBJECT.get(id).remove(objectId);
                OBJECT_COUNT.get(id).remove(objectId);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void unloadMap(@NonNull MapClass map) {
        String fileName = getMapFileName(map);
        long playerCount = PLAYER_COUNT.get(fileName);

        if(playerCount > 1) {
            PLAYER_COUNT.put(fileName, playerCount - 1);
        } else {
            String jsonString = toJson(map);
            String path = getMapPath(fileName);

            FileManager.save(path, jsonString);

            if(playerCount == 1) {
                MAP.remove(fileName);
                PLAYER_COUNT.remove(fileName);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void discardObject(@NonNull GameObject gameObject) {
        Id id = gameObject.id.getId();
        long objectId = gameObject.id.getObjectId();
        Long objectCount = OBJECT_COUNT.get(id).get(objectId);

        if (objectCount > 1) {
            OBJECT_COUNT.get(id).put(objectId, objectCount - 1);
        } else {
            OBJECT.get(id).remove(objectId);
            OBJECT_COUNT.get(id).remove(objectId);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static <T extends GameObject> T loadObject(@NonNull Id id, long objectId) {
        checkId(id, objectId);

        Long objectCount = OBJECT_COUNT.get(id).get(objectId);

        if(objectCount == null) {
            String path = getPath(id, objectId);
            String jsonString = FileManager.read(path);

            T t = fromJson(jsonString, ID_CLASS.get(id));

            OBJECT.get(id).put(objectId, t);
            OBJECT_COUNT.get(id).put(objectId, 1L);
            return t;
        } else {
            OBJECT_COUNT.get(id).put(objectId, objectCount + 1);
            return (T) OBJECT.get(id).get(objectId);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static MapClass loadMap(int x, int y) {
        String fileName = getMapFileName(x, y);
        Long playerCount = PLAYER_COUNT.get(fileName);

        if(playerCount == null) {
            String path = getMapPath(fileName);
            String jsonString = FileManager.read(path);

            MapClass map = fromJson(jsonString, MapClass.class);

            MAP.put(fileName, map);
            PLAYER_COUNT.put(fileName, 1L);
            return map;
        } else {
            PLAYER_COUNT.put(fileName, playerCount + 1);
            return MAP.get(fileName);
        }
    }

    @NonNull
    public static <T extends GameObject> T getData(@NonNull Id id, long objectId) {
        T t = loadObject(id, objectId);
        unloadObject(t);

        return t;
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

    @NonNull
    private static String getPath(@NonNull Id id, long objectId) {
        return FileManager.DATA_PATH_MAP.get(id) + objectId + ".txt";
    }

    @NonNull
    public static String getMapFileName(int x, int y) {
        String path = x + "-" + y + ".txt";

        if(x >= MIN_MAP_X && x <= MAX_MAP_X && y >= MIN_MAP_X && y <= MAX_MAP_Y) {
            return path;
        }

        throw new ObjectNotFoundException(path);
    }

    @NonNull
    public static String getMapFileName(MapClass map) {
        return getMapFileName(map.getLocation().getX().get(), map.getLocation().getY().get());
    }

    @NonNull
    private static String getMapPath(String fileName) {
        return FileManager.MAP_PATH + "/" + fileName;
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
