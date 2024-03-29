package lkd.namsic.game.object;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.LoNg;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MagicType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.event.AddExpEvent;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Player extends Entity {
    
    public static final String DEATH_MSG = "당신은 사망했습니다...";
    
    public static void replyPlayers(@NonNull Set<Player> players, @Nullable String msg) {
        replyPlayers(players, msg, null);
    }
    
    public static void replyPlayers(@NonNull Set<Player> players, @Nullable String msg, @Nullable String innerMsg) {
        replyPlayersExcept(players, msg, innerMsg, null);
    }
    
    public static void replyPlayersExcept(@NonNull Set<Player> players, @Nullable String msg, @Nullable Player except) {
        replyPlayersExcept(players, msg, null, except);
    }
    
    public static void replyPlayersExcept(@NonNull Set<Player> players, @Nullable String msg,
                                          @Nullable String innerMsg, @Nullable Player except) {
        Set<Player> excepts = new HashSet<>();
        excepts.add(except);
        replyPlayersExcepts(players, msg, innerMsg, excepts);
    }
    
    public static void replyPlayersExcepts(@NonNull Set<Player> players, @Nullable String msg, @NonNull Set<Player> excepts) {
        replyPlayersExcepts(players, msg, null, excepts);
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
            if(session == null) {
                continue;
            }
            
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
    
    private int newDayCount = 0;
    
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
    
    @Setter
    boolean isGroup = true;
    
    final Location baseLocation = new Location();
    
    @Setter
    int sp = 0;
    
    @Setter
    int adv = 0;
    
    @Setter
    int todayAdv = 0;
    
    @Setter
    long exp = 0;
    
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
    
    @Setter
    double reinforceMultiplier = 1;
    
    @NonNull
    @Setter
    String deathMsg = "";
    
    @Setter
    boolean printDeathMsg = true;
    
    @Setter
    int towerFloor = 1;
    
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
        return Config.getExpStr(this.lv, this.exp, this.getRequiredExp());
    }
    
    @NonNull
    public String getDisplayMineLv() {
        int mineLv = this.getVariable(Variable.MINE);
        return Config.getExpStr(mineLv, this.getLog(LogData.MINED), RandomList.MINE_LV_REQUIREMENT.get(mineLv));
    }
    
    @NonNull
    public String getDisplayFishLv() {
        int fishLv = this.getVariable(Variable.FISH);
        return Config.getExpStr(fishLv, this.getVariable(Variable.FISH_SKILL), RandomList.FISH_LV_REQUIREMENT.get(fishLv));
    }
    
    public void addTitle(@NonNull String title) {
        this.title.add(title);
    }
    
    public void setCurrentTitle(@NonNull String title) {
        if(this.title.contains(title)) {
            if(title.equals("관리자")) {
                Config.LV_RANK.remove(this.getName());
            }
            
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
    
    public void checkNewDay() {
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfYear();
        int year = now.getYear();
        
        if(day != this.lastDay || year != this.lastYear) {
            this.lastDay = day;
            this.lastYear = year;
            this.todayAdv = 0;
            this.newDay();
        }
    }
    
    public void newDay() {
        int count = ++this.newDayCount;
        
        List<Object[]> rewardList = new ArrayList<>();
        rewardList.add(new Object[]{ItemList.SMALL_GOLD_BAG, 1});
        rewardList.add(new Object[]{ItemList.LOW_HP_POTION, 5});
        rewardList.add(new Object[]{ItemList.LOW_MP_POTION, 5});
        rewardList.add(new Object[]{ItemList.LOW_ELIXIR, 5});
        
        if(count % 5 == 0) {
            rewardList.add(new Object[]{ItemList.STAT_POINT, 10});
            rewardList.add(new Object[]{ItemList.LOW_RECIPE, 3});
            rewardList.add(new Object[]{ItemList.EMPTY_SPHERE, 5});
            rewardList.add(new Object[]{ItemList.LOW_EXP_POTION, 3});
            rewardList.add(new Object[]{ItemList.LOW_REINFORCE_STONE, 3});
        }
        
        if(count % 10 == 0) {
            rewardList.add(new Object[]{ItemList.PIECE_OF_GEM, 5});
            rewardList.add(new Object[]{ItemList.EQUIP_SAFENER, 10});
            rewardList.add(new Object[]{ItemList.HP_POTION, 10});
            rewardList.add(new Object[]{ItemList.MP_POTION, 10});
            rewardList.add(new Object[]{ItemList.ELIXIR, 5});
            rewardList.add(new Object[]{ItemList.STAT_POINT, 20});
            rewardList.add(new Object[]{ItemList.MAGIC_STONE, 10});
        }
        
        if(count % 30 == 0) {
            rewardList.add(new Object[]{ItemList.GOLD_BAG, 3});
            rewardList.add(new Object[]{ItemList.STAT_POINT, 50});
            rewardList.add(new Object[]{ItemList.HIGH_HP_POTION, 15});
            rewardList.add(new Object[]{ItemList.HIGH_MP_POTION, 15});
            rewardList.add(new Object[]{ItemList.HIGH_ELIXIR, 15});
        }
        
        if(count % 100 == 0) {
            rewardList.add(new Object[]{ItemList.GOLD_BAG, 10});
            rewardList.add(new Object[]{ItemList.STAT_POINT, 100});
            rewardList.add(new Object[]{ItemList.HIGH_HP_POTION, 30});
            rewardList.add(new Object[]{ItemList.HIGH_MP_POTION, 30});
            rewardList.add(new Object[]{ItemList.HIGH_ELIXIR, 30});
        }
        
        StringBuilder innerBuilder = new StringBuilder("---출석 보상---");
        
        long itemId;
        int itemCount;
        for(Object[] entry : rewardList) {
            itemId = ((ItemList) entry[0]).getId();
            itemCount = (int) entry[1];
            
            innerBuilder.append("\n")
                .append(ItemList.findById(itemId))
                .append(" ")
                .append(itemCount)
                .append("개");
            
            this.addItem(itemId, itemCount, false);
        }
        
        this.replyPlayer("출석 " + count + "일차", innerBuilder.toString());
    }
    
    @Nullable
    public synchronized Notification.Action getSession() {
        Notification.Action session;
        
        if(this.isGroup) {
            session = KakaoTalk.getGroupSession(this.recentRoom);
        } else {
            session = KakaoTalk.getSoloSession(this.recentRoom);
        }
        
        return session;
    }
    
    public void addExp(long exp) {
        if(exp < 0) {
            throw new NumberRangeException(exp, 0);
        }
        
        exp *= Config.EXP_BOOST;
        
        LoNg wrappedExp = new LoNg(exp);
        String eventName = AddExpEvent.getName();
        AddExpEvent.handleEvent(this, this.event.get(eventName), this.getEquipEvents(eventName), wrappedExp);
        
        exp = wrappedExp.get();
        
        this.exp += exp;
        this.addLog(LogData.TOTAL_EXP, exp);
        
        int startLv = this.lv;
        int endLv;
        
        if(startLv == Config.MAX_LV) {
            return;
        }
        
        long needExp, currentExp;
        while(true) {
            needExp = this.getRequiredExp();
            currentExp = this.exp;
            
            if(needExp < currentExp) {
                this.lvUp(needExp);
            } else {
                endLv = this.lv;
                
                if(startLv != endLv) {
                    this.setBasicStat(StatType.HP, this.getStat(StatType.MAXHP));
                    this.setBasicStat(StatType.MN, this.getStat(StatType.MAXMN));
                    this.replyPlayer("레벨 업!(" + startLv + "->" + endLv + ")\n" +
                            Emoji.EXP + " 경험치: " + this.exp + "\n" +
                            Emoji.SP + " 스텟 포인트: " + this.sp,
                        "레벨업을 하여 체력과 마나가 모두 회복됩니다");
                }
                
                break;
            }
        }
    }
    
    public long getRequiredExp() {
        return this.getRequiredExp(this.lv);
    }
    
    public long getRequiredExp(int lv) {
        long needExp;
        if(lv <= 100) {
            needExp = 10_000 + 5_000L * lv;
        } else if(lv <= 300) {
            needExp = -200_000 + (long) (Math.pow(lv - 10, 2.5) * 2) + 10_000 * lv;
        } else if(lv <= 500) {
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
        int lv = this.lv;
        long exp = 5000 + (long) ((this.getRequiredExp(enemyLv) * 0.00001) + (this.getRequiredExp() * 0.00001));
        
        int gap = lv - enemyLv;
        if(Math.abs(gap) > 10 && lv > 10) {
            double sqrt = Math.sqrt(Math.sqrt(lv - 9) / 1.2);
            
            if(gap > 0) {
                exp /= sqrt;
            } else {
                exp *= sqrt;
            }
        }
        
        return (int) ((1 + 0.125 * enemyLv) * exp);
    }
    
    public void lvUp(long needExp) {
        Config.LV_RANK.remove(this.getName());
        
        if(this.lv < 100) {
            this.addMoney(10000);
        }
        
        this.lv += 1;
        this.sp += 5;
        this.exp += -1 * needExp;
        
        if(!this.currentTitle.equals("관리자") && this.lv >= Config.MIN_RANK_LV) {
            Config.LV_RANK.put(this.getName(), this.lv);
        }
    }
    
    @NonNull
    public Player addAchieve(long achieveId) {
        if(this.achieve.add(achieveId)) {
            Achieve achieve = Config.getData(Id.ACHIEVE, achieveId);
            
            this.addMoney(achieve.getRewardMoney());
            this.addExp(achieve.rewardExp);
            this.adv += achieve.rewardAdv;
            
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
            KakaoTalk.replyAll(this.getName() + "님이 " + msg);
        }
        
        return this;
    }
    
    public boolean canAddResearch(long researchId) {
        Research research = Config.getData(Id.RESEARCH, researchId);
        return research.getLimitLv() <= this.lv && research.getNeedMoney() <= this.getMoney()
            && Config.compareMap(this.inventory, research.needItem, true, false, 0);
    }
    
    public void addResearch(long researchId) {
        if(this.research.add(researchId)) {
            Research research = Config.getData(Id.RESEARCH, researchId);
            
            this.addMoney(-1 * research.getNeedMoney());
            
            this.addExp(research.rewardExp);
            this.adv += research.rewardAdv;
            
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
            KakaoTalk.replyAll(this.getName() + "님이 " + msg);
        }
    }
    
    public void addQuest(long questId) {
        Quest quest = Config.getData(Id.QUEST, questId);
        
        this.quest.put(questId, quest.getClearChatId());
        this.addLog(LogData.QUEST_RECEIVE, 1);
        
        long questNpcId = quest.getClearNpcId();
        ConcurrentHashSet<Long> questNpcSet = this.questNpc.get(questNpcId);
        
        if(questNpcSet == null) {
            questNpcSet = new ConcurrentHashSet<>();
            questNpcSet.add(questId);
            this.questNpc.put(questNpcId, questNpcSet);
        } else {
            questNpcSet.add(questId);
        }
    }
    
    public int getClearedQuest(long questId) {
        return this.clearedQuest.getOrDefault(questId, 0);
    }
    
    public void setChatCount(long npcId, long count) {
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
            if(lv < 1 || lv > Config.MAX_MAGIC_LV) {
                throw new NumberRangeException(lv, 1, Config.MAX_MAGIC_LV);
            }
            
            this.magic.put(magicType, lv);
        }
    }
    
    public int getMagic(@NonNull MagicType magicType) {
        return this.magic.getOrDefault(magicType, 1);
    }
    
    public void addMagic(@NonNull MagicType magicType, int lv) {
        this.setMagic(magicType, this.getMagic(magicType) + lv);
    }
    
    public void setResist(@NonNull MagicType magicType, int lv) {
        if(lv == 0) {
            this.resist.remove(magicType);
        } else {
            if(lv < 1 || lv > Config.MAX_MAGIC_LV) {
                throw new NumberRangeException(lv, 1, Config.MAX_MAGIC_LV);
            }
            
            this.resist.put(magicType, lv);
        }
    }
    
    public int getResist(@NonNull MagicType magicType) {
        return this.resist.getOrDefault(magicType, 1);
    }
    
    public void addResist(@NonNull MagicType magicType, int lv) {
        this.setResist(magicType, this.getResist(magicType) + lv);
    }
    
    public void setCloseRate(long npcId, int closeRate) {
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
    }
    
    public long getLog(@NonNull LogData logData) {
        return this.log.getOrDefault(logData, 0L);
    }
    
    public void addLog(@NonNull LogData logData, long count) {
        this.setLog(logData, this.getLog(logData) + count);
    }
    
    @NonNull
    public String getDeathMsg() {
        String deathMsg = this.deathMsg;
        
        this.printDeathMsg = true;
        this.deathMsg = "";
        
        return deathMsg;
    }
    
    public void printDamageMsg(@NonNull Entity target) {
        this.printDamageMsg(target, this.lastDamage, this.lastDrain, this.isLastCrit);
    }
    
    public void printDamageMsg(@NonNull Entity target, int totalDmg, int totalDra, boolean isCrit) {
        String msg = "공격에 성공했습니다\n적 체력: " + target.getDisplayHp();
        if(isCrit) {
            msg = "[치명타!] " + msg;
        }
        
        this.replyPlayer(msg, "총 데미지: " + totalDmg + "\n총 흡수량: " + totalDra + "\n남은 체력: " + this.getDisplayHp());
    }
    
    public void printDamageMsg(@NonNull List<Entity> targets, int totalDmg, int totalDra, int critCount) {
        if(targets.size() == 1) {
            this.printDamageMsg(targets.get(0), totalDmg, totalDra, critCount != 0);
            return;
        }
        
        String msg = "적 " + targets.size() + " 개체를 향한 공격이 ";
        
        if(totalDmg > 0) {
            StringBuilder innerBuilder = new StringBuilder();
            
            if(critCount != 0) {
                innerBuilder.append("[치명타 * ")
                    .append(critCount)
                    .append("] ");
            }
            
            innerBuilder.append("총 데미지: ")
                .append(totalDmg)
                .append("\n총 흡수량: ")
                .append(totalDra)
                .append("\n\n---적 체력---");
            
            for(Entity target : targets) {
                innerBuilder.append("\n")
                    .append(target.getFightName())
                    .append(": ")
                    .append(target.getDisplayHp());
            }
            
            this.replyPlayer(msg + "성공했습니다", innerBuilder.toString());
        } else {
            this.replyPlayer(msg + "효과가 없었습니다...");
        }
    }
    
    public void printDamagedMsg(@NonNull Entity attacker) {
        this.printDamagedMsg(attacker, attacker.lastDamage, attacker.lastDrain, attacker.isLastCrit, this.lastHeal);
    }
    
    public void printDamagedMsg(@NonNull Entity attacker, int totalDmg, int totalDra, boolean isCrit, int heal) {
        String msg = attacker.getName() + " 에게 공격당했습니다!\n남은 체력: " + this.getDisplayHp();
        
        if(isCrit) {
            msg = "[치명타!] " + msg;
        }
        
        if(this.getObjectVariable(Variable.IS_DEFENCING, false)) {
            msg += "\n방어로 인해 회복한 체력: " + heal;
        }
        
        this.replyPlayer(msg, "총 데미지: " + totalDmg + "\n총 흡수량: " + totalDra + "\n적 체력: " + attacker.getDisplayHp());
    }
    
    @Override
    public void setMoney(long money) {
        long gap = money - this.getMoney();
        super.setMoney(money);
        
        this.addLog(LogData.TOTAL_MONEY, gap);
        
        if(gap > 0) {
            if(this.getLog(LogData.MAX_MONEY) < money) {
                this.setLog(LogData.MAX_MONEY, money);
            }
        } else if(gap < 0) {
            gap *= -1;
            
            if(this.getLog(LogData.MAX_PAYMENT) < gap) {
                this.setLog(LogData.MAX_PAYMENT, gap);
            }
        }
    }
    
    @Override
    public void addSkill(long skillId) {
        super.addSkill(skillId);
        
        SkillList skillData = Objects.requireNonNull(SkillList.idMap.get(skillId));
        replyPlayer(skillData.getDisplayName() + " 스킬을 배웠습니다!");
    }
    
    @Override
    public void setBuff(long time, @NonNull StatType statType, int stat) {
        super.setBuff(time, statType, stat);
        this.addLog(LogData.BUFF_RECEIVE, 1);
    }
    
    @Override
    public void onDeath() {
        super.onDeath();
        
        this.getBuff().clear();
        this.getBuffStat().clear();
        
        Random random = new Random();
        double loseMoneyPercent = random.nextDouble() * Config.TOTAL_MONEY_LOSE_RANDOM + Config.TOTAL_MONEY_LOSE_MIN;
        double dropPercent = random.nextDouble() * Config.MONEY_DROP_RANDOM + Config.MONEY_DROP_MIN;
        int dropItemCount = random.nextInt(Config.ITEM_DROP_COUNT);
        
        long totalLoseMoney = Math.min(10000, (long) (this.getMoney() * loseMoneyPercent));
        long dropMoney = (long) (totalLoseMoney * dropPercent);
        long loseMoney = totalLoseMoney - dropMoney;
        
        if(dropMoney > 0L) {
            this.dropMoney(dropMoney);
        }
        
        if(loseMoney > 0L) {
            this.addMoney(loseMoney);
        }
        
        StringBuilder dropItemBuilder = new StringBuilder("G\n\n---떨어트린 아이템 목록---");
        StringBuilder loseItemBuilder = new StringBuilder("\n\n---잃어버린 아이템 목록---");
        
        boolean drop = false;
        boolean lost = false;
        
        if(dropItemCount != 0) {
            List<Long> keys;
            long itemId;
            int count;
            for(int i = 0; i < dropItemCount; i++) {
                keys = new ArrayList<>(this.inventory.keySet());
                
                if(keys.isEmpty()) {
                    break;
                }
                
                itemId = keys.get(random.nextInt(keys.size()));
                count = Math.min(random.nextInt(this.getItem(itemId)) + 1, Config.MAX_ITEM_DROP_COUNT);
                
                Item item = Config.getData(Id.ITEM, itemId);
                String itemName = item.getName();
                
                if(random.nextDouble() < Config.ITEM_DROP_LOSE_PERCENT) {
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
                loseItemBuilder.append("\n잃어버린 아이템이 없습니다");
            }
        }
        
        this.setBasicStat(StatType.HP, 1);
        this.location.set(this.baseLocation);
        
        GameMap map = Config.loadMap(this.location);
        map.removeEntity(this);
        Config.unloadMap(map);
        
        map = Config.loadMap(this.baseLocation);
        map.addEntity(this);
        Config.unloadMap(map);
        
        String innerMsg = "떨어트린 돈 : " + dropMoney + "G\n잃어버린 돈 : " + loseMoney
            + dropItemBuilder.toString() + loseItemBuilder.toString();
        this.addLog(LogData.DEATH, 1);
        
        if(this.printDeathMsg) {
            this.replyPlayer("당신은 사망했습니다...", innerMsg);
        } else {
            this.deathMsg = innerMsg;
        }
    }
    
    @Override
    public void onKill(@NonNull Entity entity) {
        long killExp = this.getKillExp(entity.lv);
        if(entity.getId().getId().equals(Id.BOSS)) {
            killExp *= 5;
        }
        
        this.addExp(killExp);
        
        String msg = entity.getName() + "을 ";
        
        if(this.isLastCrit) {
            msg += "크리티컬로 ";
        }
        
        msg += "처치했습니다!\n" +
            Emoji.EXP + " " + Config.getIncrease(killExp) + "\n" +
            Emoji.LV + " 레벨: " + this.getDisplayLv();
        
        if(!entity.id.getId().equals(Id.PLAYER)) {
            int token = Config.randomToken(ItemList.LOW_HUNTER_TOKEN.getId(), RandomList.HUNTER_TOKEN, entity.getLv() / 50, this);
            if(token >= 0) {
                msg += "\n\n" + Config.TIERS[token] + "급 사냥꾼의 증표 1개를 획득하였습니다";
            }
        }
        
        StringBuilder innerBuilder = new StringBuilder("드롭 필드 좌표: ")
            .append(entity.getLastDeathLoc().toFieldString())
            .append("\n떨어진 골드: ")
            .append(entity.getLastDropMoney())
            .append("\n\n---떨어진 아이템---");
        
        if(entity.getLastDropItem().isEmpty()) {
            innerBuilder.append("\n떨어트린 아이템이 없습니다");
        } else {
            for(Map.Entry<Long, Integer> entry : entity.getLastDropItem().entrySet()) {
                innerBuilder.append("\n")
                    .append(ItemList.findById(entry.getKey()))
                    .append(" ")
                    .append(entry.getValue())
                    .append("개");
            }
        }
        
        innerBuilder.append("\n\n---떨어진 장비---");
        
        if(entity.getLastDropEquip().isEmpty()) {
            innerBuilder.append("\n떨어트린 장비가 없습니다");
        } else {
            for(long equipId : entity.getLastDropEquip()) {
                innerBuilder.append("\n")
                    .append(((Equipment) Config.getData(Id.EQUIPMENT, equipId)).getName());
            }
        }
        
        this.replyPlayer(msg, innerBuilder.toString());
    }
    
    @Override
    public boolean canFight(@NonNull Entity enemy) {
        if(enemy.id.getId().equals(Id.PLAYER) && enemy.getLv() < 10) {
            return false;
        }
        
        return super.canFight(enemy);
    }
    
    //[테스트 칭호] 남식(Lv.123)
    @NonNull
    @Override
    public String getName() {
        return "[" + this.getCurrentTitle() + "] " + this.getNickName() + " (Lv." + this.getLv() + ")";
    }
    
    @NonNull
    @Override
    public String getFightName() {
        return this.getNickName();
    }
    
    @NonNull
    @Override
    public String getRealName() {
        return this.nickName + " (Lv." + this.getLv() + ")";
    }
    
}
