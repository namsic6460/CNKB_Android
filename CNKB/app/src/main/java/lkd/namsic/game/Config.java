package lkd.namsic.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import lkd.namsic.game.gameObject.Entity;
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
import lkd.namsic.game.json.NpcDeserializer;
import lkd.namsic.game.json.NpcSerializer;
import lkd.namsic.setting.FileManager;
import lkd.namsic.setting.Logger;

public class Config {

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Npc.class, new NpcSerializer())
            .registerTypeAdapter(Npc.class, new NpcDeserializer())
            .setVersion(1.0)
            .create();

    public static final Map<Id, Long> ID_COUNT = new ConcurrentHashMap<>();
    public static final Map<Id, Class<?>> ID_CLASS = new HashMap<>();

    public static final Map<Id, ConcurrentHashMap<Long, GameObject>> OBJECT = new ConcurrentHashMap<>();
    public static final Map<Id, ConcurrentHashMap<Long, Long>> OBJECT_COUNT = new ConcurrentHashMap<>();
    public static final Map<Id, ConcurrentHashSet<Long>> DELETE_LIST = new ConcurrentHashMap<>();
    public static final Set<Long> DISCARD_LIST = new ConcurrentHashSet<>();

    public static boolean IGNORE_FILE_LOG = false;

    public static final Map<String, MapClass> MAP = new ConcurrentHashMap<>();
    public static final Map<String, Long> PLAYER_COUNT = new ConcurrentHashMap<>();

    private static final String REGEX = "[^A-Za-z-_0-9ㄱ-ㅎㅏ-ㅣ가-힣\\s]|[\n]|[\n]";
    public static final Set<String> NICKNAME_LIST = new ConcurrentHashSet<>();
    public static final Map<Long, String[]> PLAYER_LIST = new ConcurrentHashMap<>();

    public static final Set<Long> SELECTABLE_CHAT_SET = new ConcurrentHashSet<>();

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
    public static final int MIN_MAGIC_LV = 1;
    public static final int MAX_MAGIC_LV = 10;
    public static final long MIN_DELAY_TIME = 500;
    public static final long MAX_DELAY_TIME = 5000;
    public static final long MIN_PAUSE_TIME = 2000;
    public static final long MAX_PAUSE_TIME = 5000;
    public static final int MAX_SPAWN_COUNT = 16;

    public static final double TOTAL_MONEY_LOSE_RANDOM = 0.1;
    public static final double TOTAL_MONEY_LOSE_MIN = 0.05;
    public static final double MONEY_DROP_RANDOM = 0.5;
    public static final double MONEY_DROP_MIN = 0.2;
    public static final int ITEM_DROP = 4;
    public static final double ITEM_DROP_PERCENT = 0.95;

    public static final int MAX_EVADE = 90;

    public static final String INCOMPLETE = "Incomplete";

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

        initialized = true;

        for(Id id : Id.values()) {
            ID_COUNT.put(id, 1L);

            OBJECT.put(id, new ConcurrentHashMap<>());
            OBJECT_COUNT.put(id, new ConcurrentHashMap<>());
            DELETE_LIST.put(id, new ConcurrentHashSet<>());
        }
    }

    public static void loadPlayers() {
        File folder = new File(FileManager.DATA_PATH_MAP.get(Id.PLAYER));
        File[] players = folder.listFiles();

        String json;
        for(File playerFile : players) {
            try {
                json = FileManager.read(playerFile);
                Player player = fromJson(json, Player.class);
                NICKNAME_LIST.add(player.getNickName());
                PLAYER_LIST.put(player.getId().getObjectId(), new String[] {player.getSender(), player.getImage()});
            } catch (Exception e) {
                Logger.e("Config.init", e);
            }
        }
    }

    public static JsonObject createConfig() {
        JsonObject jsonObject = new JsonObject();

        JsonObject idJson = new JsonObject();
        for(Id id : Id.values()) {
            idJson.addProperty(id.toString(), ID_COUNT.get(id));
        }

        jsonObject.add("id", idJson);
        jsonObject.add("selectableChat", gson.toJsonTree(SELECTABLE_CHAT_SET, HashSet.class));

        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    public static void parseConfig(JsonObject jsonObject) {
        JsonObject idObject = jsonObject.getAsJsonObject("id");

        String idName;
        for(Id id : Id.values()) {
            idName = id.toString();

            ID_COUNT.put(id, idObject.getAsJsonPrimitive(idName).getAsLong());
        }

        JsonArray selectableArray = jsonObject.getAsJsonArray("selectableChat");
        Set<Double> selectableChat = gson.fromJson(selectableArray, HashSet.class);

        for(double d : selectableChat) {
            SELECTABLE_CHAT_SET.add((long) d);
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

        Config.unloadObject(t);
        Logger.i("newObject", id.toString() + "-" + objectId);

        return t;
    }

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
        String path;

        if(id.equals(Id.PLAYER)) {
            throw new UnhandledEnumException(id);
        } else {
            path = Config.getPath(id, objectId);
        }

        File file = new File(path);
        if(!file.exists()) {
            throw new ObjectNotFoundException(id, objectId);
        }
    }

    public static void checkStatType(@NonNull StatType statType) throws UnhandledEnumException {
        if(statType.equals(StatType.HP) || statType.equals(StatType.MN)) {
            throw new UnhandledEnumException(statType);
        }
    }

    @NonNull
    public static <T> String toJson(@NonNull T t) {
        return gson.toJson(t);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> T fromJson(@NonNull String jsonString, @NonNull Class<?> c) {
        return (T) gson.fromJson(jsonString, c);
    }

    public synchronized static void unloadObject(@NonNull GameObject gameObject) {
        Id id = gameObject.getId().getId();
        long objectId = gameObject.getId().getObjectId();

        Long objectCount = OBJECT_COUNT.get(id).get(objectId);
        objectCount = objectCount == null ? 0 : objectCount;
        boolean discardFlag = false;

        if(objectCount > 1) {
            OBJECT_COUNT.get(id).put(objectId, objectCount - 1);
        } else {
            String jsonString = toJson(gameObject);

            String path = "";
            if(id.equals(Id.PLAYER)) {
                if(DISCARD_LIST.contains(objectId)) {
                    discardFlag = true;
                } else {
                    Player player = (Player) gameObject;
                    path = getPlayerPath(player.getSender(), player.getImage());
                }
            } else {
                path = getPath(id, objectId);
            }

            if(!discardFlag) {
                if (objectCount == 1) {
                    OBJECT.get(id).remove(objectId);
                    OBJECT_COUNT.get(id).remove(objectId);

                    if (DELETE_LIST.get(id).contains(objectId)) {
                        FileManager.delete(path);
                        return;
                    }
                } else if (objectCount == 0) {
                    if (id.equals(Id.CHAT)) {
                        if (((Chat) gameObject).getName() != null) {
                            SELECTABLE_CHAT_SET.add(objectId);
                        }
                    }

                    if (gameObject instanceof Entity) {
                        Entity entity = (Entity) gameObject;

                        if (entity.getLocation() != null) {
                            MapClass map = Config.loadMap(entity.getLocation());
                            map.addEntity(entity);
                            Config.unloadMap(map);
                        }

                        if (id.equals(Id.PLAYER)) {
                            Player player = (Player) entity;
                            PLAYER_LIST.put(player.getId().getObjectId(), new String[] {player.getSender(), player.getImage()});
                        }
                    }
                }

                FileManager.save(path, jsonString);
            } else if(objectCount == 1) {
                DISCARD_LIST.remove(objectId);
            }
        }
    }


    public synchronized static void discardPlayer(@NonNull Player player) {
        String path = getPlayerPath(player.getSender(), player.getImage());
        String jsonString = FileManager.read(path);

        Player originalPlayer = fromJson(jsonString, Player.class);
        OBJECT.get(Id.PLAYER).put(player.getId().getObjectId(), originalPlayer);
        unloadObject(originalPlayer);
        DISCARD_LIST.add(player.getId().getObjectId());

        Logger.w("discardPlayer", "Player discarded - " + player.getName());
    }

    public synchronized static void unloadMap(@NonNull MapClass map) {
        String fileName = getMapFileName(map);
        Long playerCount = PLAYER_COUNT.get(fileName);
        playerCount = playerCount == null ? 0 : playerCount;

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

    @SuppressWarnings("unchecked")
    @NonNull
    public synchronized static <T extends GameObject> T loadObject(@NonNull Id id, long objectId) {
        if(id.equals(Id.PLAYER)) {
            String[] playerData = PLAYER_LIST.get(objectId);
            return (T) loadPlayer(playerData[0], playerData[1]);
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
    public synchronized static Player loadPlayer(@NonNull String sender, @NonNull String image) {
        String path = getPlayerPath(sender, image);
        String jsonString = FileManager.read(path);

        if(jsonString.equals("")) {
            return null;
        }

        Logger.i("path", path);
        Logger.i("data", jsonString);

        Player player = fromJson(jsonString, Player.class);

        long objectId = player.getId().getObjectId();
        Long objectCount = OBJECT_COUNT.get(Id.PLAYER).get(objectId);

        if(objectCount == null) {
            OBJECT.get(Id.PLAYER).put(objectId, player);
            OBJECT_COUNT.get(Id.PLAYER).put(objectId, 1L);
        } else {
            Logger.i("ExistGet", OBJECT.get(Id.PLAYER).toString());
            player = (Player) OBJECT.get(Id.PLAYER).get(objectId);
            OBJECT_COUNT.get(Id.PLAYER).put(objectId, objectCount + 1);
        }

        if(player.getNickName().equals("u")) {
            throw new RuntimeException();
        }

        return player;
    }

    @NonNull
    public synchronized static MapClass loadMap(int x, int y) {
        String fileName = getMapFileName(x, y);
        Long playerCount = PLAYER_COUNT.get(fileName);

        if(playerCount == null) {
            String path = getMapPath(fileName);
            String jsonString = FileManager.read(path);

            if(jsonString.equals("")) {
                throw new ObjectNotFoundException(path);
            }

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
    public synchronized static MapClass loadMap(Location location) {
        return loadMap(location.getX().get(), location.getY().get());
    }

    @NonNull
    public synchronized static <T extends GameObject> T getData(@NonNull Id id, long objectId) {
        T t = loadObject(id, objectId);
        unloadObject(t);

        return t;
    }

    public synchronized static MapClass getMapData(Location location) {
        return getMapData(location.getX().get(), location.getY().get());
    }

    @NonNull
    public synchronized static MapClass getMapData(int x, int y) {
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
        if(!firstIsBig) {
            return compareMap(map2, map1, true, regardZero);
        }

        Integer value;
        boolean flag;
        for(Map.Entry<T, Integer> entry : map2.entrySet()) {
            value = map1.get(entry.getKey());
            if(value == null) {
                if(regardZero) {
                    value = 0;
                } else {
                    return false;
                }
            }

            flag = value >= entry.getValue();
            if(!flag) {
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
        return FileManager.DATA_PATH_MAP.get(Id.PLAYER) + sender + "-" + image + ".json";
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

    public static String getIncrease(int value) {
        if(value > 0) {
            return "+" + value;
        } else if(value == 0) {
            return "-";
        } else {
            return Integer.toString(value);
        }
    }

    public static String getIncrease(long value) {
        if(value > 0) {
            return "+" + value;
        } else if(value == 0) {
            return "-";
        } else {
            return Long.toString(value);
        }
    }

    public static String errorString(@NonNull Throwable throwable) {
        StringBuilder builder = new StringBuilder(throwable.getClass().getName());
        builder.append(": ");
        builder.append(throwable.getMessage());
        for(StackTraceElement element : throwable.getStackTrace()) {
            builder.append("\n\tat ");
            builder.append(element.toString());
        }

        return builder.toString();
    }

    public static String getRegex(@NonNull String string, @NonNull String replacement) {
        return string.replaceAll(REGEX, replacement)
                .replaceAll("[ ]{2,}", " ")
                .trim();
    }

}
