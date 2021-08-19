package lkd.namsic.game.creator;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.enums.object.QuestList;
import lkd.namsic.game.object.Quest;
import lkd.namsic.setting.Logger;

public class QuestCreator implements Creatable {

    @Override
    public void start() {
        Quest quest;

        quest = new Quest(QuestList.WORK_OF_MINER, NpcList.NOAH.getId(), 13L);
        quest.setNeedItem(ItemList.STONE.getId(), 30);
        quest.setRewardCloseRate(NpcList.NOAH.getId(), 1);
        quest.getRewardExp().set(35000L);
        quest.getRewardMoney().set(250L);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.TRASH_COLLECTING, NpcList.NOAH.getId(), 13L);
        quest.setNeedItem(ItemList.TRASH.getId(), 1);
        quest.setRewardCloseRate(NpcList.NOAH.getId(), 1);
        quest.getRewardExp().set(50000L);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.NEED_FIRE, NpcList.NOAH.getId(), 19L);
        quest.setNeedItem(ItemList.RED_SPHERE.getId(), 1);
        quest.setRewardCloseRate(NpcList.NOAH.getId(), 5);
        quest.getRewardExp().set(350000L);
        quest.getRewardMoney().set(500L);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.TOO_STRONG_FIRE, NpcList.NOAH.getId(), 23L);
        quest.setNeedItem(ItemList.LOW_MP_POTION.getId(), 3);
        quest.setRewardCloseRate(NpcList.NOAH.getId(), 10);
        quest.getRewardItem().put(ItemList.STAT_POINT.getId(), 50);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.PROVE_EXPERIENCE, NpcList.MOO_MYEONG.getId(), 55L);
        quest.setNeedItem(ItemList.LOW_ADV_TOKEN.getId(), 3);
        quest.setNeedItem(ItemList.LOW_FISH_TOKEN.getId(), 3);
        quest.setNeedItem(ItemList.LOW_MINER_TOKEN.getId(), 30);
        quest.setRewardCloseRate(NpcList.MOO_MYEONG.getId(), 10);
        quest.setRewardCloseRate(NpcList.SELINA.getId(), 10);
        quest.setRewardItem(ItemList.LOW_EXP_POTION.getId(), 30);
        quest.setRewardItem(ItemList.ADV_STAT.getId(), 20);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.NEED_FISHING_ROD_ITEM, NpcList.KANG_TAE_GONG.getId(), 61L);
        quest.setNeedItem(ItemList.LEATHER.getId(), 20);
        quest.setNeedItem(ItemList.BRANCH.getId(), 10);
        quest.setRewardCloseRate(NpcList.KANG_TAE_GONG.getId(), 10);
        quest.setRewardItem(ItemList.SMALL_GOLD_BAG.getId(), 2);
        quest.getRewardExp().set(250000L);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.POWER_OF_TOKEN, NpcList.SELINA.getId(), 65L);
        quest.setNeedItem(ItemList.LOW_HUNTER_TOKEN.getId(), 5);
        quest.setNeedItem(ItemList.HUNTER_TOKEN.getId(), 5);
        quest.setRewardCloseRate(NpcList.SELINA.getId(), 1);
        quest.setRewardItem(ItemList.EXP_POTION.getId(), 2);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.GOLD_RING_GIFT, NpcList.HYEONG_SEOK.getId(), 69L);
        quest.setNeedItem(ItemList.GOLD.getId(), 5);
        quest.getNeedMoney().set(5000L);
        quest.setRewardCloseRate(NpcList.HYEONG_SEOK.getId(), 10);
        quest.setRewardCloseRate(NpcList.EL.getId(), 10);
        quest.setRewardItem(ItemList.LOW_AMULET.getId(), 1);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.NEED_COAL, NpcList.PEDRO.getId(), 73L);
        quest.setNeedItem(ItemList.COAL.getId(), 100);
        quest.setRewardCloseRate(NpcList.PEDRO.getId(), 2);
        quest.setRewardItem(ItemList.LOW_REINFORCE_STONE.getId(), 10);
        quest.getRewardMoney().set(2000L);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.ANOTHER_PRESENT, NpcList.EL.getId(), 77L);
        quest.setNeedItem(ItemList.GLOW_STONE.getId(), 10);
        quest.setNeedItem(ItemList.COMMON_FISH1.getId(), 3);
        quest.setNeedItem(ItemList.COMMON_FISH2.getId(), 3);
        quest.setNeedItem(ItemList.COMMON_FISH3.getId(), 3);
        quest.setNeedItem(ItemList.COMMON_FISH4.getId(), 3);
        quest.setNeedItem(ItemList.COMMON_FISH5.getId(), 3);
        quest.setRewardCloseRate(NpcList.HYEONG_SEOK.getId(), 10);
        quest.setRewardCloseRate(NpcList.EL.getId(), 10);
        quest.setRewardStat(StatType.MAXHP, 30);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.LV50, NpcList.NOAH.getId(), 79L);
        quest.getClearLimitLv().set(50);
        quest.setRewardItem(ItemList.STAT_POINT.getId(), 50);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.LV100, NpcList.NOAH.getId(), 132L);
        quest.getClearLimitLv().set(100);
        quest.setRewardItem(ItemList.STAT_POINT.getId(), 150);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.MEMORIAL_CEREMONY, NpcList.JOON_SIK.getId(), 84L);
        quest.setNeedItem(ItemList.PIG_HEAD.getId(), 3);
        quest.getRewardItemRecipe().add(ItemList.COMMON_GRILLED_FISH.getId());
        quest.getRewardItemRecipe().add(ItemList.RARE_GRILLED_FISH.getId());
        quest.getRewardItemRecipe().add(ItemList.SPECIAL_GRILLED_FISH.getId());
        quest.getRewardItemRecipe().add(ItemList.UNIQUE_GRILLED_FISH.getId());
        quest.getRewardItemRecipe().add(ItemList.LEGENDARY_GRILLED_FISH.getId());
        quest.getRewardItemRecipe().add(ItemList.MYSTIC_GRILLED_FISH.getId());
        quest.getRewardItemRecipe().add(ItemList.COOKED_LAMB.getId());
        quest.getRewardItemRecipe().add(ItemList.COOKED_PORT.getId());
        quest.getRewardItemRecipe().add(ItemList.COOKED_BEEF.getId());
        quest.setRewardCloseRate(NpcList.JOON_SIK.getId(), 10);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.MAGIC_OF_SPIDER_EYE, NpcList.SELINA.getId(), 88L);
        quest.setNeedItem(ItemList.SPIDER_EYE.getId(), 5);
        quest.setRewardCloseRate(NpcList.SELINA.getId(), 3);
        quest.getRewardExp().set(100000L);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.STICKY_SLIME, NpcList.EL.getId(), 92L);
        quest.setNeedItem(ItemList.PIECE_OF_SLIME.getId(), 20);
        quest.setRewardCloseRate(NpcList.EL.getId(), 20);
        quest.setRewardCloseRate(NpcList.BOAM_E.getId(), 10);
        quest.getRewardExp().set(500000L);
        quest.getRewardMoney().set(10000L);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.HERB_COLLECTING, NpcList.EL.getId(), 96L);
        quest.setNeedItem(ItemList.HERB.getId(), 10);
        quest.setRewardItem(ItemList.ELIXIR_HERB.getId(), 1);
        Config.unloadObject(quest);

        for (int i = 0; i < Config.GEMS.length; i++) {
            ItemList gem = Config.GEMS[i];

            quest = new Quest(QuestList.nameMap.get("보석 수집 - " + gem.getDisplayName()),
                    NpcList.BOAM_E.getId(), 133L);
            quest.setNeedItem(gem.getId(), 1);
            quest.setRewardCloseRate(NpcList.BOAM_E.getId(), 5);
            quest.setRewardItem(ItemList.STAT_POINT.getId(), 5);
            Config.unloadObject(quest);
        }

        quest = new Quest(QuestList.HEALING_ELF1, NpcList.EL.getId(), 129L);
        quest.getRewardExp().set(100000L);
        quest.setRewardItem(ItemList.ELIXIR_HERB.getId(), 1);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.HEALING_ELF2, NpcList.SYLVIA.getId(), 130L);
        quest.setNeedItem(ItemList.ELIXIR_HERB.getId(), 1);
        quest.getRewardExp().set(100000L);
        quest.setRewardCloseRate(NpcList.SYLVIA.getId(), 10);
        Config.unloadObject(quest);

        Config.ID_COUNT.put(Id.QUEST, Math.max(Config.ID_COUNT.get(Id.QUEST), 35L));
        Logger.i("ObjectMaker", "Quest making is done!");
    }

}
