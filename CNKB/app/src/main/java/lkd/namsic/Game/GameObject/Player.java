package lkd.namsic.Game.GameObject;

import android.app.Notification;
import android.util.Log;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import lkd.namsic.Game.Base.ConcurrentHashSet;
import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Base.LimitLong;
import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Doing;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.LogData;
import lkd.namsic.Game.Enum.MagicType;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Enum.WaitResponse;
import lkd.namsic.Game.Event.ItemUseEvent;
import lkd.namsic.Game.Exception.NumberRangeException;
import lkd.namsic.Game.Exception.ObjectNotFoundException;
import lkd.namsic.Game.Exception.WeirdDataException;
import lkd.namsic.Service.KakaoTalk;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Player extends Entity {

    @NonNull
    String sender;

    @Setter
    @NonNull
    String nickName;

    @NonNull
    String image;

    Set<String> title = new ConcurrentHashSet<String>() {{
        add("초심자");
    }};

    @NonNull
    String currentTitle = "초심자";

    @Setter
    @NonNull
    String recentRoom;

    @Setter
    boolean pvp = false;

    @Setter
    boolean isGroup = true;

    private long lastTime;
    LimitLong runTime = new LimitLong(0L, 0L, Long.MAX_VALUE);

    Location baseLocation = new Location();

    LimitInteger sp = new LimitInteger(0, Config.MIN_SP, Config.MAX_SP);
    LimitInteger adv = new LimitInteger(0, 0, Integer.MAX_VALUE);
    LimitLong exp = new LimitLong(0, 0L, Long.MAX_VALUE);

    @Setter
    int lastDay = LocalDateTime.now().getDayOfMonth();

    Set<Long> achieve = new ConcurrentHashSet<>();
    Set<Long> research = new ConcurrentHashSet<>();

    Map<Long, Long> quest = new ConcurrentHashMap<>();
    Map<Long, ConcurrentHashSet<Long>> questNpc = new ConcurrentHashMap<>();
    Map<Long, Integer> clearedQuest = new ConcurrentHashMap<>();

    Map<WaitResponse, Long> responseChat = new ConcurrentHashMap<>();
    Map<String, Long> anyResponseChat = new ConcurrentHashMap<>();

    Map<MagicType, Integer> magic = new ConcurrentHashMap<>();
    Map<MagicType, Integer> resist = new ConcurrentHashMap<>();

    Map<Long, Integer> closeRate = new ConcurrentHashMap<>();

    Map<LogData, Long> log = new ConcurrentHashMap<LogData, Long>() {{
        put(LogData.PLAYED_DAY, 1L);
    }};

    public Player(@NonNull String sender, @NonNull String name, @NonNull String nickName,
                  @NonNull String image, @NonNull String recentRoom, long lastTime) {
        super(name);
        this.id.setId(Id.PLAYER);

        this.sender = sender;
        this.nickName = nickName;
        this.image = image;
        this.recentRoom = recentRoom;
        this.lastTime = lastTime;
    }

    //[테스트 칭호] 남식(Lv.123)
    @NonNull
    @Override
    public String getName() {
        return "[" + this.getCurrentTitle() + "] " + this.getNickName() + " (Lv." + this.getLv() + ")";
    }

    public void replyPlayer(@NonNull String msg) {
        KakaoTalk.reply(this.getSession(), this.getName() + "\n" + msg);
    }

    public void replyPlayer(@NonNull String msg, @NonNull String innerMsg) {
        KakaoTalk.reply(this.getSession(), this.getName() + "\n" + msg, innerMsg);
    }

    public boolean useItem(long itemId, @NonNull List<Entity> other) {
        Config.checkId(Id.ITEM, itemId);

        if(this.getItem(itemId) == 0) {
            throw new ObjectNotFoundException(Id.ITEM, itemId);
        }

        Item item = null;
        boolean isCancelled;

        try {
            item = Config.loadObject(Id.ITEM, itemId);

            if (item.getUse() != null) {
                isCancelled = ItemUseEvent.handleEvent(this.events.get(ItemUseEvent.getName()), new Object[]{item});

                if (!isCancelled) {
                    Use use = item.getUse();

                    if(use != null) {
                        this.addLog(LogData.TOTAL_ITEM_USED, 1);
                        use.use(this, other);
                    }
                }
            } else {
                Config.unloadObject(item);
                throw new WeirdDataException(Id.ITEM, itemId);
            }
        } finally {
            if(item != null) {
                Config.unloadObject(item);
            }
        }

        return isCancelled;
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
        builder.append("=공격 [대상] : [\"대상\" 또는 \"가장 가까운 적\"] 에게 기본 공격을 합니다\n");
        builder.append("=방어 : 다음 피해가 기본 공격일 경우, 공격을 방어하고 체력과 마나를 일부 회복합니다\n");
        builder.append("=이동 (e/w/s/n) [1~3] : 동/서/남/북의 방향으로 [\"3칸\" 또는 \"지정한 거리\"] 만큼 최대한 이동합니다\n");
        builder.append("=필드 : 필드 정보를 표시합니다\n");
        builder.append("=스킬 목록 [o] : [\"보유한 또는 \"사용 가능한\"] 스킬 목록을 확인합니다\n");
        builder.append("=스킬 사용 (스킬 번호) [대상] : 스킬 번호에 해당하는 스킬을 [\"대상\" 또는 \"가장 가까운 적\"] 에게 사용합니다\n");
        builder.append("=아이템 사용 (아이템 번호) (대상) : 아이템 번호에 해당하는 아이템을 대상(본인 : \"s\")에게 사용합니다\n");
        builder.append("=도망 : 거점으로의 도망을 시도합니다. 실패 시 최대 체력의 10%에 해당하는 피해를 입습니다");
        builder.append("(사망하지는 않습니다)(실패 시 15초간 사용이 불가능해집니다)\n");
        builder.append("=카운터 [대상] : (보스한정) [\"대상\" 또는 \"가장 가까운 보스\"] 의 스킬(집중 스킬 한정)을 반사합니다");

        return builder.toString();
    }

    public boolean canEat(long itemId) {
        if(this.getItem(itemId) > 0) {
            Item item = null;

            try {
                item = Config.loadObject(Id.ITEM, itemId);
                return item.isFood();
            } finally {
                if(item != null) {
                    Config.unloadObject(item);
                }
            }
        } else {
            return false;
        }
    }
    public void startChat(long chatId) {
        Config.checkId(Id.CHAT, chatId);

        Chat chat = null;
        try {
            chat = Config.loadObject(Id.CHAT, chatId);

            Notification.Action session = getSession();
            boolean canContinue = true;
            String failMsg = "";

            long needMoney = chat.getNeedMoney().get();
            if(needMoney != 0 && this.getMoney() < needMoney) {
                canContinue = false;
                failMsg += "보유 골드가 부족합니다(부족한 금액 : " + (needMoney - this.getMoney()) + "G)\n";
            }

            long questId = chat.getQuestId().get();
            if (!this.canAddQuest(questId)) {
                canContinue = false;
                failMsg += "퀘스트 수령이 불가능합니다(상세 내용은 퀘스트 검색(아이디 : " + questId + ")\n";
            }

            int diff;
            int missingCount = 0;
            for (Map.Entry<Long, Integer> entry : chat.getNeedItem().entrySet()) {
                diff = entry.getValue() - this.getItem(entry.getKey());

                if (diff > 0) {
                    missingCount += diff;
                }
            }

            if (missingCount != 0) {
                canContinue = false;
                failMsg += "보유 아이템이 부족합니다(총 부족한 아이템 개수 : " + missingCount + ")\n";
            }

            missingCount = 0;
            for(Map.Entry<StatType, Integer> entry : chat.getNeedStat().entrySet()) {
                diff = entry.getValue() - this.getStat(entry.getKey());

                if(diff > 0) {
                    missingCount += diff;
                }
            }

            if(missingCount != 0) {
                canContinue = false;
                failMsg += "스텟이 부족합니다(총 부족한 스텟 : " + missingCount + ")";
            }

            if(canContinue) {
                this.addMoney(-1 * needMoney);

                for(Map.Entry<Long, Integer> entry : chat.getNeedItem().entrySet()) {
                    this.addItem(entry.getKey(), -1 * entry.getValue());
                }

                for(Map.Entry<StatType, Integer> entry : chat.getNeedStat().entrySet()) {
                    this.addBasicStat(entry.getKey(), -1 * entry.getValue());
                }
            } else {
                this.replyPlayer(failMsg);
                return;
            }

            this.setDoing(Doing.CHAT);

            AtomicReference<String> exception = new AtomicReference<>(null);
            final Chat finalChat = chat;
            Thread thread = new Thread(() -> {
                this.addMoney(finalChat.getRewardMoney().get());

                for(Map.Entry<Long, Integer> entry : finalChat.getNeedItem().entrySet()) {
                    this.addItem(entry.getKey(), entry.getValue());
                }

                for(Map.Entry<StatType, Integer> entry : finalChat.getNeedStat().entrySet()) {
                    this.addBasicStat(entry.getKey(), entry.getValue());
                }

                long pauseTime = finalChat.getPauseTime().get();
                for(String text : finalChat.getText()) {
                    KakaoTalk.reply(session, text);

                    try {
                        Thread.sleep(pauseTime);
                    } catch (InterruptedException e) {
                        Log.e("Player.startChat - chat Thread", Config.errorString(e));
                        exception.set(e.getMessage());
                        break;
                    }
                }

                this.addQuest(questId);
                this.setMap(finalChat.getTpLocation(), false);

                if(finalChat.getResponseChat().isEmpty() && finalChat.getAnyResponseChat().isEmpty()) {
                    this.setDoing(Doing.NONE);
                } else {
                    this.setDoing(Doing.WAIT_RESPONSE);

                    this.responseChat = new ConcurrentHashMap<>(finalChat.getResponseChat());
                    this.anyResponseChat = new ConcurrentHashMap<>(finalChat.getAnyResponseChat());
                }
            });

            thread.start();
            this.addLog(LogData.CHAT, 1);

            String exceptionMsg = exception.get();
            if(exceptionMsg != null) {
                throw new RuntimeException(exceptionMsg);
            }
        } finally {
            if(chat != null) {
                Config.unloadObject(chat);
            }
        }
    }
