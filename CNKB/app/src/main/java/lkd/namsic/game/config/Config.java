package lkd.namsic.game.config;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import lkd.namsic.game.gameObject.GameMap;
import lkd.namsic.game.gameObject.Monster;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.gameObject.Quest;
import lkd.namsic.game.gameObject.Research;
import lkd.namsic.game.gameObject.Skill;
import lkd.namsic.game.gameObject.Use;
import lkd.namsic.game.json.UseAdapter;
import lkd.namsic.game.json.LocationAdapter;
import lkd.namsic.game.json.NpcAdapter;
import lkd.namsic.setting.FileManager;
import lkd.namsic.setting.Logger;

public class Config {

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Npc.class, new NpcAdapter())
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .registerTypeAdapter(Use.class, new UseAdapter())
            .setVersion(1.0)
            .create();

    public static final Map<Id, Long> ID_COUNT = new ConcurrentHashMap<>();
    public static final Map<Id, Class<?>> ID_CLASS = new HashMap<>();

    public static final Map<Id, ConcurrentHashMap<Long, GameObject>> OBJECT = new ConcurrentHashMap<>();
    public static final Map<Id, ConcurrentHashMap<Long, Long>> OBJECT_COUNT = new ConcurrentHashMap<>();
    public static final Map<Id, ConcurrentHashSet<Long>> DELETE_LIST = new ConcurrentHashMap<>();
    public static final Set<Long> DISCARD_LIST = new ConcurrentHashSet<>();

    public static boolean IGNORE_FILE_LOG = false;

    public static final Map<String, GameMap> MAP = new ConcurrentHashMap<>();
    public static final Map<String, Long> PLAYER_COUNT = new ConcurrentHashMap<>();

    private static final String REGEX = "[^A-Za-z-_0-9ㄱ-ㅎㅏ-ㅣ가-힣\\s]|[\n]|[\r]";
    public static final Map<String, Long> PLAYER_ID = new ConcurrentHashMap<>();
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
    public static final int MIN_AI_INCREASE = 0;
    public static final int MIN_SP = 0;
    public static final int MAX_SP = 100;
    public static final int MIN_MAGIC_LV = 1;
    public static final int MAX_MAGIC_LV = 10;
    public static final long MIN_DELAY_TIME = 500;
    public static final long MAX_DELAY_TIME = 5000;
//    public static final long MIN_PAUSE_TIME = 2000;     //REAL TIME
    public static final long MIN_PAUSE_TIME = 500;      //DEBUG
    public static final long MAX_PAUSE_TIME = 5000;
    public static final int MAX_SPAWN_COUNT = 16;

    public static final double TOTAL_MONEY_LOSE_RANDOM = 0.1;
    public static final double TOTAL_MONEY_LOSE_MIN = 0.05;
    public static final double MONEY_DROP_RANDOM = 0.5;
    public static final double MONEY_DROP_MIN = 0.2;
    public static final int ITEM_DROP_COUNT = 4;
    public static final int MAX_ITEM_DROP_COUNT = 5;
    public static final double ITEM_DROP_LOSE_PERCENT = 0.95;

    public static final int RECOGNIZE_DISTANCE = 16;
    public static final double ATTACKED_PERCENT = 0.5;
    public static final double ATTACKED_PERCENT_INCREASE = 0.025;
    public static final double MAX_ATTACKED_PERCENT = 0.9;

    public static final int MAX_AGI = 400;
    public static final double CRIT_PER_AGI = 0.0025;
    public static final int MAX_EVADE = 80;
    
    public static final int FISH_DELAY_TIME = 3000;
    public static final long FISH_DELAY_TIME_OFFSET = 5000;
    public static final long FISH_WAIT_TIME = 5000;
    public static final long FIGHT_WAIT_TIME = 30000;
    public static final int ADVENTURE_COUNT = 5;
    public static final long ADVENTURE_WAIT_TIME = 30000;
    public static final int EXPLORE_HARD_SUCCESS_PERCENT = 20;
    public static final int APPRAISE_LIMIT = 999;

    public static final String INCOMPLETE = "Incomplete";
    public static final String[] TIERS = new String[] { "하", "중", "상" };

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

        for(Id id : Id.values()) {
            ID_COUNT.put(id, 1L);

            OBJECT.put(id, new ConcurrentHashMap<>());
            OBJECT_COUNT.put(id, new ConcurrentHashMap<>());
            DELETE_LIST.put(id, new ConcurrentHashSet<>());
        }
    }

    public static void loadPlayers() {
        File folder = new File(Objects.requireNonNull(FileManager.DATA_PATH_MAP.get(Id.PLAYER)));
        File[] players = Objects.requireNonNull(folder.listFiles());

        String json;
        for(File playerFile : players) {
            try {
                json = FileManager.read(playerFile);
                Player player = fromJson(json, Player.class);

                long objectId = player.getId().getObjectId();
                String nickName = player.getNickName();

                PLAYER_ID.put(nickName, objectId);
                PLAYER_LIST.put(objectId, new String[] {player.getSender(), player.getImage()});
            } catch (Exception e) {
                Logger.e("Config.init", e);
            }
        }

        ID_COUNT.put(Id.PLAYER, (long) players.length);
    }

    @NonNull
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
    public static void parseConfig(@NonNull JsonObject jsonObject) {
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

    public static void save() {
        try {
            for(Id id : Id.values()) {
                List<GameObject> objectCopy = new ArrayList<>(OBJECT.get(id).values());
                for (GameObject gameObject : objectCopy) {
                    unloadObject(gameObject);
                }

                List<GameMap> mapCopy = new ArrayList<>(MAP.values());
                for(GameMap map : mapCopy) {
                    unloadMap(map);
                }
            }

            FileManager.save(FileManager.CONFIG_PATH, createConfig().toString());
        } catch (Exception e) {
            Logger.e("onTerminate", e);
        }
    }

    @NonNull
    public static <T extends GameObject> T newObject(@NonNull T t) {
        Id id = t.getId().getId();
        long objectId = ID_COUNT.get(id);

        t = t.newObject();

        t.getId().setObjectId(objectId);
        ID_COUNT.put(id, objectId + 1);

        unloadObject(t);
        Logger.i("newObject", id.toString() + "-" + objectId);

        return t;
    }

    public static void deleteAiEntity(@NonNull AiEntity entity) {
        Id id = entity.getId().getId();
        long objectId = entity.getId().getObjectId();

        long count = OBJECT_COUNT.get(id).getOrDefault(objectId, 0L);
        if(count == 0) {
            FileManager.delete(getPath(id, objectId));
            Logger.w("deleteAiEntity", id + "-" + objectId + " has 0 count");
        } else {
            Logger.i("deleteAiEntity", id + "-" + objectId + " added");
            DELETE_LIST.get(entity.getId().getId()).add(entity.getId().getObjectId());

            for(long equipId : entity.getEquipInventory()) {
                DELETE_LIST.get(Id.EQUIPMENT).add(equipId);
            }
        }
    }

    public static void checkId(@NonNull Id id, long objectId) throws NumberRangeException {
        String path = Config.getPath(id, objectId);

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
        try {
            return (T) gson.fromJson(jsonString, c);
        } catch (Exception e) {
            Log.e("namsic!", jsonString);
            throw e;
        }
    }

    @NonNull
    public static <T extends Serializable> String serialize(T t) {
        byte[] serialized;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(t);
                serialized = baos.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(serialized);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T extends Serializable> T deserialize(String byteStr) {
        byte[] serialized = Base64.getDecoder().decode(byteStr);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(serialized)) {
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object object = ois.readObject();

            return (T) object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

                    Logger.i("FileManager", "Object unloaded - [" + id + ", " + objectId + "]");

                    if (DELETE_LIST.get(id).contains(objectId)) {
                        FileManager.delete(path);
                        DELETE_LIST.get(id).remove(objectId);
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
                            GameMap map = Config.loadMap(entity.getLocation());
                            map.addEntity(entity);
                            Config.unloadMap(map);
                        }

                        if (id.equals(Id.PLAYER)) {
                            Player player = (Player) entity;
                            PLAYER_LIST.put(player.getId().getObjectId(), new String[] {player.getSender(), player.getImage()});
                        }
                    }

                    Logger.i("FileManager", "New object unloaded");
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

    public synchronized static void unloadMap(@NonNull GameMap map) {
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
            return (T) Objects.requireNonNull(loadPlayer(playerData[0], playerData[1]));
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

            try {
                Id.checkEntityId(id);
                ((Entity) t).revalidateBuff();
            } catch (UnhandledEnumException ignore) {}

            OBJECT.get(id).put(objectId, t);
            OBJECT_COUNT.get(id).put(objectId, 1L);
            return t;
        } else {
            OBJECT_COUNT.get(id).put(objectId, objectCount + 1);
            Logger.i("loadObject(" + objectCount + ")", objectId + " - " + objectId);

            T t = (T) Objects.requireNonNull(OBJECT.get(id).get(objectId));

            try {
                Id.checkEntityId(id);
                ((Entity) t).revalidateBuff();
            } catch (UnhandledEnumException ignore) {}

            return t;
        }
    }

    @NonNull
    public synchronized static Player loadPlayer(@NonNull String sender, @NonNull String image) {
        String path = getPlayerPath(sender, image);
        String jsonString = FileManager.read(path);

        if(jsonString.equals("")) {
            throw new ObjectNotFoundException(sender);
        }

        Player player = fromJson(jsonString, Player.class);
        player.checkTime();

        long objectId = player.getId().getObjectId();
        Long objectCount = OBJECT_COUNT.get(Id.PLAYER).get(objectId);

        if(objectCount == null) {
            OBJECT.get(Id.PLAYER).put(objectId, player);
            OBJECT_COUNT.get(Id.PLAYER).put(objectId, 1L);
        } else {
            Logger.i("loadPlayer(" + objectCount + ")", sender);
            player = (Player) Objects.requireNonNull(OBJECT.get(Id.PLAYER).get(objectId));
            OBJECT_COUNT.get(Id.PLAYER).put(objectId, objectCount + 1);
        }

        player.revalidateBuff();
        return player;
    }

    @NonNull
    public synchronized static GameMap loadMap(int x, int y) {
        String fileName = getMapFileName(x, y);
        Long playerCount = PLAYER_COUNT.get(fileName);

        if(playerCount == null) {
            String path = getMapPath(fileName);
            String jsonString = FileManager.read(path);

            if(jsonString.equals("")) {
                throw new ObjectNotFoundException(path);
            }

            GameMap map = fromJson(jsonString, GameMap.class);

            MAP.put(fileName, map);
            PLAYER_COUNT.put(fileName, 1L);
            return map;
        } else {
            PLAYER_COUNT.put(fileName, playerCount + 1);
            return MAP.get(fileName);
        }
    }

    @NonNull
    public synchronized static GameMap loadMap(Location location) {
        return loadMap(location.getX().get(), location.getY().get());
    }

    @NonNull
    public synchronized static <T extends GameObject> T getData(@NonNull Id id, long objectId) {
        T t = loadObject(id, objectId);
        unloadObject(t);

        return t;
    }

    public synchronized static GameMap getMapData(Location location) {
        return getMapData(location.getX().get(), location.getY().get());
    }

    @NonNull
    public synchronized static GameMap getMapData(int x, int y) {
        GameMap map = loadMap(x, y);
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
        if(id.equals(Id.PLAYER)) {
            String[] playerData = PLAYER_LIST.get(objectId);
            return getPlayerPath(playerData[0], playerData[1]);
        } else {
            return FileManager.DATA_PATH_MAP.get(id) + objectId + ".json";
        }
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
    public static String getMapFileName(GameMap map) {
        return getMapFileName(map.getLocation().getX().get(), map.getLocation().getY().get());
    }

    @NonNull
    private static String getMapPath(String fileName) {
        return FileManager.MAP_PATH + "/" + fileName;
    }

    //AlphaDo 님의 체력바 소스를 참고했음을 알립니다
    @NonNull
    public static String getBar(int value, int maxValue) {
        double percent = 10.0 * value / maxValue;
        double dec = percent % 1;
        int filled = (int) percent;

        StringBuilder output = new StringBuilder("[");

        for(int i = 0; i < filled; i++) {
            output.append(Emoji.FILLED_BAR);
        }

        output.append(Emoji.HALF_BAR[(int) Math.round(dec * 8)]);

        for(int i = 9; i > filled; i--) {
            output.append("  ");
        }

        return output.toString() + "] (" + value + "/" + maxValue + ")";
    }

    @NonNull
    public static String getIncrease(int value) {
        if(value > 0) {
            return "+" + value;
        } else if(value == 0) {
            return "-";
        } else {
            return Integer.toString(value);
        }
    }

    @NonNull
    public static String getIncrease(long value) {
        if(value > 0) {
            return "+" + value;
        } else if(value == 0) {
            return "-";
        } else {
            return Long.toString(value);
        }
    }

    public static int giveToken(long startItemId, double[][] tokenList, int index, @NonNull Player self) {
        double random = Math.random();

        double weight = 0D;
        double percent;
        double[] percents = tokenList[index];

        for(int i = 0; i < 3; i++) {
            percent = percents[i];

            if(percent == 0) {
                continue;
            }

            if(random < percent + weight) {
                startItemId += i;
                self.addItem(startItemId, 1, false);

                return i;
            } else {
                weight += random;
            }
        }

        return -1;
    }

    @NonNull
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

    @NonNull
    public static String getRegex(@NonNull String string, @NonNull String replacement) {
        return string.replaceAll(REGEX, replacement)
                .replaceAll("[ ]{2,}", " ")
                .trim();
    }

}
