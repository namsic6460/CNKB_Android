package lkd.namsic.game.gameObject;

import android.app.Notification;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.collect.BiMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import lkd.namsic.MainActivity;
import lkd.namsic.game.Emoji;
import lkd.namsic.game.ObjectList;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.LimitDouble;
import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.LimitLong;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.Config;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.FishWaitType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MagicType;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.event.ItemUseEvent;
import lkd.namsic.game.exception.InvalidNumberException;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.exception.WeirdDataException;
import lkd.namsic.game.Variable;
import lkd.namsic.service.KakaoTalk;
import lkd.namsic.setting.Logger;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Player extends Entity {
    
    public static List<List<Double>> MINE_PERCENT = Arrays.asList(
            Arrays.asList(50D, 45D, 40D, 35D, 30D, 30D, 30D, 25D, 20D),
            Arrays.asList(35D, 35D, 20D, 10D, 0D, 0D, 0D, 0D, 0D),
            Arrays.asList(14D, 15D, 20D, 15D, 0D, 0D, 0D, 0D, 0D),
            Arrays.asList(1D, 4.5D, 14D, 20D, 15D, 0D, 0D, 0D, 0D),
            Arrays.asList(0D, 0.5D, 5.5D, 12D, 40D, 20D, 0D, 0D, 0D),
            Arrays.asList(0D, 0D, 0.5D, 7.6D, 10D, 30D, 25D, 0D, 0D),
            Arrays.asList(0D, 0D, 0D, 0.4D, 4.98D, 15D, 25D, 20D, 0D),
            Arrays.asList(0D, 0D, 0D, 0D, 0.019D, 4.8D, 15D, 32.5D, 30.5D),
            Arrays.asList(0D, 0D, 0D, 0D, 0.001D, 0.149D, 4.79D, 17.5D, 30.3D),
            Arrays.asList(0D, 0D, 0D, 0D, 0D, 0.001D, 0.2095D, 4.99D, 19.042D),
            Arrays.asList(0D, 0D, 0D, 0D, 0D, 0D, 0.0005D, 0.0055D, 0.1577D),
            Arrays.asList(0D, 0D, 0D, 0D, 0D, 0D, 0D, 0.0001D, 0.0003D)
    );

    public static List<List<Double>> FISH_PERCENT = Arrays.asList(
            Arrays.asList(35D, 25D, 15D, 5D, 1D, 0D, 0D, 0D, 0D),
            Arrays.asList(55D, 50D, 35D, 25D, 15D, 10D, 9D, 7D, 4D),
            Arrays.asList(10D, 25D, 40D, 40D, 30D, 25D, 20D, 10D, 5D),
            Arrays.asList(0D, 0D, 10D, 30D, 50D, 45D, 30D, 30D, 25D),
            Arrays.asList(0D, 0D, 0D, 0D, 4D, 20D, 40D, 5D, 60D),
            Arrays.asList(0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 1D)
    );

    @NonNull
    final
    String sender;

    @Setter
    @NonNull
    String nickName;

    @NonNull
    final
    String image;

    final Set<String> title = new ConcurrentHashSet<>();

    @NonNull
    String currentTitle = "";

    @Setter
    @NonNull
    String recentRoom;

    @Setter
    boolean pvp = false;

    @Setter
    boolean isGroup = true;

    private long lastTime = System.currentTimeMillis();

    final Location baseLocation = new Location();

    final LimitDouble moneyBoost = new LimitDouble(1, 1D, Double.MAX_VALUE);
    final LimitDouble expBoost = new LimitDouble(1, 1D, Double.MAX_VALUE);

    final LimitInteger sp = new LimitInteger(0, Config.MIN_SP, Config.MAX_SP);
    final LimitInteger adv = new LimitInteger(0, 0, Integer.MAX_VALUE);
    final LimitLong exp = new LimitLong(0, 0L, Long.MAX_VALUE);

    @Setter
    int lastDay = LocalDateTime.now().getDayOfMonth();

    final Set<Long> achieve = new ConcurrentHashSet<>();
    final Set<Long> research = new ConcurrentHashSet<>();

    final Map<Long, Long> quest = new ConcurrentHashMap<>();
    final Map<Long, ConcurrentHashSet<Long>> questNpc = new ConcurrentHashMap<>();
    final Map<Long, Integer> clearedQuest = new ConcurrentHashMap<>();

    Map<WaitResponse, Long> responseChat = new ConcurrentHashMap<>();
    Map<String, Long> anyResponseChat = new ConcurrentHashMap<>();
    String waitNpcName;

    Map<MagicType, Integer> magic = new ConcurrentHashMap<>();
    Map<MagicType, Integer> resist = new ConcurrentHashMap<>();

    final Map<Long, Integer> closeRate = new ConcurrentHashMap<>();

    final Map<LogData, Long> log = new ConcurrentHashMap<LogData, Long>() {{
        put(LogData.PLAYED_DAY, 1L);
    }};

    public Player(@NonNull String sender, @NonNull String nickName, @NonNull String image, @NonNull String recentRoom) {
        super(sender);
        this.id.setId(Id.PLAYER);

        this.sender = sender;
        this.nickName = nickName;
        this.image = image;
        this.recentRoom = recentRoom;
        
        this.addTitle("초심자");
        this.setCurrentTitle("초심자");

        this.setBasicStat(StatType.MAXHP, 100);
        this.setBasicStat(StatType.HP, 100);
        this.setBasicStat(StatType.MAXMN, 10);
        this.setBasicStat(StatType.MN, 10);

        this.setMap(this.getLocation(), 1, true);
    }

    //[테스트 칭호] 남식(Lv.123)
    @NonNull
    @Override
    public String getName() {
        return "[" + this.getCurrentTitle() + "] " + this.getNickName() + " (Lv." + this.getLv().get() + ")";
    }

    public void displayInfo() {
        StringBuilder innerMsg = new StringBuilder();

        if(this.doing.equals(Doing.WAIT_RESPONSE)) {
            innerMsg.append(this.getDisplayResponse())
                    .append("\n\n");
        }

        innerMsg.append(this.getDisplayStat())
                .append("\n\n")
                .append(this.getDisplayQuest())
                .append("\n\n")
                .append(this.getDisplayMagic())
                .append("\n\n달성한 업적 개수: ")
                .append(this.achieve.size())
                .append("\n연구 완료 개수: ")
                .append(this.research.size());

        this.replyPlayer("===내 정보===\n" +
                Emoji.GOLD + ": " + this.getMoney() + "G\n" +
                Emoji.HEART + ": " + this.getDisplayHp() + "\n" +
                Emoji.MANA + ": " + this.getStat(StatType.MN) + "/" + this.getStat(StatType.MAXMN) + "\n" +
                Emoji.WORLD + ": " + Config.getMapData(this.location).getName() + "(" + this.location.toCoordString() + ")\n" +
                Emoji.LV + ": " + this.lv.get() + "Lv (" + this.getExp().get() + "/" + this.getTotalNeedExp() + ")\n" +
                Emoji.SP + ": " + this.sp.get() + "\n" +
                Emoji.ADV + ": " + this.adv.get() + "\n" +
                Emoji.HOME + ": " + Config.getMapData(this.baseLocation).getName() + "(" + this.baseLocation.toCoordString() + ")",
                innerMsg.toString());
    }

    public String getDisplayHp() {
        int maxHp = this.getStat(StatType.MAXHP);
        int hp = this.getStat(StatType.HP);

        double percent = 10.0 * hp / maxHp;
        double dec = percent % 1;
        int filled = (int) percent;

        StringBuilder output = new StringBuilder("[");

        for(int i = 0; i < filled; i++) {
            output.append(Emoji.FILLED_HEART);
        }

        output.append(Emoji.HALF_HEART[(int) Math.round(dec * 8)]);

        for(int i = 9; i > filled; i--) {
            output.append("  ");
        }

//        if(hp % 10 == 0) {
//            output.append(" ");
//        }

        return output + "] (" + hp + "/" + maxHp + ")";
    }

    public String getDisplayResponse() {
        StringBuilder builder = new StringBuilder("---대기중인 대답---\n");

        for(WaitResponse waitResponse : this.responseChat.keySet()) {
            builder.append(waitResponse.toString())
                    .append(": ")
                    .append(waitResponse.getList());
        }

        if(!anyResponseChat.isEmpty()) {
            builder.append("(다른 메세지 목록)");

            for(String response : anyResponseChat.keySet()) {
                builder.append("\n")
                        .append(response.replaceAll("__", "n "));
            }
        }

        return builder.toString();
    }

    public String getDisplayStat() {
        StringBuilder builder = new StringBuilder("---스텟---");
        for(StatType statType : StatType.values()) {
            builder.append("\n")
                    .append(statType.toString())
                    .append(": ")
                    .append(this.getStat(statType));
        }

        return builder.toString();
    }

    public String getDisplayQuest() {
        StringBuilder builder = new StringBuilder("---퀘스트 목록---");

        if(this.quest.isEmpty()) {
            return builder.toString() + "\n현재 진행중인 퀘스트 없음";
        }

        for(long questId : this.quest.keySet()) {
            Quest quest = Config.getData(Id.QUEST, questId);

            builder.append("\n[")
                    .append(questId)
                    .append("] ")
                    .append(quest.getName());

            Long npcId = quest.npcId.get();
            if(!npcId.equals(0L)) {
                Npc npc = Config.getData(Id.NPC, npcId);

                builder.append(" (NPC: ")
                        .append(npc.getName())
                        .append(")");
            }
        }

        return builder.toString();
    }

    public String getDisplayMagic() {
        StringBuilder builder = new StringBuilder("---마법 정보---");

        for(MagicType magicType : MagicType.values()) {
            builder.append("\n")
                    .append(magicType.toString())
                    .append(": ")
                    .append(this.getMagic(magicType))
                    .append("Lv, 저항: ")
                    .append(this.getResist(magicType))
                    .append("Lv");
        }

        return builder.toString();
    }

    public void displayInventory(int page) {
        if(this.inventory.isEmpty()) {
            this.replyPlayer("---인벤토리---\n인벤토리가 비어있습니다...");
            return;
        }

        int maxPage = (int) Math.ceil(this.inventory.size() / 30D);

        if(page < 1 || page > maxPage) {
            throw new WeirdCommandException(page + "페이지 부터 " + maxPage + "페이지 범위 내의 숫자를 입력해주세요");
        }

        List<Long> highPriorityItems = this.getObjectVariable(Variable.HIGH_PRIORITY_ITEM);
        BiMap<Long, String> inverseMap = ObjectList.itemList.inverse();

        StringBuilder msgBuilder = new StringBuilder("---인벤토리---\n[")
                .append(page)
                .append("페이지 /")
                .append(maxPage)
                .append("페이지]");

        if(highPriorityItems == null) {
            highPriorityItems = new ArrayList<>();
            this.setVariable(Variable.HIGH_PRIORITY_ITEM, highPriorityItems);
            msgBuilder.append("\n우선 표시 설정된 아이템이 없습니다");
        } else {
            for(long itemId : highPriorityItems) {
                msgBuilder.append("\n")
                        .append(inverseMap.get(itemId))
                        .append(": ")
                        .append(this.getItem(itemId))
                        .append("개");
            }
        }

        StringBuilder innerMsgBuilder = new StringBuilder();

        int count = 0;
        Set<Long> sortedKeys = new TreeSet<>(this.inventory.keySet());
        for(long itemId : sortedKeys) {
            if(highPriorityItems.contains(itemId)) {
                continue;
            }

            innerMsgBuilder.append(inverseMap.get(itemId))
                    .append(": ")
                    .append(this.getItem(itemId))
                    .append("개\n");
            count++;

            if(count == 30) {
                break;
            }
        }

        this.replyPlayer(msgBuilder.toString(), innerMsgBuilder.toString());
    }

    public void addTitle(String title) {
        this.title.add(title);
    }

    public void setCurrentTitle(String title) {
        if(this.title.contains(title)) {
            this.currentTitle = title;
        } else {
            throw new ObjectNotFoundException("Title : " + title);
        }
    }

    public void replyPlayer(@NonNull String msg) {
        KakaoTalk.reply(this.getSession(), this.getName() + "\n" + msg);
    }

    public void replyPlayer(@NonNull String msg, @NonNull String innerMsg) {
        KakaoTalk.reply(this.getSession(), this.getName() + "\n" + msg, innerMsg);
    }

    public boolean checkChat() {
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - this.lastTime;
        this.lastTime = currentTime;

        boolean flag = this.getDoing().equals(Doing.FIGHT) ? diffTime >= 3000 : diffTime >= 500;
        if(flag) {
            LocalDateTime now = LocalDateTime.now();

            if(now.getDayOfMonth() != this.lastDay) {
                this.lastDay = now.getDayOfMonth();
                this.newDay();
            }
        }

        return flag;
    }

    public void newDay() {
        System.out.println("New Day!");
    }

    @NonNull
    public Notification.Action getSession() {
        if(this.isGroup) {
            return KakaoTalk.getGroupSession(this.recentRoom);
        } else {
            return KakaoTalk.getSoloSession(this.recentRoom);
        }
    }

    @NonNull
    public String getBattleMsg() {
        StringBuilder builder = new StringBuilder("---적 목록---\n");

        Id id;
        Entity enemy;

        for(Map.Entry<Id, ConcurrentHashSet<Long>> entry : this.enemies.entrySet()) {
            id = entry.getKey();

            for(long objectId : entry.getValue()) {
                builder.append(id.getValue());
                builder.append(".");
                builder.append(objectId);
                builder.append(" - ");

                enemy = Config.getData(id, objectId);
                builder.append(" (");
                builder.append(this.getFieldDistance(enemy.getLocation()));
                builder.append("m) ");
                builder.append(enemy.getName());
                builder.append("\n");
            }
        }

        builder.append("\n---전투 명령어---\n");
        builder.append("(공격/attack/atk) [대상] : [\"대상\" 또는 \"가장 가까운 적\"] 에게 기본 공격을 합니다\n");
        builder.append("(방어/defence/def) : 다음 피해가 기본 공격일 경우, 공격을 방어하고 체력과 마나를 일부 회복합니다\n");
        builder.append("(이동/move) (e/w/s/n) [1~3] : 동/서/남/북의 방향으로 [\"3칸\" 또는 \"지정한 거리\"] 만큼 최대한 이동합니다\n");
        builder.append("(필드/field) : 필드 정보를 표시합니다\n");
        builder.append("(스킬/skill) 목록 [o] : [\"보유한 또는 \"사용 가능한\"] 스킬 목록을 확인합니다\n");
        builder.append("(스킬/skill) 사용 (스킬 번호) [대상] : 스킬 번호에 해당하는 스킬을 [\"대상\" 또는 \"가장 가까운 적\"] 에게 사용합니다\n");
        builder.append("(아이템/item) 사용 (아이템 번호) (대상) : 아이템 번호에 해당하는 아이템을 대상(본인 : \"s\")에게 사용합니다\n");
        builder.append("(도망/run) : 거점으로의 도망을 시도합니다. 실패 시 최대 체력의 10%에 해당하는 피해를 입습니다");
        builder.append("(사망하지는 않습니다)(실패 시 15초간 사용이 불가능해집니다)\n");
        builder.append("(카운터/counter) [대상] : (보스한정) [\"대상\" 또는 \"가장 가까운 보스\"] 의 스킬을 반사합니다");

        return builder.toString();
    }

    public int getMovableDistance() {
        return (int) Math.sqrt(2 + this.getStat(StatType.AGI) / 4D);
    }

    public void move(String locationStr) {
        String[] split = locationStr.split("-");
        if(split.length != 2) {
            throw new WeirdCommandException("좌표를 정확하게 입력해주세요(x좌표-y좌표)\nex) 0-1");
        }

        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        Location location = new Location(x, y);

        if(location.equalsMap(this.location)) {
            throw new WeirdCommandException("현재 위치로는 이동할 수 없습니다");
        }

        int dis = this.getMapDistance(location);
        int movableDis = this.getMovableDistance();

        if(dis > movableDis) {
            throw new WeirdCommandException("이동 가능한 거리보다 먼 거리에 있는 좌표입니다\n" +
                    "이동 가능 거리 : " + movableDis + ", 실제 거리: " + dis);
        } else {
            MapClass moveMap = null;

            try {
                moveMap = Config.loadMap(x, y);

                try {
                    this.setMap(location, true);
                } catch (NumberRangeException e) {
                    if(Objects.requireNonNull(e.getMessage()).endsWith(Integer.toString(Config.MAX_LV))) {
                        this.replyPlayer("해당 지역으로 이동하기 위한 요구 레벨이 부족합니다\n현재 레벨: " +
                                this.lv.get() + ", 요구 레벨: " + moveMap.getRequireLv().get());
                        return;
                    } else {
                        throw e;
                    }
                }

                this.replyPlayer("이동을 완료했습니다\n현재 좌표: " + this.location.toCoordString());
            } finally {
                if(moveMap != null) {
                    Config.unloadMap(moveMap);
                }
            }
        }
    }

    public boolean canUse(long itemId) {
        if(this.getItem(itemId) > 0) {
            Item item = Config.getData(Id.ITEM, itemId);
            return item.getUse() != null;
        } else {
            return false;
        }
    }

    public boolean useItem(long itemId, @NonNull List<GameObject> other) {
        Config.checkId(Id.ITEM, itemId);

        boolean isCancelled;
        Item item = Config.getData(Id.ITEM, itemId);
        isCancelled = ItemUseEvent.handleEvent(this.events.get(ItemUseEvent.getName()), new Object[]{item});

        if (!isCancelled) {
            this.addLog(LogData.TOTAL_ITEM_USED, 1);
            item.getUse().use(this, other);
            this.addItem(itemId, -1);
        }

        return isCancelled;
    }

    public boolean canEat(long itemId) {
        if(this.getItem(itemId) > 0) {
            Item item = Config.getData(Id.ITEM, itemId);
            return item.isFood();
        } else {
            return false;
        }
    }

    public void eat(long itemId) {
        Item item = Config.getData(Id.ITEM, itemId);

        if(item.isFood()) {
            this.addItem(itemId, -1);
            this.addLog(LogData.EAT, 1);

            for(Map.Entry<Long, HashMap<StatType, Integer>> entry : item.getEatBuff().entrySet()) {
                for(Map.Entry<StatType, Integer> statEntry : entry.getValue().entrySet()) {
                    this.addBuff(entry.getKey(), statEntry.getKey(), statEntry.getValue());
                }
            }

            this.revalidateBuff();

            this.replyPlayer(item.getName() + "을 먹었습니다\n남은 개수 : " + this.getItem(itemId));
        } else {
            throw new WeirdDataException(Id.ITEM, itemId);
        }
    }

    public void addExp(long exp) {
        if(exp < 0) {
            throw new NumberRangeException(0, 0, Long.MAX_VALUE);
        }

        exp *= Config.EXP_BOOST;
        exp *= this.expBoost.get();

        this.exp.add(exp);
        this.addLog(LogData.TOTAL_EXP, exp);

        long requiredExp;
        int startLv = this.lv.get();
        int endLv;

        if(startLv == Config.MAX_LV) {
            return;
        }

        while(true) {
            requiredExp = this.getRequiredExp();

            if(requiredExp == 0) {
                this.lvUp(requiredExp);
            } else {
                endLv = this.lv.get();

                if(startLv != endLv) {
                    this.replyPlayer(
                            "레벨 업!(" + startLv + "->" + endLv + ")",
                            "현재 경험치 : " + this.exp.get() + "\n현재 스텟 포인트 : " + this.sp.get()
                    );
                }

                break;
            }
        }
    }

    public long getRequiredExp() {
        return this.getTotalNeedExp() - this.getExp().get();
    }

    public long getTotalNeedExp() {
        return this.getTotalNeedExp(this.lv.get());
    }

    public long getTotalNeedExp(int lv) {
        long needExp;
        if (lv <= 100) {
            needExp = 10_000 + 5_000 * lv;
        } else if (lv <= 300) {
            needExp = -200_000 + (long) (Math.pow(lv - 10, 2.5) * 2) + 10_000 * lv;
        } else if (lv <= 500) {
            needExp = -50_000_000 + 200_000 * lv;
        } else if(lv <= 750) {
            needExp = -50_000_000 + (long) (Math.pow(lv, 3.1) / 1.5);
        } else if(lv <= 950) {
            needExp = 45_000_000L * (lv - 750) + 1_000_000_000L;
        } else {
            needExp = -30_000_000_000L + (long) Math.pow(lv - 600, 4.2);
        }

        return needExp;
    }

    public long getKillExp(int enemyLv) {
        int lv = this.lv.get();
        long exp = 5000 + (long) ((this.getTotalNeedExp(enemyLv) * 0.00001) + (this.getTotalNeedExp() * 0.00001));

        int gap = lv - enemyLv;
        if(Math.abs(gap) > 10 && lv > 10) {
            double sqrt = Math.sqrt(lv - 9) - 1;

            if(gap > 0) {
                exp /= sqrt;
            } else {
                exp *= sqrt;
            }
        }

        return exp;
    }

    public void lvUp(long requiredExp) {
        this.lv.add(1);
        this.sp.add(5);
        this.exp.add(-1 * requiredExp);
    }

    @Nullable
    public String canStartChat(Chat chat) {
        this.revalidateBuff();

        String failMsg = "";

        long needMoney = chat.getNeedMoney().get();
        if(needMoney != 0 && this.getMoney() < needMoney) {
            failMsg += "보유 골드가 부족합니다(부족한 금액 : " + (needMoney - this.getMoney()) + "G)\n\n";
        }

        long questId = chat.getQuestId().get();
        if (!this.canAddQuest(questId)) {
            failMsg += "퀘스트 수령이 불가능합니다(상세 내용은 퀘스트 검색(아이디 : " + questId + ")\n\n";
        }

        int diff;
        Map<String, Integer> missingItem = new HashMap<>();

        for (Map.Entry<Long, Integer> entry : chat.getNeedItem().entrySet()) {
            long key = entry.getKey();
            int value = entry.getValue();

            diff = value - this.getItem(key);

            if (diff > 0) {
                Item item = Config.getData(Id.ITEM, key);
                missingItem.put(item.getName(), diff);
            }
        }

        if (!missingItem.isEmpty()) {
            failMsg += "보유 아이템이 부족합니다\n---부족한 아이템 목록---\n" + Config.mapToDisplayString(missingItem) + "\n";
        }

        Map<String, Integer> missingStat = new ConcurrentHashMap<>();

        for(Map.Entry<StatType, Integer> entry : chat.getNeedStat().entrySet()) {
            StatType key = entry.getKey();
            int value = entry.getValue();

            diff = entry.getValue() - this.getStat(entry.getKey());

            if(diff > 0) {
                missingStat.put(key.toString(), value);
            }
        }

        if(!missingStat.isEmpty()) {
            failMsg += "스텟이 부족합니다\n---부족한 스텟 목록---\n : " + Config.mapToDisplayString(missingStat);
        }

        if(failMsg.equals("")) {
            return null;
        } else {
            return failMsg;
        }
    }

    public void startChat(long chatId, @NonNull  String npcName) {
        Config.checkId(Id.CHAT, chatId);
        this.setDoing(Doing.CHAT);

        this.getResponseChat().clear();
        this.getAnyResponseChat().clear();

        Chat chat = Config.getData(Id.CHAT, chatId);
        Notification.Action session = this.getSession();

        this.addMoney(-1 * chat.getNeedMoney().get());

        for(Map.Entry<Long, Integer> entry : chat.getNeedItem().entrySet()) {
            this.addItem(entry.getKey(), -1 * entry.getValue());
        }

        for(Map.Entry<StatType, Integer> entry : chat.getNeedStat().entrySet()) {
            this.addBasicStat(entry.getKey(), -1 * entry.getValue());
        }

        final Chat finalChat = chat;
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(chat.delayTime.get());
            } catch (InterruptedException e) {
                Log.e("Player.startChat - chat Thread", Config.errorString(e));
                return;
            }

            this.addMoney(finalChat.getRewardMoney().get());

            for(Map.Entry<Long, Integer> entry : finalChat.getNeedItem().entrySet()) {
                this.addItem(entry.getKey(), entry.getValue());
            }

            for(Map.Entry<StatType, Integer> entry : finalChat.getNeedStat().entrySet()) {
                this.addBasicStat(entry.getKey(), entry.getValue());
            }

            long pauseTime = finalChat.getPauseTime().get();
            String preString = "[" + npcName + " -> " + this.getNickName() + "]\n";

            List<String> texts = finalChat.getText();
            int size = texts.size() - 1;
            for(int i = 0; i <= size; i++) {
                KakaoTalk.reply(session, preString + texts.get(i).replaceAll("__nickname", this.getNickName())
                        .replaceAll("__lv", this.getLv().get().toString()));

                try {
                    if(i != size) {
                        Thread.sleep(pauseTime);
                    }
                } catch (InterruptedException e) {
                    Log.e("Player.startChat - chat Thread", Config.errorString(e));
                    return;
                }
            }

            long questId = finalChat.getQuestId().get();
            if(questId != 0) {
                this.addQuest(finalChat.getQuestId().get());
            }

            Location tpLocation = finalChat.getTpLocation();
            if(!this.getLocation().equals(tpLocation)) {
                this.setMap(finalChat.getTpLocation(), false);
            }

            if(finalChat.getResponseChat().isEmpty() && finalChat.getAnyResponseChat().isEmpty()) {
                this.setDoing(Doing.NONE);
            } else {
                long anyLinkedChatId = finalChat.getResponseChat(WaitResponse.ANYTHING);
                if(anyLinkedChatId != 0) {
                    if (chatId == anyLinkedChatId) {
                        throw new InvalidNumberException(chatId);
                    }

                    this.startChat(anyLinkedChatId, npcName);
                    return;
                }

                this.waitNpcName = npcName;
                this.responseChat = new ConcurrentHashMap<>(finalChat.getResponseChat());
                this.anyResponseChat = new ConcurrentHashMap<>(finalChat.getAnyResponseChat());

                this.setDoing(Doing.WAIT_RESPONSE);
            }
        });

        this.addLog(LogData.CHAT, 1);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            Logger.e("player.chatThread", e);
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public Player addAchieve(long achieveId) {
        Config.checkId(Id.ACHIEVE, achieveId);

        if(this.achieve.add(achieveId)) {
            Achieve achieve = null;

            try {
                achieve = Config.loadObject(Id.ACHIEVE, achieveId);

                this.addMoney(achieve.getRewardMoney().get());
                this.addExp(achieve.rewardExp.get());
                this.adv.add(achieve.rewardAdv.get());

                for (long npcId : achieve.getRewardCloseRate().keySet()) {
                    this.addCloseRate(npcId, achieve.getRewardCloseRate(npcId));
                }

                for (long itemId : achieve.getRewardItem().keySet()) {
                    this.addItem(itemId, achieve.getRewardItem(itemId));
                }

                for (StatType statType : achieve.getRewardStat().keySet()) {
                    this.addBasicStat(statType, achieve.getRewardStat(statType));
                }
            } finally {
                if(achieve != null) {
                    Config.unloadObject(achieve);
                }
            }
        }

        return this;
    }

    public boolean canAddResearch(long researchId) {
        Config.checkId(Id.RESEARCH, researchId);

        Research research = null;
        boolean flag;

        try {
            research = Config.loadObject(Id.RESEARCH, researchId);
            flag = research.getLimitLv().isInRange(this.lv.get()) && research.getNeedMoney().get() <= this.getMoney()
                    && Config.compareMap(this.inventory, research.needItem, true);
        } finally {
            if(research != null) {
                Config.unloadObject(research);
            }
        }

        Config.unloadObject(research);

        return flag;
    }

    public void addResearch(long researchId) {
        if(this.research.add(researchId)) {
            Research research = null;

            try {
                research = Config.loadObject(Id.RESEARCH, researchId);

                this.addMoney(-1 * research.getNeedMoney().get());

                this.addExp(research.rewardExp.get());
                this.adv.add(research.rewardAdv.get());

                for (long npcId : research.getRewardCloseRate().keySet()) {
                    this.addCloseRate(npcId, research.getRewardCloseRate(npcId));
                }

                for (long itemId : research.getRewardItem().keySet()) {
                    this.addItem(itemId, research.getRewardItem(itemId));
                }

                for (StatType statType : research.getRewardStat().keySet()) {
                    this.addBasicStat(statType, research.getRewardStat(statType));
                }
            } finally {
                if(research != null) {
                    Config.unloadObject(research);
                }
            }
        }
    }

    public boolean canAddQuest(long questId) {
        Quest quest = null;
        boolean flag;

        try {
            quest = Config.loadObject(Id.QUEST, questId);

            flag = quest.limitLv.isInRange(this.lv.get()) && quest.limitCloseRate.isInRange(this.closeRate)
                    && this.checkStatRange(quest.limitStat.getMin(), quest.limitStat.getMax());

            if(flag) {
                if(clearedQuest.containsKey(questId)) {
                    flag = quest.isRepeatable();
                }
            }
        } finally {
            if(quest != null) {
                Config.unloadObject(quest);
            }
        }

        return flag;
    }

    public void addQuest(long questId) {
        Quest quest = null;

        try {
            quest = Config.loadObject(Id.QUEST, questId);

            this.quest.put(questId, quest.getChatId().get());
            this.addLog(LogData.QUEST_RECEIVED, 1);

            long questNpcId = quest.getNpcId().get();
            if(questNpcId != 0) {
                ConcurrentHashSet<Long> questNpc = this.questNpc.get(questNpcId);

                if(questNpc == null) {
                    questNpc = new ConcurrentHashSet<>();
                    questNpc.add(questId);
                    this.questNpc.put(questNpcId, questNpc);
                } else {
                    questNpc.add(questId);
                }
            }
        } finally {
            if(quest != null) {
                Config.unloadObject(quest);
            }
        }
    }

    public boolean canMine() {
        return Config.getMapData(this.location).getMapType().equals(MapType.COUNTRY);
    }

    public void mine() {
        this.setDoing(Doing.MINE);
        this.addLog(LogData.MINED, 1);

        int mineLv = this.getVariable(Variable.MINE);

        double randomPercent = Math.random() * 100;

        List<Double> percents = new ArrayList<>();
        for(List<Double> minePercent : MINE_PERCENT) {
            percents.add(minePercent.get(mineLv));
        }

        List<Long> output = Arrays.asList(20L, 21L, 0L, 0L, 0L, 31L, 33L, 0L, 0L, 0L, 43L, 0L);

        double percent;
        long itemId = 0;
        for(int itemTier = 0; itemTier < percents.size(); itemTier++) {
            percent = percents.get(itemTier);

            if(randomPercent < percent) {
                itemId = output.get(itemTier);

                if(itemId == 0) {
                    long[] itemList;

                    switch(itemTier) {
                        case 2:
                            itemList = new long[]{23L, 24L, 25L, 26L};
                            break;
                        case 3:
                            itemList = new long[]{27L, 28L};
                            break;
                        case 4:
                            itemList = new long[]{29L, 30L};
                            break;
                        case 7:
                            itemList = new long[]{34L, 35L};
                            break;
                        case 8:
                            itemList = new long[]{36L, 37L};
                            break;
                        case 9:
                            itemList = new long[]{39L, 40L};
                            break;
                        case 11:
                            itemList = new long[]{44L, 45L, 46L};
                            break;
                        default:
                            throw new InvalidNumberException(itemTier);
                    }

                    itemId = itemList[new Random().nextInt(itemList.length)];
                }

                break;
            } else {
                randomPercent -= percent;
            }
        }

        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            Logger.e("player.mineThread", e);
            return;
        }

        int count = this.getItem(itemId);
        this.addItem(itemId, 1);

        Item item = Config.getData(Id.ITEM, itemId);
        this.replyPlayer(item.getName() + "을 캤습니다!\n아이템 개수 : " + count + " -> " + (count + 1));

        if(this.checkMineLevel()) {
            this.addVariable(Variable.MINE, 1);
            this.replyPlayer("광업 레벨이 올랐습니다!\n광업 레벨: " + mineLv + " -> " + (mineLv + 1));
        }

        this.setDoing(Doing.NONE);
    }

    public boolean checkMineLevel() {
        int mineLv = this.getVariable(Variable.MINE);
        long mined = this.getLog(LogData.MINED);
        int requireCount;

        if(mineLv == 8) {
            return false;
        }

        switch (mineLv) {
            case 0:
                requireCount = 30;
                break;
            case 1:
                requireCount = 200;
                break;
            case 2:
                requireCount = 1000;
                break;
            case 3:
                requireCount = 5000;
                break;
            case 4:
                requireCount = 20_000;
                break;
            case 5:
                requireCount = 100_000;
                break;
            case 6:
                requireCount = 1_000_000;
                break;
            case 7:
                requireCount = 10_000_000;
                break;
            default:
                throw new NumberRangeException(mineLv, 0, 7);
        }

        return mined >= requireCount;
    }

    public boolean canFish() {
        MapType mapType = Config.getMapData(this.location).getMapType();
        return mapType.equals(MapType.SEA) || mapType.equals(MapType.RIVER);
    }

    public void fish() {
        this.setDoing(Doing.FISH);
        this.addLog(LogData.FISH, 1);

        int fishLv = this.getVariable(Variable.FISH);

        double randomPercent = Math.random() * 100;

        List<Double> percents = new ArrayList<>();
        for(List<Double> fishPercent : FISH_PERCENT) {
            percents.add(fishPercent.get(fishLv));
        }

        Random random = new Random();

        int itemTier = 0;
        long itemId = 0;
        int commandCount = 0;
        double percent;
        for(; itemTier < percents.size(); itemTier++) {
            percent = percents.get(itemTier);

            if(randomPercent < percent) {
                if (randomPercent < percents.get(itemTier)) {
                    long[] itemList;

                    switch (itemTier) {
                        case 0:
                            itemList = new long[]{1L, 2L, 58L, 59L};
                            commandCount = random.nextInt(2);
                            break;
                        case 1:
                            itemList = new long[]{60L, 61L, 62L, 63L, 64L};
                            commandCount = random.nextInt(6);
                            break;
                        case 2:
                            itemList = new long[]{65L, 66L, 67L, 68L, 69L, 70L};
                            commandCount = random.nextInt(8) + 1;
                            break;
                        case 3:
                            itemList = new long[]{71L, 72L, 73L, 74L, 75L, 76L, 77L, 78L, 79L, 80L};
                            commandCount = random.nextInt(8) + 3;
                            break;
                        case 4:
                            itemList = new long[]{81L, 82L, 83L, 84L, 85L, 86L, 87L};
                            commandCount = random.nextInt(10) + 6;
                            break;
                        case 5:
                            itemList = new long[]{88L, 89L, 90L, 91L};
                            commandCount = random.nextInt(6) + 10;
                            break;
                        case 6:
                            itemList = new long[]{92L, 93L};
                            commandCount = random.nextInt(11) + 20;
                            break;
                        default:
                            throw new NumberRangeException(itemTier, 0, 6);
                    }

                    itemId = itemList[random.nextInt(itemList.length)];
                }

                break;
            } else {
                randomPercent -= percent;
            }
        }

        this.startFishThread(itemId, itemTier, commandCount);
    }

    public void startFishThread(long itemId, int itemTier, int commandCount) {
        Random random = new Random();

        this.setVariable(Variable.FISH_WAIT_TYPE, FishWaitType.NONE);
        this.replyPlayer("낚시를 시작합니다");

        Thread thread = new Thread(() -> {
            FishWaitType lastWaitType = null;
            FishWaitType waitType;

            for(int i = commandCount; i >= 0; i--) {
                if(i == 0) {
                    waitType = FishWaitType.CATCH;
                } else {
                    int randomWaitTypeIdx;

                    if(lastWaitType == null || lastWaitType.equals(FishWaitType.SHAKE) || lastWaitType.equals(FishWaitType.WAIT)) {
                        randomWaitTypeIdx = random.nextInt(4);
                        waitType = new FishWaitType[]{
                                FishWaitType.SHAKE, FishWaitType.WAIT, FishWaitType.PULL, FishWaitType.RESIST
                        }[randomWaitTypeIdx];
                    } else {
                        randomWaitTypeIdx = random.nextInt(2);
                        waitType = new FishWaitType[]{
                                FishWaitType.PULL, FishWaitType.RESIST
                        }[randomWaitTypeIdx];
                    }
                }

                lastWaitType = waitType;

                try {
                    Thread.sleep(random.nextInt(3000) + 5000);
                } catch (InterruptedException e) {
                    Logger.e("Player.fishThread", e);
                    throw new RuntimeException(e.getMessage());
                }

                this.setVariable(Variable.FISH_WAIT_TYPE, waitType);

                String message;
                if(waitType.equals(FishWaitType.SHAKE)) {
                    message = "아직 특별한 느낌이 없습니다\n낚싯대를 흔들어 물고기를 유혹해봅시다\n" +
                            Emoji.focus("(n/ㅜ) (낚시/fish) (흔들기/shake)");
                } else if(waitType.equals(FishWaitType.WAIT)) {
                    message = "미세한 무언가가 느껴집니다...\n확실히 물릴때까지 기다려봅시다\n" +
                            Emoji.focus("(n/ㅜ) (낚시/fish) (기다리기/wait)");
                } else if(waitType.equals(FishWaitType.PULL)) {
                    message = "걸린 것 같습니다!\n힘차게 당겨봅시다\n" +
                            Emoji.focus("(n/ㅜ) (낚시/fish) (당기기/pull)");
                } else if(waitType.equals(FishWaitType.RESIST)) {
                    message = "이런! 잘못하면 낚싯대가 망가지겠네요\n최대한 버텨봅시다\n" +
                            Emoji.focus("(n/ㅜ) (낚시/fish) (버티기/resist)");
                } else {
                    message = "지금입니다, 당기세요!\n" +
                            Emoji.focus("(n/ㅜ) (낚시/fish) (잡기/catch)");
                }

                this.replyPlayer(message);

                synchronized (this) {
                    try {
                        this.wait(5000);
                    } catch (InterruptedException e) {
                        Logger.e("Player.fishThread - wait", e);
                        throw new RuntimeException(e.getMessage());
                    }

                    this.notifyAll();
                }

                FishWaitType response = this.getObjectVariable(Variable.FISH_WAIT_TYPE);

                if(response.equals(FishWaitType.NONE)) {
                    if(i == 0) {
                        this.addItem(itemId, 1);
                        this.addVariable(Variable.FISH_SKILL, 1);
                        this.setDoing(Doing.NONE);

                        if(itemTier != 0) {
                            Map<Long, Integer> fishMap = this.getObjectVariable(Variable.FISH_MAP);
                            if (fishMap == null) {
                                fishMap = new ConcurrentHashMap<>();
                                this.setVariable(Variable.FISH_MAP, fishMap);

                                int skillIncrease = 2 * itemTier * itemTier;
                                this.replyPlayer("새로운 물고기를 낚았습니다!\n낚시 숙련도 + " + skillIncrease);

                                this.addVariable(Variable.FISH_SKILL, skillIncrease);
                            }

                            int fishCount = fishMap.getOrDefault(itemId, 0) + 1;
                            fishMap.put(itemId, fishCount);

                            int skillIncrease = 0;
                            if(fishCount == 10) {
                                skillIncrease = 5;
                            } else if(fishCount == 50) {
                                skillIncrease = 10;
                            } else if(fishCount == 100 || fishCount % 500 == 0) {
                                skillIncrease = 50;
                            }

                            if(skillIncrease != 0) {
                                this.replyPlayer("해당 물고기를 " + fishCount + "회 낚았습니다!\n낚시 숙련도 + " + skillIncrease);
                                this.addVariable(Variable.FISH_SKILL, skillIncrease);
                            }
                        }

                        int fishLv = this.getVariable(Variable.FISH);
                        if(checkFishLevel()) {
                            this.addVariable(Variable.FISH, 1);
                            this.setVariable(Variable.FISH_SKILL, 0);
                            this.replyPlayer("낚시 레벨이 올랐습니다!\n낚시 레벨: " + fishLv + " -> " + (fishLv + 1));
                        }

                        int fishSkill = this.getVariable(Variable.FISH_SKILL);

                        this.getVariable().remove(Variable.FISH_WAIT_TYPE);
                        this.replyPlayer("낚시 성공!\n" +
                                "낚은 아이템: " + ObjectList.itemList.inverse().get(itemId) + "\n" +
                                "현재 보유 개수: " + this.getItem(itemId) + "개\n" +
                                "현재 낚시 숙련도: " + fishSkill);

                        return;
                    } else {
                        this.replyPlayer("성공적으로 물고기를 컨트롤했습니다!");
                    }
                } else {
                    int fishSkill = this.getVariable(Variable.FISH_SKILL);

                    String failMessage = "이런... 물고기를 놓쳐버렸습니다";
                    if(fishSkill > 0) {
                        if(random.nextInt(2) == 0) {
                            this.setVariable(Variable.FISH_SKILL, fishSkill - 1);
                            failMessage = failMessage + "\n낚시 숙련도 -1";
                        }
                    }

                    this.setDoing(Doing.NONE);

                    this.replyPlayer(failMessage);
                    return;
                }
            }
        });

        MainActivity.startThread(thread);

        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Player.fishThread", Config.errorString(e));
        }
    }

    public void fishCommand(String command) {
        FishWaitType waitType = this.getObjectVariable(Variable.FISH_WAIT_TYPE);
        FishWaitType response;

        switch (command) {
            case "흔들기":
            case "shake":
                response = FishWaitType.SHAKE;
                break;
            case "기다리기":
            case "wait":
                response = FishWaitType.WAIT;
                break;
            case "당기기":
            case "pull":
                response = FishWaitType.PULL;
                break;
            case "버티기":
            case "resist":
                response = FishWaitType.RESIST;
                break;
            case "잡기":
            case "catch":
                response = FishWaitType.CATCH;
                break;
            default:
                throw new WeirdCommandException();
        }

        if(waitType.equals(response)) {
            this.setVariable(Variable.FISH_WAIT_TYPE, FishWaitType.NONE);
        }

        synchronized (this) {
            this.notifyAll();
        }
    }

    public boolean checkFishLevel() {
        int fishLv = this.getVariable(Variable.FISH);
        int fishSkill = this.getVariable(Variable.FISH_SKILL);
        int requireSkill;

        if(fishLv == 8) {
            return false;
        }

        switch (fishLv) {
            case 0:
                requireSkill = 20;
                break;
            case 1:
                requireSkill = 50;
                break;
            case 2:
                requireSkill = 100;
                break;
            case 3:
                requireSkill = 500;
                break;
            case 4:
                requireSkill = 1000;
                break;
            case 5:
                requireSkill = 5000;
                break;
            case 6:
                requireSkill = 10000;
                break;
            case 7:
                requireSkill = 20000;
                break;
            default:
                throw new NumberRangeException(fishLv, 0, 7);
        }

        return fishSkill >= requireSkill;
    }

    public boolean canClearQuest(long questId) {
        Config.checkId(Id.QUEST, questId);
        Long chatId = this.quest.get(questId);

        if(chatId == null) {
            throw new ObjectNotFoundException(Id.QUEST, questId);
        }

        Quest quest = Config.getData(Id.QUEST, questId);

        return this.getMoney() >= quest.getNeedMoney().get() &&
                this.getAdv().get() >= quest.getNeedAdv().get() &&
                Config.compareMap(this.inventory, quest.getNeedItem(), true) &&
                this.compareStat(quest.getNeedStat()) &&
                Config.compareMap(this.getCloseRate(), quest.getNeedCloseRate(), true);
    }

    public void clearQuest(long questId) {
        Config.checkId(Id.QUEST, questId);
        Long chatId = this.quest.get(questId);

        if(chatId == null) {
            throw new ObjectNotFoundException(Id.QUEST, questId);
        }

        Quest quest = Config.getData(Id.QUEST, questId);

        this.addMoney(-1 * quest.getNeedMoney().get());
        this.adv.add(-1 * quest.getNeedAdv().get());

        for(Map.Entry<Long, Integer> entry : quest.getNeedItem().entrySet()) {
            this.addItem(entry.getKey(), -1 * entry.getValue());
        }

        for(Map.Entry<StatType, Integer> entry : quest.getNeedStat().entrySet()) {
            this.addBasicStat(entry.getKey(), -1 * entry.getValue());
        }

        for(Map.Entry<Long, Integer> entry : quest.getNeedCloseRate().entrySet()) {
            this.addCloseRate(entry.getKey(), -1 * entry.getValue());
        }

        this.addMoney(quest.getRewardMoney().get());
        this.addExp(quest.getRewardExp().get());
        this.adv.add(quest.getRewardAdv().get());

        for(Map.Entry<Long, Integer> entry : quest.getRewardItem().entrySet()) {
            this.addItem(entry.getKey(), entry.getValue());
        }

        for(Map.Entry<StatType, Integer> entry : quest.getNeedStat().entrySet()) {
            this.addBasicStat(entry.getKey(), -1 * entry.getValue());
        }

        for(Map.Entry<Long, Integer> entry : quest.getNeedCloseRate().entrySet()) {
            this.addCloseRate(entry.getKey(), -1 * entry.getValue());
        }

        this.clearedQuest.put(questId, this.getClearedQuest(questId) + 1);
        this.quest.remove(questId);
        this.addLog(LogData.QUEST_CLEARED, 1);

        long questNpcId = quest.getNpcId().get();
        Npc npc = Config.getData(Id.NPC, questNpcId);
        if(questNpcId != 0) {
            ConcurrentHashSet<Long> questNpc = Objects.requireNonNull(this.questNpc.get(questNpcId));

            if(questNpc.size() == 1) {
                this.questNpc.remove(questId);
            } else {
                questNpc.remove(questId);
            }
        }

        this.startChat(chatId, npc.getName());
    }

    public int getClearedQuest(long questId) {
        return this.clearedQuest.getOrDefault(questId, 0);
    }

    public void setResponseChat(@NonNull WaitResponse waitResponse, long questId) {
        if(questId == 0) {
            this.responseChat.remove(waitResponse);
        } else {
            Config.checkId(Id.QUEST, questId);
            this.responseChat.put(waitResponse, questId);
        }
    }

    public long getResponseChat(@NonNull WaitResponse waitResponse) {
        return this.responseChat.getOrDefault(waitResponse, 0L);
    }

    public void setAnyResponseChat(@NonNull String response, long questId) {
        if(questId == 0) {
            this.anyResponseChat.remove(response);
        } else {
            Config.checkId(Id.QUEST, questId);
            this.anyResponseChat.put(response, questId);
        }
    }

    public long getAnyResponseChat(@NonNull String response) {
        return this.anyResponseChat.getOrDefault(response, 0L);
    }

    public void setMagic(@NonNull MagicType magicType, int lv) {
        if(lv == 0) {
            this.magic.remove(magicType);
        } else {
            if(lv < Config.MIN_MAGIC_LV || lv > Config.MAX_MAGIC_LV) {
                throw new NumberRangeException(lv, Config.MIN_MAGIC_LV, Config.MAX_MAGIC_LV);
            }

            this.magic.put(magicType, lv);
        }
    }

    public int getMagic(@NonNull MagicType magicType) {
        return this.magic.getOrDefault(magicType, Config.MIN_MAGIC_LV);
    }

    public void addMagic(@NonNull MagicType magicType, int lv) {
        this.setMagic(magicType, this.getMagic(magicType) + lv);
    }

    public void setResist(@NonNull MagicType magicType, int lv) {
        if(lv == 0) {
            this.resist.remove(magicType);
        } else {
            if(lv < Config.MIN_MAGIC_LV || lv > Config.MAX_MAGIC_LV) {
                throw new NumberRangeException(lv, Config.MIN_MAGIC_LV, Config.MAX_MAGIC_LV);
            }

            this.resist.put(magicType, lv);
        }
    }

    public int getResist(@NonNull MagicType magicType) {
        return this.resist.getOrDefault(magicType, Config.MIN_MAGIC_LV);
    }

    public void addResist(@NonNull MagicType magicType, int lv) {
        this.setResist(magicType, this.getResist(magicType) + lv);
    }

    public void setCloseRate(long npcId, int closeRate) {
        Config.checkId(Id.NPC, npcId);

        this.addLog(LogData.TOTAL_CLOSERATE, closeRate - this.getCloseRate(npcId));

        if(closeRate == 0) {
            this.closeRate.remove(npcId);
        } else {
            this.closeRate.put(npcId, closeRate);
            this.setLog(LogData.MAX_CLOSERATE, Math.max(this.getLog(LogData.MAX_CLOSERATE), closeRate));
        }
    }

    public int getCloseRate(long npcId) {
        return this.closeRate.getOrDefault(npcId, 0);
    }

    public void addCloseRate(long npcId, int closeRate) {
        this.setCloseRate(npcId, this.getCloseRate(npcId) + closeRate);
    }

    public void setLog(@NonNull LogData logData, long count) {
        if(count < 0) {
            throw new NumberRangeException(count, 0);
        }

        if(count == 0) {
            this.log.remove(logData);
        } else {
            this.log.put(logData, count);
        }

        Logger.i("LogData", this.getName() + " {" + logData.toString() + " : " + count + "}");
    }

    public long getLog(@NonNull LogData logData) {
        return this.log.getOrDefault(logData, 0L);
    }

    public void addLog(@NonNull LogData logData, long count) {
        this.setLog(logData, this.getLog(logData) + count);
        this.log.put(LogData.LOG_COUNT, this.getLog(LogData.LOG_COUNT) + 1);
    }

    public boolean canEquip(long equipId) {
        Equipment equipment = null;
        boolean flag;

        try {
            equipment = Config.loadObject(Id.EQUIPMENT, equipId);
            flag = equipment.getTotalLimitLv().isInRange(this.lv.get()) &&
                    this.checkStatRange(equipment.getLimitStat().getMin(), equipment.getLimitStat().getMax());
        } finally {
            if(equipment != null) {
                Config.unloadObject(equipment);
            }
        }

        return flag;
    }

    public boolean canReinforce(long equipId, Map<StatType, Integer> increaseLimitStat) {
        if(this.getEquipInventory().contains(equipId)) {
            Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);

            boolean flag = equipment.getReinforceCount().get() < Config.MAX_REINFORCE_COUNT;

            if(flag && this.getEquip(equipment.getEquipType()) == equipId) {
                Map<StatType, Integer> minStat = new HashMap<>(equipment.getLimitStat().getMin());
                Map<StatType, Integer> maxStat = new HashMap<>(equipment.getLimitStat().getMax());

                StatType statType;
                Integer value;
                int stat;

                for(Map.Entry<StatType, Integer> entry : increaseLimitStat.entrySet()) {
                    statType = entry.getKey();
                    stat = entry.getValue();

                    value = minStat.get(statType);
                    value = value == null ? 0 : value;
                    minStat.put(statType, value + stat);

                    value = maxStat.get(statType);
                    value = value == null ? 0 : value;
                    maxStat.put(statType, value + stat);
                }

                return equipment.getTotalLimitLv().isInRange(this.lv.get()) && this.checkStatRange(minStat, maxStat);
            }

            return flag;
        } else {
            return false;
        }
    }

    public boolean reinforce(long equipId, Map<StatType, Integer> increaseReinforceStat,
                             Map<StatType, Integer> increaseMinLimitStat, Map<StatType, Integer> increaseMaxLimitStat) {
        this.setDoing(Doing.REINFORCE);
        this.addLog(LogData.REINFORCE_TRIED, 1);

        Random random = new Random();
        double percent = random.nextDouble();

        try {
            Thread.sleep(random.nextInt(5000));
        } catch (InterruptedException e) {
            Logger.e("Player.reinforce", e);
            throw new RuntimeException(e.getMessage());
        } finally {
            this.setDoing(Doing.NONE);
        }

        Equipment equipment = null;

        try {
            equipment = Config.loadObject(Id.EQUIPMENT, equipId);
            double reinforcePercent = equipment.getReinforcePercent();

            if(percent < reinforcePercent) {
                equipment.successReinforce(increaseReinforceStat, increaseMinLimitStat, increaseMaxLimitStat);

                this.addLog(LogData.REINFORCE_SUCCEED, 1);

                int reinforceCount = equipment.reinforceCount.get();
                this.replyPlayer("강화에 성공헀습니다!\n" + reinforceCount + "강 -> " + (reinforceCount + 1) + "강");

                return true;
            } else {
                equipment.failReinforce();
                this.replyPlayer("강화에 실패하였습니다...",
                        "현재 강화 천장 : " + equipment.reinforceFloor.get() + "\n" +
                                "다음 강화 확률 : " + Config.getDisplayPercent(equipment.getReinforcePercent()) + "%");
                return false;
            }
        } finally {
            if (equipment != null) {
                Config.unloadObject(equipment);
            }
        }
    }

    @Override
    public boolean setMoney(long money) {
        money *= Config.MONEY_BOOST;
        money *= this.moneyBoost.get();

        long gap = money - this.getMoney();
        boolean isCancelled = super.setMoney(money);

        if(!isCancelled) {
            this.addLog(LogData.TOTAL_MONEY, gap);

            if(gap > 0) {
                if (this.getLog(LogData.MAX_MONEY) < money) {
                    this.setLog(LogData.MAX_MONEY, money);
                }
            } else if(gap < 0) {
                gap *= -1;

                if(this.getLog(LogData.MAX_PAYMENT) < gap) {
                    this.setLog(LogData.MAX_PAYMENT, gap);
                }
            }
        }

        return isCancelled;
    }

    @Override
    public boolean setField(int fieldX, int fieldY, int distance) {
        boolean isCancelled = super.setField(fieldX, fieldY, distance);

        if(!isCancelled) {
            this.addLog(LogData.FIELD_MOVE_DISTANCE, distance);
        }

        return isCancelled;
    }

    @Override
    public boolean setMap(int x, int y, int fieldX, int fieldY, int distance, boolean isToBase) {
        boolean isCancelled = super.setMap(x, y, fieldX, fieldY, distance, isToBase);

        if(!isCancelled) {
            this.addLog(LogData.MAP_MOVE_DISTANCE, distance);
        }

        return isCancelled;
    }

    @Override
    public void setBuff(long time, @NonNull StatType statType, int stat) {
        super.setBuff(time, statType, stat);
        this.addLog(LogData.BUFF_RECEIVED, 1);
    }

    @Override
    public void onDeath() {
        this.endFight();

        this.setBasicStat(StatType.HP, 1);
        this.location.set(this.baseLocation);

        this.getBuff().clear();
        this.getBuffStat().clear();

        Random random = new Random();
        double loseMoneyPercent = random.nextDouble() * Config.TOTAL_MONEY_LOSE_RANDOM + Config.TOTAL_MONEY_LOSE_MIN;
        double dropPercent = random.nextDouble() * Config.MONEY_DROP_RANDOM + Config.MONEY_DROP_MIN;
        int dropItem = random.nextInt(Config.ITEM_DROP);

        long totalLoseMoney = (long) (this.getMoney() * loseMoneyPercent);
        long dropMoney = (long) (totalLoseMoney * dropPercent);
        long loseMoney = totalLoseMoney - dropMoney;

        this.dropMoney(dropMoney);
        this.addMoney(loseMoney);

        StringBuilder dropItemBuilder = new StringBuilder("G\n\n---떨어트린 아이템 목록---\n");
        StringBuilder loseItemBuilder = new StringBuilder("\n---잃어버린 아이템 목록---\n");

        List<Long> keys;
        long itemId;
        int count;
        for(int i = 0; i < dropItem; i++) {
            keys = new ArrayList<>(this.inventory.keySet());
            itemId = keys.get(random.nextInt(keys.size()));
            count = random.nextInt(this.getItem(itemId));

            Item item = Config.getData(Id.ITEM, itemId);
            String itemName = item.getName();

            if(random.nextDouble() < Config.ITEM_DROP_PERCENT) {
                this.dropItem(itemId, count);

                dropItemBuilder.append(itemName);
                dropItemBuilder.append(" ");
                dropItemBuilder.append(count);
                dropItemBuilder.append("개\n");
            } else {
                this.addItem(itemId, count);

                loseItemBuilder.append(itemName);
                loseItemBuilder.append(" ");
                loseItemBuilder.append(count);
                loseItemBuilder.append("개\n");
            }
        }

        String innerMsg = "떨어트린 돈 : " + dropMoney + "G\n잃어버린 돈 : " + loseMoney
                + dropItemBuilder.toString() + loseItemBuilder.toString();
        this.replyPlayer("사망했습니다...", innerMsg);
        this.addLog(LogData.DEATH, 1);
    }

    @Override
    public void onKill(Entity entity) {
        long prevExp = this.exp.get();
        long exp = getKillExp(entity.lv.get());
        this.addExp(exp);

        this.replyPlayer(entity.getName() + "을 처치했습니다!\n경험치 : " + prevExp + " -> " + this.exp.get(),
                "획득한 경험치 : " + exp);
    }

    @Override
    public void setItem(long itemId, int count) {
        int currentItem = this.getItem(itemId);
        super.setItem(itemId, count);

        this.addLog(LogData.TOTAL_ITEM, count - currentItem);
    }

    @Override
    public boolean canFight(@NonNull Entity enemy) {
        List<Doing> doingList = Doing.fightList();

        if(doingList.contains(this.doing) && this.isPvp()) {
            if(enemy.getId().getId().equals(Id.PLAYER) && !((Player) enemy).isPvp()) {
                return false;
            }

            return doingList.contains(enemy.getDoing());
        } else {
            return false;
        }
    }

    @Override
    public boolean startFight(@NonNull Set<Entity> enemies) {
        if(super.startFight(enemies)) {
            this.replyPlayer("적 " + enemies.size() + "명 과의 전투가 시작되었습니다", this.getBattleMsg());
            return true;
        } else {
            return false;
        }
    }

}
