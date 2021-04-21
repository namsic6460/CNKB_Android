package lkd.namsic.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.exception.UnhandledEnumException;
import lkd.namsic.game.gameObject.Achieve;
import lkd.namsic.game.gameObject.AiEntity;
import lkd.namsic.game.gameObject.Boss;
import lkd.namsic.game.gameObject.Chat;
import lkd.namsic.game.gameObject.Equipment;
import lkd.namsic.game.gameObject.GameObject;
import lkd.namsic.game.gameObject.Item;
import lkd.namsic.game.gameObject.MapClass;
import lkd.namsic.game.gameObject.Monster;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.gameObject.Quest;
import lkd.namsic.game.gameObject.Research;
import lkd.namsic.game.gameObject.Skill;
import lkd.namsic.setting.FileManager;
import lkd.namsic.setting.Logger;

public class Config {

    public static final Map<Id, Long> ID_COUNT = new ConcurrentHashMap<>();
    public static final Map<Id, Class<?>> ID_CLASS = new HashMap<>();

    public static final Map<Id, ConcurrentHashMap<Long, GameObject>> OBJECT = new ConcurrentHashMap<>();
    public static final Map<Id, ConcurrentHashMap<Long, Long>> OBJECT_COUNT = new ConcurrentHashMap<>();
    public static final Map<Id, ConcurrentHashSet<Long>> DELETE_LIST = new ConcurrentHashMap<>();

    public static final Map<String, MapClass> MAP = new ConcurrentHashMap<>();
    public static final Map<String, Long> PLAYER_COUNT = new ConcurrentHashMap<>();

    public static final double MONEY_BOOST = 1;
    public static final double EXP_BOOST = 1;

    public static final int MIN_MAP_X = 0;
    public static final int MAX_MAP_X = 10;
    public static final int MIN_MAP_Y = 0;
    public static final int MAX_MAP_Y = 10;
    public static final int MIN_FIELD_X = 1;
    public static final int MAX_FIELD_X = 64;
    public static final int MIN_FIELD_Y = 1;
    public static final int MAX_FIELD_Y = 64;

    public static final int MIN_HANDLE_LV = 1;
    public static final int MAX_HANDLE_LV = 13;
    public static final int MIN_REINFORCE_COUNT = 0;
    public static final int MAX_REINFORCE_COUNT = 15;
    public static final double REINFORCE_FLOOR_MULTIPLE = 0.025;
    public static final int MIN_LV = 1;
    public static final int MAX_LV = 999;
    public static final int MIN_SP = 0;
    public static final int MAX_SP = 100;
    public static final long MIN_PAUSE_TIME = 1000;
    public static final long MAX_PAUSE_TIME = 5000;

    public static final double TOTAL_MONEY_LOSE_RANDOM = 0.1;
    public static final double TOTAL_MONEY_LOSE_MIN = 0.05;
    public static final double MONEY_DROP_RANDOM = 0.5;
    public static final double MONEY_DROP_MIN = 0.2;
    public static final int ITEM_DROP = 4;
    public static final double ITEM_DROP_PERCENT = 0.95;

    public static final double BASE_INCREASE_PERCENT = 0.05;

    public static final int MAX_EVADE = 95;

    private static boolean initialized = false;
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

        if(initialized) {
            Logger.i("Config.init", "Returned");
            return;
        }

        for(Id id : Id.values()) {
            ID_COUNT.put(id, 1L);

            OBJECT.put(id, new ConcurrentHashMap<>());
            OBJECT_COUNT.put(id, new ConcurrentHashMap<>());
            DELETE_LIST.put(id, new ConcurrentHashSet<>());
        }

        initialized = true;
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

    public static <T extends GameObject> T newObject(@NonNull T t) {
        Id id = t.getId().getId();
        long objectId = ID_COUNT.get(id);

        t = t.newObject();
        t.getId().setObjectId(objectId);
        ID_COUNT.put(id, objectId + 1);

        return t;
    }

    @SuppressWarnings("ConstantConditions")
    public static void deleteAiEntity(AiEntity entity) {
        Id id = entity.getId().getId();
        long objectId = entity.getId().getObjectId();

        long count = OBJECT_COUNT.get(id).get(objectId);
        if(count == 0) {
            FileManager.delete(getPath(id, objectId));
            Logger.w("deleteAiEntity", id + "-" + objectId + " has 0 count");
        } else {
            DELETE_LIST.get(entity.getId().getId()).add(entity.getId().getObjectId());
        }
    }

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

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> T fromJson(@NonNull String jsonString, @NonNull Class<?> c) {
        return (T) new Gson().fromJson(jsonString, c);
    }

    public static void unloadObject(@NonNull GameObject gameObject) {
        Id id = gameObject.getId().getId();
        long objectId = gameObject.getId().getObjectId();

        Long objectCount = OBJECT_COUNT.get(id).get(objectId);
        objectCount = objectCount == null ? 0 : objectCount;

        if(objectCount > 1) {
            OBJECT_COUNT.get(id).put(objectId, objectCount - 1);
        } else {
            String jsonString = toJson(gameObject);

            String path;
            if(id.equals(Id.PLAYER)) {
                Player player = (Player) gameObject;
                path = getPlayerPath(player.getSender(), player.getImage());
            } else {
                path = getPath(id, objectId);
            }

            if(objectCount == 1) {
                OBJECT.get(id).remove(objectId);
                OBJECT_COUNT.get(id).remove(objectId);

                if(DELETE_LIST.get(id).contains(objectId)) {
                    FileManager.delete(path);
                    return;
                }
            }

            FileManager.save(path, jsonString);
        }
    }

    public static void unloadMap(@NonNull MapClass map) {
        String fileName = getMapFileName(map);
        long playerCount = PLAYER_COUNT.get(fileName);

        if(playerCount > 1) {
            PLAYER_COUNT.put(fileName, playerCount - 1);
        } else {
            String jsonString = toJson(map);
            String path = getMapPath(fileName);

            if(playerCount == 1) {
                MAP.remove(fileName);
                PLAYER_COUNT.remove(fileName);
            }

            FileManager.save(path, jsonString);
        }
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @NonNull
    public static <T extends GameObject> T loadObject(@NonNull Id id, long objectId) {
        if(id.equals(Id.PLAYER)) {
            throw new UnhandledEnumException(id);
        }

        checkId(id, objectId);

        Long objectCount = OBJECT_COUNT.get(id).get(objectId);

        if(objectCount == null) {
            String path = getPath(id, objectId);
            String jsonString = FileManager.read(path);

            if(jsonString.equals("")) {
                throw new ObjectNotFoundException(id, objectId);
            }

            T t = fromJson(jsonString, ID_CLASS.get(id));

            OBJECT.get(id).put(objectId, t);
            OBJECT_COUNT.get(id).put(objectId, 1L);
            return t;
        } else {
            OBJECT_COUNT.get(id).put(objectId, objectCount + 1);
            return (T) OBJECT.get(id).get(objectId);
        }
    }

    @Nullable
    public static Player loadPlayer(@NonNull String sender, @NonNull String image) {
        String path = getPlayerPath(sender, image);
        String jsonString = FileManager.read(path);

        if(jsonString.equals("")) {
            return null;
        }

        Player player = fromJson(jsonString, Player.class);
        long objectId = player.getId().getObjectId();
        Long objectCount = OBJECT_COUNT.get(Id.PLAYER).get(objectId);

        if(objectCount == null) {
            OBJECT.get(Id.PLAYER).put(objectId, player);
            OBJECT_COUNT.get(Id.PLAYER).put(objectId, 1L);
        } else {
            OBJECT_COUNT.get(Id.PLAYER).put(objectId, objectCount + 1);
        }

        return player;
    }

    @NonNull
    public static MapClass loadMap(int x, int y) {
        String fileName = getMapFileName(x, y);
        Long playerCount = PLAYER_COUNT.get(fileName);

        if(playerCount == null) {
            String path = getMapPath(fileName);
            String jsonString = FileManager.read(path);

            if(jsonString.equals("")) {
                throw new ObjectNotFoundException(path);
            }

            MapClass map = fromJson(jsonString, MapClass.class);
            map.spawnMonster();

            MAP.put(fileName, map);
            PLAYER_COUNT.put(fileName, 1L);
            return map;
        } else {
            PLAYER_COUNT.put(fileName, playerCount + 1);
            return MAP.get(fileName);
        }
    }

    @NonNull
    public static MapClass loadMap(Location location) {
        return loadMap(location.getX().get(), location.getY().get());
    }

    @NonNull
    public static <T extends GameObject> T getData(@NonNull Id id, long objectId) {
        T t = loadObject(id, objectId);
        unloadObject(t);

        return t;
    }

    public static MapClass getMapData(Location location) {
        return getMapData(location.getX().get(), location.getY().get());
    }

    @NonNull
    public static MapClass getMapData(int x, int y) {
        MapClass map = loadMap(x, y);
        unloadMap(map);

        return map;
    }

    @NonNull
    public static String mapToString(@NonNull Map<?, ?> map) {
        if(map instanceof AbstractMap) {
            return map.toString();
        } else {
            return new HashMap<>(map).toString();
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

    public static String getDisplayPercent(double percent) {
        return (Math.floor(percent * 10000) / 100) + "%";
    }

    @NonNull
    private static String getPath(@NonNull Id id, long objectId) {
        return FileManager.DATA_PATH_MAP.get(id) + objectId + ".json";
    }

    @NonNull
    private static String getPlayerPath(@NonNull String sender, @NonNull String image) {
        return sender + "-" + image + ".json";
    }

    @NonNull
    public static String getMapFileName(int x, int y) {
        String path = x + "-" + y + ".json";

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