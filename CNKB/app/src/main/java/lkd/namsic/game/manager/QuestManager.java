package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.object.Quest;

public class QuestManager {

    private static final QuestManager instance = new QuestManager();

    public static QuestManager getInstance() {
        return instance;
    }

    public boolean canClearQuest(@NonNull Player self,  long questId) {
        Quest quest = Config.getData(Id.QUEST, questId);

        return self.getMoney() >= quest.getNeedMoney() &&
                Config.compareMap(self.getInventory(), quest.getNeedItem(), true, false, 0) &&
                self.compareStat(quest.getNeedStat()) &&
                Config.compareMap(self.getCloseRate(), quest.getNeedCloseRate(), true, false, 0) &&
                self.getLv() >= quest.getClearLimitLv();
    }

    public void clearQuest(@NonNull Player self, long questId, long npcId) {
        Long chatId = self.getQuest().get(questId);
        if(chatId == null) {
            throw new ObjectNotFoundException(Id.QUEST, questId);
        }

        long totalMoney = 0;
        long totalExp;
        int totalAdv;
        Map<Long, Integer> totalItem = new HashMap<>();
        Map<StatType, Integer> totalStat = new HashMap<>();
        Map<Long, Integer> totalCloseRate = new HashMap<>();

        //Remove Need Things
        Quest quest = Config.getData(Id.QUEST, questId);

        self.addMoney(quest.getNeedMoney() * -1);
        totalMoney -= quest.getNeedMoney();

        long longKey;
        int value;
        for(Map.Entry<Long, Integer> entry : quest.getNeedItem().entrySet()) {
            longKey = entry.getKey();
            value = entry.getValue() * -1;

            self.addItem(longKey, value);
            totalItem.put(longKey, value);
        }

        StatType statTypeKey;
        for(Map.Entry<StatType, Integer> entry : quest.getNeedStat().entrySet()) {
            statTypeKey = entry.getKey();
            value = entry.getValue() * -1;

            self.addBasicStat(statTypeKey, value);
            totalStat.put(statTypeKey, value);
        }

        for(Map.Entry<Long, Integer> entry : quest.getNeedCloseRate().entrySet()) {
            longKey = entry.getKey();
            value = entry.getValue() * -1;

            self.addCloseRate(longKey, value);
            totalCloseRate.put(longKey, value);
        }

        //Add Reward Things
        self.addMoney(quest.getRewardMoney());
        totalMoney += quest.getRewardMoney();

        self.addExp(quest.getRewardExp());
        totalExp = quest.getRewardExp();

        self.setAdv(self.getAdv() + quest.getRewardAdv());
        totalAdv = quest.getRewardAdv();

        for(Map.Entry<Long, Integer> entry : quest.getRewardItem().entrySet()) {
            longKey = entry.getKey();
            value = entry.getValue();

            self.addItem(longKey, value, false);
            totalItem.put(longKey, totalItem.getOrDefault(longKey, 0) + value);
        }

        for(Map.Entry<StatType, Integer> entry : quest.getRewardStat().entrySet()) {
            statTypeKey = entry.getKey();
            value = entry.getValue();

            self.addBasicStat(statTypeKey, value);
            totalStat.put(statTypeKey, totalStat.getOrDefault(statTypeKey, 0) + value);
        }

        for(Map.Entry<Long, Integer> entry : quest.getRewardCloseRate().entrySet()) {
            longKey = entry.getKey();
            value = entry.getValue();

            self.addCloseRate(longKey, value);
            totalCloseRate.put(longKey, totalCloseRate.getOrDefault(longKey, 0) + value);
        }

        StringBuilder innerMsg = new StringBuilder("---아이템 현황---");

        if(totalItem.isEmpty()) {
            innerMsg.append("\n변경된 아이템이 없습니다");
        } else {
            for (Map.Entry<Long, Integer> entry : totalItem.entrySet()) {
                innerMsg.append("\n")
                        .append(ItemList.findById(entry.getKey()))
                        .append(": ")
                        .append(Config.getIncrease(entry.getValue()));
            }
        }

        innerMsg.append("\n\n---스텟 현황---");
        if(totalStat.isEmpty()) {
            innerMsg.append("\n변경된 스텟이 없습니다");
        } else {
            for (Map.Entry<StatType, Integer> entry : totalStat.entrySet()) {
                innerMsg.append("\n")
                        .append(entry.getKey().getDisplayName())
                        .append(": ")
                        .append(Config.getIncrease(entry.getValue()));
            }
        }

        innerMsg.append("\n\n---친밀도 현황---");
        if(totalCloseRate.isEmpty()) {
            innerMsg.append("\n변경된 친밀도가 없습니다");
        } else {
            for (Map.Entry<Long, Integer> entry : totalCloseRate.entrySet()) {
                innerMsg.append("\n")
                        .append(NpcList.findById(entry.getKey()))
                        .append(": ")
                        .append(Config.getIncrease(entry.getValue()));
            }
        }

        innerMsg.append("\n\n---획득한 아이템 제작법---");
        if(quest.getRewardItemRecipe().isEmpty()) {
            innerMsg.append("\n획득한 아이템 제작법이 없습니다");
        } else {
            for(long itemId : quest.getRewardItemRecipe()) {
                self.getItemRecipe().add(itemId);
                innerMsg.append("\n")
                        .append(Emoji.LIST)
                        .append(" ")
                        .append(ItemList.findById(itemId));
            }
        }

        innerMsg.append("\n\n---획득한 장비 제작법---");
        if(quest.getRewardEquipRecipe().isEmpty()) {
            innerMsg.append("\n획득한 장비 제작법이 없습니다");
        } else {
            for(long equipId : quest.getRewardEquipRecipe()) {
                self.getEquipRecipe().add(equipId);
                innerMsg.append("\n")
                        .append(Emoji.LIST)
                        .append(" ")
                        .append(EquipList.findById(equipId));
            }
        }

        String msg = "===퀘스트 클리어 결과===\n" +
                Emoji.GOLD + ": " + Config.getIncrease(totalMoney) + "\n" +
                Emoji.EXP + ": " + Config.getIncrease(totalExp) + "\n" +
                Emoji.ADV + ": " + Config.getIncrease(totalAdv);
        self.replyPlayer(msg, innerMsg.toString());

        self.getClearedQuest().put(questId, self.getClearedQuest(questId) + 1);
        self.getQuest().remove(questId);

        Set<Long> questNpcSet = self.getQuestNpc().get(npcId);
        questNpcSet.remove(questId);
        if(questNpcSet.isEmpty()) {
            self.getQuestNpc().remove(npcId);
        }

        self.addLog(LogData.QUEST_CLEAR, 1);
    }

}
