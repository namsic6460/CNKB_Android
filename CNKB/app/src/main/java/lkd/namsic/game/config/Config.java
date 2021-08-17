package lkd.namsic.game.config;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.IdClass;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.exception.UnhandledEnumException;
import lkd.namsic.game.json.ChatLimitAdapter;
import lkd.namsic.game.json.LocationAdapter;
import lkd.namsic.game.json.NpcAdapter;
import lkd.namsic.game.object.Achieve;
import lkd.namsic.game.object.AiEntity;
import lkd.namsic.game.object.Boss;
import lkd.namsic.game.object.Chat;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.GameObject;
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.Monster;
import lkd.namsic.game.object.Npc;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.object.Quest;
import lkd.namsic.game.object.Research;
import lkd.namsic.game.object.Shop;
import lkd.namsic.game.object.Skill;
import lkd.namsic.setting.FileManager;
import lkd.namsic.setting.Logger;

public class Config {

    public static final double VERSION = 1.61;

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Npc.class, new NpcAdapter())
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .registerTypeAdapter(ChatLimit.class, new ChatLimitAdapter())
            .setVersion(VERSION)
            .create();

    public static final Map<Player, String> RUNNING_COMMAND = new ConcurrentHashMap<>();

    public static final Map<Id, Long> ID_COUNT = new ConcurrentHashMap<>();
    public static final Map<Id, Class<?>> ID_CLASS = new HashMap<>();

    public static final Map<Id, ConcurrentHashMap<Long, GameObject>> OBJECT = new ConcurrentHashMap<>();
    public static final Map<Id, ConcurrentHashMap<Long, Long>> OBJECT_COUNT = new ConcurrentHashMap<>();
    public static final Map<Id, ConcurrentHashSet<Long>> DELETE_LIST = new ConcurrentHashMap<>();
    public static final Set<Long> DISCARD_LIST = new ConcurrentHashSet<>();

    public static boolean IGNORE_FILE_LOG = false;

    public static final Map<String, GameMap> MAP = new ConcurrentHashMap<>();
    public static final Map<String, Long> MAP_COUNT = new ConcurrentHashMap<>();

    private static final String REGEX = "[^A-Za-z-_0-9ㄱ-ㅎㅏ-ㅣ가-힣\\s]|[\n]|[\r]";
    public static final List<String> FORBIDDEN_NICKNAME = Arrays.asList("아이템", "item", "장비", "equip", "시발", "애미", "애비",
            "느금", "느금마", "지랄", "염병", "옘병", "tlqkf", "ㄴㄱㅁ", "앰이", "보지", "자지", "섹스", "발기", "왕고추", "느금",
            "유미없음", "창년", "창녀", "창남", "몸팔이", "니애미", "니애비", "씨발", "fuck", "씨빨", "좆", "개새");
    public static final Map<String, Long> PLAYER_ID = new ConcurrentHashMap<>();
    public static final Map<Long, String[]> PLAYER_LIST = new ConcurrentHashMap<>();
    public static final Map<String, Integer> PLAYER_LV_RANK = new ConcurrentHashMap<>();

    public static final Set<Long> SELECTABLE_CHAT_SET = new HashSet<>();

    public static final double MONEY_BOOST = 1;
    public static final double EXP_BOOST = 1;

    public static final int MAX_COUNT_PER_PAGE = 30;

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
    public static final int MAX_REINFORCE_COUNT = 15;
    public static final int MIN_LV = 1;
    public static final int MAX_LV = 999;
    public static final int MIN_RANK_LV = 10;
    public static final int MIN_AI_INCREASE = 0;
    public static final int MIN_SP = 0;
    public static final int MIN_MAGIC_LV = 1;
    public static final int MAX_MAGIC_LV = 10;
    public static final long MIN_DELAY_TIME = 500;
    public static final long MAX_DELAY_TIME = 5000;
    public static final long MIN_PAUSE_TIME = 2000;
    public static final long MAX_PAUSE_TIME = 5000;
    public static final int MAX_SPAWN_COUNT = 16;

    public static final double REINFORCE_EFFICIENCY = 0.18;
    public static final double REINFORCE_EFFICIENCY_PER_HANDLE_LV = 0.01;
    public static final long REINFORCE_BASE_COST = 1000;
    public static final long REINFORCE_COST_PER_HANDLE_LV = 500;
    public static final double REINFORCE_COST_MULTIPLIER = 0.12;
    public static final long REINFORCE_DELAY_TIME = 2000;

    public static final double TOTAL_MONEY_LOSE_RANDOM = 0.1;
    public static final double TOTAL_MONEY_LOSE_MIN = 0.05;
    public static final double MONEY_DROP_RANDOM = 0.5;
    public static final double MONEY_DROP_MIN = 0.2;
    public static final int ITEM_DROP_COUNT = 4;
    public static final int MAX_ITEM_DROP_COUNT = 5;
    public static final double ITEM_DROP_LOSE_PERCENT = 0.95;

    public static final long PREVENT_FIGHT_TIME = 30000L;
    public static final int RECOGNIZE_DISTANCE = 16;
    public static final double ATTACKED_PERCENT = 0.3;
    public static final double ATTACKED_PERCENT_INCREASE = 0.025;
    public static final double MAX_ATTACKED_PERCENT = 0.9;

    public static final int MAX_AGI = 400;
    public static final double CRIT_PER_AGI = 0.0025;
    public static final int MAX_EVADE = 80;
    
    public static final int FISH_DELAY_TIME = 3000;
    public static final long FISH_DELAY_TIME_OFFSET = 3000;
    public static final long FISH_WAIT_TIME = 5000;
    public static final long FIGHT_WAIT_TIME = 30000;
    public static final int ADVENTURE_COUNT = 5;
    public static final int ADVENTURE_DELAY_TIME = 5000;
    public static final long ADVENTURE_WAIT_TIME = 30000;
    public static final int EXPLORE_HARD_SUCCESS_PERCENT = 20;
    public static final int APPRAISE_LIMIT = 999;
    public static final long SHOPPING_WAIT_TIME = 120000;

    public static final String SPLIT_BAR = "------------------------------------------";
    public static final String HARD_SPLIT_BAR = "==========================================";
    public static final String INCOMPLETE = "Incomplete";
    public static final String[] TIERS = new String[] { "하", "중", "상" };
    public static final ItemList[] GEMS = { ItemList.QUARTZ, ItemList.GOLD, ItemList.WHITE_GOLD, ItemList.GARNET,
            ItemList.AMETHYST, ItemList.AQUAMARINE, ItemList.DIAMOND, ItemList.EMERALD, ItemList.PEARL, ItemList.RUBY,
            ItemList.PERIDOT, ItemList.SAPPHIRE, ItemList.OPAL, ItemList.TOPAZ, ItemList.TURQUOISE };

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
        ID_CLASS.put(Id.SHOP, Shop.class);
        ID_CLASS.put(Id.SKILL, Skill.class);

        for(Id id : Id.values()) {
            ID_COUNT.put(id, ID_COUNT.getOrDefault(id, 1L));

            OBJECT.put(id, new ConcurrentHashMap<>());
            OBJECT_COUNT.put(id, new ConcurrentHashMap<>());
            DELETE_LIST.put(id, new ConcurrentHashSet<>());
        }
    }

    public static void loadPlayers() {
        File folder = new File(Objects.requireNonNull(FileManager.DATA_PATH_MAP.get(Id.PLAYER)));
        File[] players = Objects.requireNonNull(folder.listFiles());

        String json;
        for(File file : players) {
            if(file.getName().endsWith(".zip")) {
                continue;
            }

            try {
                Config.IGNORE_FILE_LOG = true;

                json = FileManager.read(file);
                Player player = fromJson(json, Player.class);

                long objectId = player.getId().getObjectId();

                PLAYER_ID.put(player.getNickName(), objectId);
                PLAYER_LIST.put(objectId, new String[] {player.getSender(), player.getImage()});

                if(objectId != 1 && player.getLv().get() >= MIN_RANK_LV) {
                    PLAYER_LV_RANK.put(player.getName(), player.getLv().get());
                }
            } catch (Exception e) {
                Logger.e("Config.init(" + file.getName() + ")", e);
            } finally {
                Config.IGNORE_FILE_LOG = false;
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
        jsonObject.add("selectableChat", gson.toJsonTree(SELECTABLE_CHAT_SET));

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

        JsonArray selectableChatArray = jsonObject.getAsJsonArray("selectableChat");
        Set<Double> selectableChatSet = gson.fromJson(selectableChatArray, HashSet.class);
        for(double selectableChatId : selectableChatSet) {
            SELECTABLE_CHAT_SET.add((long) selectableChatId);
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

            saveConfig();
        } catch (Exception e) {
            Logger.e("onTerminate", e);
        }
    }

    public static void saveConfig() {
        FileManager.save(FileManager.CONFIG_PATH, createConfig().toString());
    }

    @NonNull
    public static <T extends GameObject & Cloneable> T newObject(@NonNull T t, boolean save) {
        Id id = t.getId().getId();
        long objectId = ID_COUNT.get(id);

        t = t.newObject();

        t.getId().setObjectId(objectId);
        ID_COUNT.put(id, objectId + 1);

        Logger.i("newObject", id.toString() + "-" + objectId);

        if(save) {
            saveConfig();
        }

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
            Logger.e("fromJson", e);
            throw e;
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

                    Logger.i("FileManager", "New object unloaded - [" + id + ", " + objectId + "]");
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
        Long mapCount = MAP_COUNT.get(fileName);
        mapCount = mapCount == null ? 0 : mapCount;

        if(mapCount > 1) {
            MAP_COUNT.put(fileName, mapCount - 1);
        } else {
            String jsonString = toJson(map);
            String path = getMapPath(fileName);

            if(mapCount == 1) {
                MAP.remove(fileName);
                MAP_COUNT.remove(fileName);
            }

            FileManager.save(path, jsonString);
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public synchronized static <T extends GameObject> T loadObject(@NonNull Id id, long objectId) {
        T t = null;

        if(id.equals(Id.PLAYER)) {
            String[] playerData = PLAYER_LIST.get(objectId);

            try {
                t = (T) Objects.requireNonNull(loadPlayer(playerData[0], playerData[1]));
            } catch (NullPointerException e) {
                throw new NullPointerException(e.getMessage() + "\n" + objectId + " " + PLAYER_LIST.toString());
            }
        }

        File file = new File(getPath(id, objectId));
        if(!file.exists()) {
            throw new ObjectNotFoundException(id, objectId);
        }

        Long objectCount = OBJECT_COUNT.get(id).get(objectId);
        if(objectCount == null) {
            if(t == null) {
                String jsonString;

                try {
                    jsonString = FileManager.read(file);
                } catch (Exception e) {
                    Logger.e("loadObject", e);
                    throw new RuntimeException(e);
                }

                if (jsonString.equals("")) {
                    throw new ObjectNotFoundException(id, objectId);
                }

                t = fromJson(jsonString, ID_CLASS.get(id));
            }

            OBJECT.get(id).put(objectId, t);
            OBJECT_COUNT.get(id).put(objectId, 1L);

            try {
                Id.checkEntityId(id);

                if(!id.equals(Id.PLAYER)) {
                    checkUpdate((Entity) t);
                }
            } catch (UnhandledEnumException ignore) {}
        } else {
            OBJECT_COUNT.get(id).put(objectId, objectCount + 1);

            if(t == null) {
                t = (T) Objects.requireNonNull(OBJECT.get(id).get(objectId));
                Logger.i("loadObject(" + objectCount + ")", id + " - " + objectId);
            }

            try {
                Id.checkEntityId(id);
                ((Entity) t).revalidateBuff();
            } catch (UnhandledEnumException ignore) {}
        }

        return t;
    }

    @NonNull
    public synchronized static Player loadPlayer(@NonNull String sender, @NonNull String image) {
        return loadPlayer(sender, image, null, false, false);
    }

    @NonNull
    public synchronized static Player loadPlayer(@NonNull String sender, @NonNull String image,
                                                 @Nullable String room, boolean isGroupChat, boolean count) {
        String path = getPlayerPath(sender, image);
        String jsonString = FileManager.read(path);

        if(jsonString.equals("")) {
            throw new ObjectNotFoundException(sender);
        }

        Player player = fromJson(jsonString, Player.class);
        long objectId = player.getId().getObjectId();
        Long objectCount = OBJECT_COUNT.get(Id.PLAYER).get(objectId);

        if(count) {
            if(objectCount == null) {
                OBJECT.get(Id.PLAYER).put(objectId, player);
                OBJECT_COUNT.get(Id.PLAYER).put(objectId, 1L);
            } else {
                OBJECT_COUNT.get(Id.PLAYER).put(objectId, objectCount + 1);
            }
        }

        if(objectCount != null) {
            player = Objects.requireNonNull((Player) OBJECT.get(Id.PLAYER).get(objectId));
            Logger.i("loadPlayer(" + objectCount + ")", sender);
        }

        if(room != null) {
            player.setRecentRoom(room);
            player.setGroup(isGroupChat);
        }

        checkUpdate(player);

        return player;
    }

    @NonNull
    public synchronized static GameMap loadMap(int x, int y) {
        String fileName = getMapFileName(x, y);
        Long mapCount = MAP_COUNT.get(fileName);

        if(mapCount == null) {
            String path = getMapPath(fileName);
            String jsonString = FileManager.read(path);

            if(jsonString.equals("")) {
                throw new ObjectNotFoundException(path);
            }

            GameMap map = fromJson(jsonString, GameMap.class);

            MAP.put(fileName, map);
            MAP_COUNT.put(fileName, 1L);

            for(long monsterId : new HashSet<>(map.getEntity(Id.MONSTER))) {
                if(FileManager.read(getPath(Id.MONSTER, monsterId)).equals("")) {
                    map.removeEntity(new IdClass(Id.MONSTER, monsterId));
                    Log.w("loadMap", "Monster " + monsterId + " removed");
                }
            }

            for(long bossId : new HashSet<>(map.getEntity(Id.BOSS))) {
                if(FileManager.read(getPath(Id.BOSS, bossId)).equals("")) {
                    map.removeEntity(new IdClass(Id.BOSS, bossId));
                    Log.w("loadMap", "Boss " + bossId + " removed");
                }
            }

            return map;
        } else {
            MAP_COUNT.put(fileName, mapCount + 1);
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

    public static <T> boolean compareMap(@NonNull Map<T, Integer> map1, @NonNull Map<T, Integer> map2,
                                         boolean firstIsBig, boolean ignoreFirst, @Nullable Integer regardValue) {
        if(!firstIsBig) {
            return compareMap(map2, map1, true, ignoreFirst, regardValue);
        }

        Integer value;
        boolean flag;
        for(Map.Entry<T, Integer> entry : map2.entrySet()) {
            value = map1.get(entry.getKey());
            if(value == null) {
                if(ignoreFirst) {
                    continue;
                } else {
                    if(regardValue != null) {
                        value = regardValue;
                    } else {
                        return false;
                    }
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
        return FileManager.DATA_PATH_MAP.get(Id.PLAYER) + getRegex(sender, "") + "-" + image + ".json";
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
        if(value < 0) {
            throw new NumberRangeException(value, 0, null);
        }

        double percent = 10.0 * value / maxValue;
        double dec = percent % 1;
        int filled = (int) percent;

        StringBuilder output = new StringBuilder("[");

        for(int i = 0; i < filled; i++) {
            output.append(Emoji.FILLED_BAR);
        }

        output.append(Emoji.HALF_BAR[(int) Math.round(dec * 8)]);

        for(int i = 9; i > filled; i--) {
            output.append("   ");
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

    public static int randomToken(long startItemId, double[][] tokenList, int index, @NonNull Player self) {
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
                weight += percent;
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

    @NonNull
    public static String replaceLast(@NonNull String text, @NonNull String regex, @NonNull String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement).trim();
    }

    private static void checkUpdate(@NonNull Entity entity) {
        entity.revalidateBuff();

        if(entity.getVersion() != Config.VERSION) {
            if (entity.getId().getId().equals(Id.PLAYER)) {
                ((Player) entity).replyPlayer("데이터를 업데이트하는 중입니다\n잠시만 기다려주세요");
            }

            Logger.i("Version Update", entity.getName());
            update(entity);
        }
    }

    public static void update(@NonNull Entity entity) {
        Equipment equipment;

        boolean unavailable = false;
        List<String> unavailableEquipList = new ArrayList<>();
        for(long equipId : entity.getEquipInventory()) {
            equipment = Config.loadObject(Id.EQUIPMENT, equipId);

            if(!equipment.revalidateStat(true, entity)) {
                unavailable = true;
                unavailableEquipList.add(equipment.getName());
            }

            Config.unloadObject(equipment);
        }

        entity.revalidateEquipStat();

        if(entity.getId().getId().equals(Id.PLAYER)) {
            Player player = (Player) entity;

            if(player.getVersion() <= 1.4) {
                player.addMoney(30000);
                player.addItem(ItemList.EQUIP_SAFENER.getId(), 10);
                player.addItem(ItemList.REINFORCE_MULTIPLIER.getId(), 5);
                player.addItem(ItemList.HIGH_ELIXIR.getId(), 1);
            }

            if (unavailable) {
                StringBuilder innerBuilder = new StringBuilder("---장비 업데이트 경고---\n(장착 해제할 경우 재장착이 불가능한 장비 목록)");
                for (String string : unavailableEquipList) {
                    innerBuilder.append(Emoji.LIST)
                            .append(" ")
                            .append(string);
                }

                player.replyPlayer("업데이트가 완료되었습니다\nv" + player.getVersion() + " -> v" + Config.VERSION,
                        innerBuilder.toString());
            } else {
                player.replyPlayer("업데이트가 완료되었습니다\nv" + player.getVersion() + " -> v" + Config.VERSION);
            }
        }

        entity.setVersion(Config.VERSION);
    }

    public static int getMaxPage(int size) {
        return (int) Math.ceil((double) size / MAX_COUNT_PER_PAGE);
    }

}
