package lkd.namsic.game.gameObject;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.LimitLong;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MagicType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.enums.object_list.ItemList;
import lkd.namsic.game.exception.InvalidNumberException;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.manager.MoveManager;
import lkd.namsic.game.manager.QuestManager;
import lkd.namsic.setting.Logger;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Player extends Entity {

    private final static QuestManager questManager = QuestManager.getInstance();
    private final static MoveManager moveManager = MoveManager.getInstance();

    public static void replyPlayers(@NonNull Set<Player> players, @Nullable String msg) {
        replyPlayers(players, msg, null);
    }

    public static void replyPlayersExcept(@NonNull Set<Player> players, @Nullable String msg, @Nullable Player except) {
        replyPlayersExcept(players, msg, null, except);
    }

    public static void replyPlayersExcepts(@NonNull Set<Player> players, @Nullable String msg, @NonNull Set<Player> excepts) {
        replyPlayersExcepts(players, msg, null, excepts);
    }

    public static void replyPlayers(@NonNull Set<Player> players, @Nullable String msg, @Nullable String innerMsg) {
        replyPlayersExcept(players, msg, innerMsg, null);
    }

    public static void replyPlayersExcept(@NonNull Set<Player> players, @Nullable String msg,
                                          @Nullable String innerMsg, @Nullable Player except) {
        Set<Player> excepts = new HashSet<>();
        excepts.add(except);
        replyPlayersExcepts(players, msg, innerMsg, excepts);
    }

    public static void replyPlayersExcepts(@NonNull Set<Player> players, @Nullable String msg,
                                          @Nullable String innerMsg, @NonNull Set<Player> excepts) {
        Map<Notification.Action, Set<Player>> sessions = new HashMap<>();

        Set<Player> playerSet;
        for(Player player : players) {
            if(excepts.contains(player)) {
                continue;
            }

            Notification.Action session = player.getSession();
            playerSet = sessions.get(session);

            if(playerSet == null) {
                playerSet = new HashSet<>();
                playerSet.add(player);
                sessions.put(session, playerSet);
            } else {
                playerSet.add(player);
            }
        }

        for(Map.Entry<Notification.Action, Set<Player>> entry : sessions.entrySet()) {
            playerSet = entry.getValue();

            if(playerSet.size() == 1) {
                ((Player) playerSet.toArray()[0]).replyPlayer(msg, innerMsg);
                continue;
            }

            StringBuilder builder = new StringBuilder("[");
            for(Player player : playerSet) {
                builder.append(player.getNickName())
                        .append(", ");
            }

            KakaoTalk.reply(entry.getKey(), builder.substring(0, builder.length() - 2) + "]\n" + msg);
        }
    }

    @NonNull
    final LocalDateTime created = LocalDateTime.now();

    @NonNull
    final String sender;

    @Setter
    @NonNull
    String nickName;

    @NonNull
    final String image;

    @Setter
    Doing prevDoing = Doing.NONE;

    final Set<String> title = new ConcurrentHashSet<>();

    @NonNull
    String currentTitle = "";

    @Setter
    @NonNull
    String recentRoom;

    boolean pvp = true;

    int pvpEnableYear = 0;
    int pvpEnableDay = 0;
    boolean pvpEnabled = false;
    boolean checkPvp = false;

    @Setter
    boolean isGroup = true;

    final Location baseLocation = new Location();

    final LimitInteger sp = new LimitInteger(0, Config.MIN_SP, Config.MAX_SP);
    final LimitInteger adv = new LimitInteger(0, 0, null);
    final LimitLong exp = new LimitLong(0, 0L, null);

    @Setter
    int lastYear = LocalDateTime.now().getYear();

    @Setter
    int lastDay = LocalDateTime.now().getDayOfYear();

    final Set<Long> achieve = new ConcurrentHashSet<>();
    final Set<Long> research = new ConcurrentHashSet<>();

    final Set<Long> itemRecipe = new ConcurrentHashSet<>();
    final Set<Long> equipRecipe = new ConcurrentHashSet<>();

    //QuestId - Clear ChatId
    final Map<Long, Long> quest = new ConcurrentHashMap<>();
    final Map<Long, ConcurrentHashSet<Long>> questNpc = new ConcurrentHashMap<>();
    final Map<Long, Integer> clearedQuest = new ConcurrentHashMap<>();

    final Map<Long, Long> chatCount = new ConcurrentHashMap<>();
    final Map<WaitResponse, Long> responseChat = new ConcurrentHashMap<>();
    final Map<String, Long> anyResponseChat = new ConcurrentHashMap<>();

    @Setter
    long waitNpcId;

    final Map<MagicType, Integer> magic = new ConcurrentHashMap<>();
    final Map<MagicType, Integer> resist = new ConcurrentHashMap<>();

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
        this.setBasicStat(StatType.ATK, 10);
    }

    @NonNull
    public String getDisplayLv() {
        return this.lv.get() + "Lv (" + this.exp.get() + "/" + this.getNeedExp() + ")";
    }

    public void addTitle(@NonNull String title) {
        this.title.add(title);
    }

    public void setCurrentTitle(@NonNull String title) {
        if(this.title.contains(title)) {
            this.currentTitle = title;
        } else {
            throw new ObjectNotFoundException("Title : " + title);
        }
    }

    public void replyPlayer(@Nullable String msg) {
        KakaoTalk.reply(this.getSession(), this.getName() + "\n" + msg);
    }

    public void replyPlayer(@Nullable String msg, @Nullable String innerMsg) {
        msg = msg == null ? "" : msg;
        KakaoTalk.reply(this.getSession(), this.getName() + "\n" + msg, innerMsg);
    }

    public void checkTime() {
        if(this.checkPvp) {
            LocalDateTime now = LocalDateTime.now();
            int year = now.getYear();
            int day = now.getDayOfYear();

            if(year >= this.pvpEnableDay || day >= this.pvpEnableDay) {
                this.setPvp(true, null);
                this.pvpEnabled = true;
            }
        }
    }

    public void checkNewDay() {
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfYear();
        int year = now.getYear();

        if(day != this.lastDay || year != this.lastYear) {
            this.lastDay = day;
            this.lastYear = year;
            this.newDay();
        }

        if(this.pvpEnabled) {
            LocalDateTime pvpEnabledTime = LocalDateTime.of(this.pvpEnableYear, 1, 1, 0, 0)
                    .plusDays(this.pvpEnableDay);
            replyPlayer("PvP가 " + pvpEnabledTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "에 활성화되었습니다");
            this.pvpEnabled = false;
        }
    }

    public void newDay() {
        //출석 보상
        this.replyPlayer("New Day!");
    }

    @NonNull
    public Notification.Action getSession() {
        if(this.isGroup) {
            return KakaoTalk.getGroupSession(this.recentRoom);
        } else {
            return KakaoTalk.getSoloSession(this.recentRoom);
        }
    }

    public void addExp(long exp) {
        if(exp < 0) {
            throw new NumberRangeException(0, 0);
        }

        exp *= Config.EXP_BOOST;

        this.exp.add(exp);
        this.addLog(LogData.TOTAL_EXP, exp);

        int startLv = this.lv.get();
        int endLv;

        if(startLv == Config.MAX_LV) {
            return;
        }

        long needExp, currentExp;
        while(true) {
            needExp = this.getNeedExp();
            currentExp = this.exp.get();

            if(needExp < currentExp) {
                this.lvUp(needExp);
            } else {
                endLv = this.lv.get();

                if(startLv != endLv) {
                    this.setBasicStat(StatType.HP, this.getStat(StatType.MAXHP));
                    this.setBasicStat(StatType.MN, this.getStat(StatType.MAXMN));
                    this.replyPlayer("레벨 업!(" + startLv + "->" + endLv + ")\n" +
                                    Emoji.EXP + " 경험치: " + this.exp.get() + "\n" +
                                    Emoji.SP + " 스텟 포인트: " + this.sp.get(),
                            "레벨업을 하여 체력과 마나가 모두 회복됩니다");
                }

                break;
            }
        }
    }

    public long getNeedExp() {
        return this.getNeedExp(this.lv.get());
    }

    public long getNeedExp(int lv) {
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
        long exp = 5000 + (long) ((this.getNeedExp(enemyLv) * 0.00001) + (this.getNeedExp() * 0.00001));

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

    public void lvUp(long needExp) {
        this.lv.add(1);
        this.sp.add(5);
        this.exp.add(-1 * needExp);
    }

    public void setPvp(boolean enable, @Nullable Integer day) {
        if(enable) {
            this.pvp = true;
            this.checkPvp = false;
            this.pvpEnableYear = 0;
            this.pvpEnableDay = 0;
            
            this.replyPlayer("PvP를 활성화했습니다");
        } else {
            if(day == null) {
                throw new NullPointerException();
            }

            if(day != 1 && day != 7) {
                throw new InvalidNumberException(day);
            }

            LocalDateTime enableTime = LocalDateTime.now().plusDays(day + 1);

            this.pvp = false;
            this.checkPvp = true;
            this.pvpEnableYear = enableTime.getYear();
            this.pvpEnableDay = enableTime.getDayOfYear();

            this.replyPlayer("PvP를 " + day + "일간 비활성화 했습니다");
        }
    }

    @NonNull
    public Player addAchieve(long achieveId) {
        Config.checkId(Id.ACHIEVE, achieveId);

        if(this.achieve.add(achieveId)) {
            Achieve achieve = Config.getData(Id.ACHIEVE, achieveId);

            this.addMoney(achieve.getRewardMoney().get());
            this.addExp(achieve.rewardExp.get());
            this.adv.add(achieve.rewardAdv.get());

            StringBuilder innerMsg = new StringBuilder("---친밀도 현황---");

            if(achieve.rewardCloseRate.isEmpty()) {
                innerMsg.append("\n변경된 친밀도가 없습니다");
            } else {
                long npcId;
                int closeRate;

                for(Map.Entry<Long, Integer> entry : achieve.rewardCloseRate.entrySet()) {
                    npcId = entry.getKey();
                    closeRate = entry.getValue();

                    this.addCloseRate(npcId, closeRate);

                    Npc npc = Config.getData(Id.NPC, npcId);
                    innerMsg.append("\n")
                            .append(npc.getName())
                            .append(": ")
                            .append(Config.getIncrease(closeRate));
                }
            }

            innerMsg.append("\n\n---아이템 현황---");

            if(achieve.rewardItem.isEmpty()) {
                innerMsg.append("\n변경된 아이템이 없습니다");
            } else {
                long itemId;
                int count;

                for(Map.Entry<Long, Integer> entry : achieve.rewardItem.entrySet()) {
                    itemId = entry.getKey();
                    count = entry.getValue();

                    this.addItem(itemId, count, false);

                    Item item = Config.getData(Id.ITEM, itemId);
                    innerMsg.append("\n")
                            .append(item.getName())
                            .append(": ")
                            .append(Config.getIncrease(count));
                }
            }

            innerMsg.append("\n\n---스텥 현황---");

            if(achieve.rewardStat.isEmpty()) {
                innerMsg.append("\n변경된 스텟이 없습니다");
            } else {
                StatType statType;
                int count;

                for(Map.Entry<StatType, Integer> entry : achieve.rewardStat.entrySet()) {
                    statType = entry.getKey();
                    count = entry.getValue();

                    this.addBasicStat(statType, count);
                    innerMsg.append("\n")
                            .append(statType.getDisplayName())
                            .append(": ")
                            .append(Config.getIncrease(count));
                }
            }

            String msg = achieve.name + " 업적을 획득하였습니다!";
            this.replyPlayer(msg, innerMsg.toString());

            //TODO: 서비스 시 주석 해제
//          KakaoTalk.replyGroup(this.getName() + "님이 " + msg);
        }

        return this;
    }

    public boolean canAddResearch(long researchId) {
        Config.checkId(Id.RESEARCH, researchId);

        Research research = Config.getData(Id.RESEARCH, researchId);
        return research.getLimitLv().isInRange(this.lv.get()) && research.getNeedMoney().get() <= this.getMoney()
                    && Config.compareMap(this.inventory, research.needItem, true);
    }

    public void addResearch(long researchId) {
        if(this.research.add(researchId)) {
            Research research = Config.getData(Id.RESEARCH, researchId);

            this.addMoney(-1 * research.getNeedMoney().get());

            this.addExp(research.rewardExp.get());
            this.adv.add(research.rewardAdv.get());

            StringBuilder innerMsg = new StringBuilder("---친밀도 현황---");

            if(research.rewardCloseRate.isEmpty()) {
                innerMsg.append("\n변경된 친밀도가 없습니다");
            } else {
                long npcId;
                int closeRate;

                for(Map.Entry<Long, Integer> entry : research.rewardCloseRate.entrySet()) {
                    npcId = entry.getKey();
                    closeRate = entry.getValue();

                    this.addCloseRate(npcId, closeRate);

                    Npc npc = Config.getData(Id.NPC, npcId);
                    innerMsg.append("\n")
                            .append(npc.getName())
                            .append(": ")
                            .append(Config.getIncrease(closeRate));
                }
            }

            innerMsg.append("\n\n---아이템 현황---");

            if(research.rewardItem.isEmpty()) {
                innerMsg.append("\n변경된 아이템이 없습니다");
            } else {
                long itemId;
                int count;

                for(Map.Entry<Long, Integer> entry : research.rewardItem.entrySet()) {
                    itemId = entry.getKey();
                    count = entry.getValue();

                    this.addItem(itemId, count, false);

                    Item item = Config.getData(Id.ITEM, itemId);
                    innerMsg.append("\n")
                            .append(item.getName())
                            .append(": ")
                            .append(Config.getIncrease(count));
                }
            }

            innerMsg.append("\n\n---스텥 현황---");

            if(research.rewardStat.isEmpty()) {
                innerMsg.append("\n변경된 스텟이 없습니다");
            } else {
                StatType statType;
                int count;

                for(Map.Entry<StatType, Integer> entry : research.rewardStat.entrySet()) {
                    statType = entry.getKey();
                    count = entry.getValue();

                    this.addBasicStat(statType, count);
                    innerMsg.append("\n")
                            .append(statType.getDisplayName())
                            .append(": ")
                            .append(Config.getIncrease(count));
                }
            }

            String msg = research.name + " 연구를 해금하였습니다!";
            this.replyPlayer(msg, innerMsg.toString());

            //TODO: 서비스 시 주석 해제
//          KakaoTalk.replyGroup(this.getName() + "님이 " + msg);
        }
    }

    public boolean canAddQuest(long questId) {
        Config.checkId(Id.QUEST, questId);

        Quest quest = Config.getData(Id.QUEST, questId);
        return quest.limitLv.isInRange(this.lv.get()) && quest.limitCloseRate.isInRange(this.closeRate)
                && this.checkStatRange(quest.limitStat.getMin(), quest.limitStat.getMax());
    }

    public void addQuest(long questId) {
        Quest quest = Config.getData(Id.QUEST, questId);

        this.quest.put(questId, quest.getChatId().get());
        this.addLog(LogData.QUEST_RECEIVE, 1);

        long questNpcId = quest.getNpcId().get();
        ConcurrentHashSet<Long> questNpcSet = this.questNpc.get(questNpcId);

        if(questNpcSet == null) {
            questNpcSet = new ConcurrentHashSet<>();
            questNpcSet.add(questId);
            this.questNpc.put(questNpcId, questNpcSet);
        } else {
            questNpcSet.add(questId);
        }
    }

    public boolean canEquip(long equipId) {
        Config.checkId(Id.EQUIPMENT, equipId);

        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
        return equipment.getTotalLimitLv().isInRange(this.lv.get()) &&
                this.checkStatRange(equipment.getLimitStat().getMin(), equipment.getLimitStat().getMax());
    }

    public boolean canReinforce(long equipId, Map<StatType, Integer> increaseLimitStat) {
        if(this.getEquipInventory().contains(equipId)) {
            Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);

            boolean flag = equipment.getReinforceCount().get() < Config.MAX_REINFORCE_COUNT;

            if(flag && this.getEquipped(equipment.getEquipType()) == equipId) {
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
        this.addLog(LogData.REINFORCE_TRY, 1);

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

        Equipment equipment = Config.loadObject(Id.EQUIPMENT, equipId);
        double reinforcePercent = equipment.getReinforcePercent();

        try {
            if (percent < reinforcePercent) {
                equipment.successReinforce(increaseReinforceStat, increaseMinLimitStat, increaseMaxLimitStat);

                this.addLog(LogData.REINFORCE_SUCCESS, 1);

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
            Config.unloadObject(equipment);
        }
    }

    public int getClearedQuest(long questId) {
        return this.clearedQuest.getOrDefault(questId, 0);
    }

    public void setChatCount(long npcId, long count) {
        Config.checkId(Id.NPC, npcId);

        if(count < 0) {
            throw new NumberRangeException(count, 0);
        }

        if(count != 0) {
            this.chatCount.put(npcId, count);
        } else {
            this.chatCount.remove(npcId);
        }
    }

    public long getChatCount(long npcId) {
        return this.chatCount.getOrDefault(npcId, 0L);
    }

    public void addChatCount(long npcId, long count) {
        this.setChatCount(npcId, this.getChatCount(npcId) + count);
    }

    public long getResponseChat(@NonNull WaitResponse waitResponse) {
        return this.responseChat.getOrDefault(waitResponse, 0L);
    }

    public void setAnyResponseChat(@NonNull String response, long chatId) {
        if(chatId == 0) {
            this.anyResponseChat.remove(response);
        } else {
            Config.checkId(Id.CHAT, chatId);
            this.anyResponseChat.put(response, chatId);
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
        if(count == 0) {
            this.log.remove(logData);
        } else {
            this.log.put(logData, count);
        }

        this.log.put(LogData.LOG_COUNT, this.getLog(LogData.LOG_COUNT) + 1);
        Logger.i("LogData", this.getName() + " {" + logData.toString() + " : " + count + "}");
    }

    public long getLog(@NonNull LogData logData) {
        return this.log.getOrDefault(logData, 0L);
    }

    public void addLog(@NonNull LogData logData, long count) {
        this.setLog(logData, this.getLog(logData) + count);
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
    public void setBuff(long time, @NonNull StatType statType, int stat) {
        super.setBuff(time, statType, stat);
        this.addLog(LogData.BUFF_RECEIVE, 1);
    }

    @Override
    public void onDeath() {
        super.onDeath();

        this.setBasicStat(StatType.HP, 1);
        this.location.set(this.baseLocation);

        GameMap map = null;
        try {
            map = Config.loadMap(this.baseLocation);
            map.addEntity(this);
        } finally {
            if(map != null) {
                Config.unloadMap(map);
            }
        }

        this.getBuff().clear();
        this.getBuffStat().clear();

        Random random = new Random();
        double loseMoneyPercent = random.nextDouble() * Config.TOTAL_MONEY_LOSE_RANDOM + Config.TOTAL_MONEY_LOSE_MIN;
        double dropPercent = random.nextDouble() * Config.MONEY_DROP_RANDOM + Config.MONEY_DROP_MIN;
        int dropItemCount = random.nextInt(Config.ITEM_DROP_COUNT);

        long totalLoseMoney = (long) (this.getMoney() * loseMoneyPercent);
        long dropMoney = (long) (totalLoseMoney * dropPercent);
        long loseMoney = totalLoseMoney - dropMoney;

        this.dropMoney(dropMoney);
        this.addMoney(loseMoney);

        StringBuilder dropItemBuilder = new StringBuilder("G\n\n---떨어트린 아이템 목록---");
        StringBuilder loseItemBuilder = new StringBuilder("\n\n---사라진 아이템 목록---");

        boolean drop = false;
        boolean lost = false;

        if(dropItemCount != 0) {
            List<Long> keys;
            long itemId;
            int count;
            for (int i = 0; i < dropItemCount; i++) {
                keys = new ArrayList<>(this.inventory.keySet());

                if(keys.isEmpty()) {
                    break;
                }

                itemId = keys.get(random.nextInt(keys.size()));
                count = Math.min(random.nextInt(this.getItem(itemId)) + 1, Config.MAX_ITEM_DROP_COUNT);

                Item item = Config.getData(Id.ITEM, itemId);
                String itemName = item.getName();

                if (random.nextDouble() < Config.ITEM_DROP_LOSE_PERCENT) {
                    drop = true;
                    this.dropItem(itemId, count);

                    dropItemBuilder.append("\n")
                            .append(itemName)
                            .append(" ")
                            .append(count)
                            .append("개");
                } else {
                    lost = true;
                    this.addItem(itemId, count * -1);

                    loseItemBuilder.append("\n")
                            .append(itemName)
                            .append(" ")
                            .append(count)
                            .append("개");
                }
            }

            if(!drop) {
                dropItemBuilder.append("\n떨어트린 아이템이 없습니다");
            }

            if(!lost) {
                dropItemBuilder.append("\n잃어버린 아이템이 없습니다");
            }
        }

        String innerMsg = "떨어트린 돈 : " + dropMoney + "G\n잃어버린 돈 : " + loseMoney
                + dropItemBuilder.toString() + loseItemBuilder.toString();
        this.replyPlayer("당신은 사망했습니다...", innerMsg);
        this.addLog(LogData.DEATH, 1);
    }

    @Override
    public void onKill(@NonNull Entity entity) {
        this.addExp(getKillExp(entity.lv.get()));

        String msg = entity.getName() + "을 처치했습니다!\n";
        
        if(!entity.id.getId().equals(Id.PLAYER)) {
            int token = Config.giveToken(ItemList.LOW_HUNTER_TOKEN.getId(), RandomList.HUNTER_TOKEN, this.getLv().get() / 100, this);
            
            if(token >= 0) {
                msg += Config.TIERS[token] + "급 사냥꾼의 증표 1개를 획득하였습니다\n";
            }
        }

        this.replyPlayer(msg + Emoji.LV + " 레벨: " + this.getDisplayLv());
    }

    @Override
    public boolean canFight(@NonNull Entity enemy) {
        if(enemy.id.getId().equals(Id.PLAYER)) {
            return super.canFight(enemy) && this.isPvp() && ((Player) enemy).isPvp();
        } else {
            return super.canFight(enemy);
        }
    }

    //[테스트 칭호] 남식(Lv.123)
    @NonNull
    @Override
    public String getName() {
        return "[" + this.getCurrentTitle() + "] " + this.getNickName() + " (Lv." + this.getLv().get() + ")";
    }

    @NonNull
    @Override
    public String getRealName() {
        return this.getNickName();
    }

}
