package lkd.namsic.Game.GameObject;

import android.app.Notification;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    public void eat(long itemId) {
        Item item = null;

        try {
            item = Config.loadObject(Id.ITEM, itemId);

            if(item.isFood()) {
                this.addItem(itemId, -1);
                this.addLog(LogData.EAT, 1);

                this.setSkipRevalidate(true);

                for(Map.Entry<Long, HashMap<StatType, Integer>> entry : item.getEatBuff().entrySet()) {
                    for(Map.Entry<StatType, Integer> statEntry : entry.getValue().entrySet()) {
                        this.addBuff(entry.getKey(), statEntry.getKey(), statEntry.getValue());
                    }
                }

                this.setSkipRevalidate(false);
            } else {
                throw new WeirdDataException(Id.ITEM, itemId);
            }
        } finally {
            if(item != null) {
                Config.unloadObject(item);
            }
        }
    }

    public void addExp(long exp) {
        if(exp < 0) {
            throw new NumberRangeException(0, 0, Long.MAX_VALUE);
        }

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
        int lv = this.lv.get();

        long needExp;
        if(lv <= 100) {
            needExp =  100 + (lv * 100);
        } else if(lv <= 500) {
            needExp =  (long) Math.pow(lv, 3.2);
        } else if(lv <= 850) {
            needExp =  lv * 12_000_000 - 5_500_000_000L;
        } else {
            needExp =  (long) Math.pow(lv, 3.3) + (lv * 10_000_000) - 7_910_000_000L;
        }

        return needExp - this.getExp().get();
    }

    public void lvUp(long requiredExp) {
        this.lv.add(1);
        this.sp.add(5);
        this.exp.add(-1 * requiredExp);
    }

    @Nullable
    public String canStartChat(Chat chat) {
        String failMsg = "";

        long needMoney = chat.getNeedMoney().get();
        if(needMoney != 0 && this.getMoney() < needMoney) {
            failMsg += "보유 골드가 부족합니다(부족한 금액 : " + (needMoney - this.getMoney()) + "G)\n";
        }

        long questId = chat.getQuestId().get();
        if (!this.canAddQuest(questId)) {
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
            failMsg += "스텟이 부족합니다(총 부족한 스텟 : " + missingCount + ")";
        }

        if(failMsg.equals("")) {
            return null;
        } else {
            return failMsg;
        }
    }

    public void startChat(long chatId) {
        Config.checkId(Id.CHAT, chatId);

        Chat chat = null;
        try {
            chat = Config.loadObject(Id.CHAT, chatId);

            Notification.Action session = getSession();
            String failMsg = canStartChat(chat);

            if(failMsg == null) {
                this.addMoney(-1 * chat.getNeedMoney().get());

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

                this.addQuest(finalChat.getQuestId().get());
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

                this.setSkipRevalidate(true);

                for (StatType statType : research.getRewardStat().keySet()) {
                    this.addBasicStat(statType, research.getRewardStat(statType));
                }

                this.setSkipRevalidate(false);
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
                    && quest.limitStat.isInRange(this.stat);

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

    @NonNull
    public Player clearQuest(long questId) {
        Long chatId = this.quest.get(questId);

        if(chatId == null) {
            throw new ObjectNotFoundException(Id.QUEST, questId);
        }

        Quest quest = null;

        try {
            quest = Config.loadObject(Id.QUEST, questId);

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
            if(questNpcId != 0) {
                ConcurrentHashSet<Long> questNpc = Objects.requireNonNull(this.questNpc.get(questNpcId));

                if(questNpc.size() == 1) {
                    this.questNpc.remove(questId);
                } else {
                    questNpc.remove(questId);
                }
            }

            this.startChat(chatId);
        } finally {
            if(quest != null) {
                Config.unloadObject(quest);
            }
        }

        return this;
    }

    public int getClearedQuest(long questId) {
        Integer value = this.clearedQuest.get(questId);

        if(value != null) {
            return value;
        } else {
            return 0;
        }
    }

    public void setCloseRate(@NonNull Map<Long, Integer> closeRate) {
        for(Map.Entry<Long, Integer> entry : closeRate.entrySet()) {
            this.setCloseRate(entry.getKey(), entry.getValue());
        }
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
        Integer value = this.closeRate.get(npcId);

        if(value != null) {
            return value;
        } else {
            return 0;
        }
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
    }

    public long getLog(@NonNull LogData logData) {
        Long value = this.log.get(logData);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
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
                    Config.compareMap(equipment.getLimitStat(), this.basicStat, false);
        } finally {
            if(equipment != null) {
                Config.unloadObject(equipment);
            }
        }

        return flag;
    }

    @Override
    public boolean setMoney(long money) {
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
    public void death() {
        this.endFight();

        this.setStat(StatType.HP, 1);
        this.location.set(this.baseLocation);

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
    public void revalidateStat() {
        super.revalidateStat();
        this.addLog(LogData.STAT_UPDATED, 1);
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
