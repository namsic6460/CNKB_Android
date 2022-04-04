package lkd.namsic.game.creator;

import lkd.namsic.game.config.Config;
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
        quest.setRewardExp(35000L);
        quest.setRewardMoney(250L);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.TRASH_COLLECTING, NpcList.NOAH.getId(), 13L);
        quest.setNeedItem(ItemList.TRASH.getId(), 1);
        quest.setRewardCloseRate(NpcList.NOAH.getId(), 1);
        quest.setRewardExp(50000L);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.NEED_FIRE, NpcList.NOAH.getId(), 19L);
        quest.setNeedItem(ItemList.RED_SPHERE.getId(), 1);
        quest.setRewardCloseRate(NpcList.NOAH.getId(), 5);
        quest.setRewardExp(350000L);
        quest.setRewardMoney(500L);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.TOO_STRONG_FIRE, NpcList.NOAH.getId(), 23L);
        quest.setNeedItem(ItemList.LOW_MP_POTION.getId(), 3);
        quest.setRewardCloseRate(NpcList.NOAH.getId(), 10);
        quest.getRewardItem().put(ItemList.STAT_POINT.getId(), 50);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.PROVE_EXPERIENCE, NpcList.MOO_MYEONG.getId(), 55L);
        quest.setNeedItem(ItemList.LOW_ADV_TOKEN.getId(), 1);
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
        quest.setRewardExp(250000L);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.POWER_OF_TOKEN, NpcList.SELINA.getId(), 65L);
        quest.setNeedItem(ItemList.LOW_HUNTER_TOKEN.getId(), 5);
        quest.setNeedItem(ItemList.HUNTER_TOKEN.getId(), 5);
        quest.setRewardCloseRate(NpcList.SELINA.getId(), 1);
        quest.setRewardItem(ItemList.EXP_POTION.getId(), 2);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.GOLD_RING_GIFT, NpcList.HYEONG_SEOK.getId(), 69L);
        quest.setNeedItem(ItemList.GOLD.getId(), 5);
        quest.setNeedMoney(5000L);
        quest.setRewardCloseRate(NpcList.HYEONG_SEOK.getId(), 10);
        quest.setRewardCloseRate(NpcList.EL.getId(), 10);
        quest.setRewardItem(ItemList.LOW_AMULET.getId(), 1);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.NEED_COAL, NpcList.PEDRO.getId(), 73L);
        quest.setNeedItem(ItemList.COAL.getId(), 100);
        quest.setRewardCloseRate(NpcList.PEDRO.getId(), 2);
        quest.setRewardItem(ItemList.LOW_REINFORCE_STONE.getId(), 10);
        quest.setRewardMoney(2000L);
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
        quest.setClearLimitLv(50);
        quest.setRewardItem(ItemList.STAT_POINT.getId(), 50);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.LV100, NpcList.NOAH.getId(), 132L);
        quest.setClearLimitLv(100);
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
        quest.setRewardExp(100000L);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.STICKY_SLIME, NpcList.EL.getId(), 92L);
        quest.setNeedItem(ItemList.PIECE_OF_SLIME.getId(), 20);
        quest.setRewardCloseRate(NpcList.EL.getId(), 20);
        quest.setRewardCloseRate(NpcList.BOAM_E.getId(), 10);
        quest.setRewardExp(500000L);
        quest.setRewardMoney(10000L);
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
        quest.setRewardExp(100000L);
        quest.setRewardItem(ItemList.ELIXIR_HERB.getId(), 1);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.HEALING_ELF2, NpcList.SYLVIA.getId(), 130L);
        quest.setNeedItem(ItemList.ELIXIR_HERB.getId(), 1);
        quest.setRewardExp(100000L);
        quest.setRewardCloseRate(NpcList.SYLVIA.getId(), 10);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.LEATHER_COLLECTING1, NpcList.HYEONG_SEOK.getId(), 141L);
        quest.setNeedItem(ItemList.SHEEP_LEATHER.getId(), 10);
        quest.setRewardMoney(500L);
        quest.setRewardExp(100000L);
        quest.setRewardCloseRate(NpcList.HYEONG_SEOK.getId(), 2);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.LEATHER_COLLECTING2, NpcList.HYEONG_SEOK.getId(), 141L);
        quest.setNeedItem(ItemList.LEATHER.getId(), 10);
        quest.setRewardMoney(1000L);
        quest.setRewardExp(180000L);
        quest.setRewardCloseRate(NpcList.HYEONG_SEOK.getId(), 3);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.LEATHER_COLLECTING3, NpcList.HYEONG_SEOK.getId(), 141L);
        quest.setNeedItem(ItemList.OAK_LEATHER.getId(), 5);
        quest.setRewardMoney(3500L);
        quest.setRewardExp(250000L);
        quest.setRewardCloseRate(NpcList.HYEONG_SEOK.getId(), 5);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.INCREASING_ZOMBIE, NpcList.PEDRO.getId(), 145L);
        quest.setNeedItem(ItemList.ZOMBIE_HEAD.getId(), 5);
        quest.setRewardMoney(2000L);
        quest.setRewardExp(150000L);
        quest.setRewardCloseRate(NpcList.PEDRO.getId(), 3);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.BONE_IN_THE_SEA, NpcList.KANG_TAE_GONG.getId(), 149L);
        quest.setNeedItem(ItemList.PIECE_OF_BONE.getId(), 30);
        quest.setRewardMoney(3000L);
        quest.setRewardExp(100000L);
        quest.setRewardCloseRate(NpcList.KANG_TAE_GONG.getId(), 3);
        Config.unloadObject(quest);

        quest = new Quest(QuestList.SOUND_IN_THE_SINKHOLE, NpcList.NOAH.getId(), 153L);
        quest.setNeedItem(ItemList.HORN_OF_IMP.getId(), 1);
        quest.setRewardExp(300000L);
        Config.unloadObject(quest);
        
        quest = new Quest(QuestList.HEALTHY_DEVIL_HORN, NpcList.NOAH.getId(), 183L);
        quest.setNeedItem(ItemList.HORN_OF_LOW_DEVIL.getId(), 10);
        quest.setRewardCloseRate(NpcList.NOAH.getId(), 10);
        Config.unloadObject(quest);
        
        quest = new Quest(QuestList.WORK_TO_KILL_DEVIL1, NpcList.LONGSILVER.getId(), 187L);
        quest.setNeedItem(ItemList.HORN_OF_IMP.getId(), 3);
        quest.setRewardCloseRate(NpcList.ELWOOD.getId(), 5);
        quest.setRewardCloseRate(NpcList.LONGSILVER.getId(), 5);
        quest.setRewardMoney(100000L);
        Config.unloadObject(quest);
        
        quest = new Quest(QuestList.WORK_TO_KILL_DEVIL2, NpcList.LONGSILVER.getId(), 191L);
        quest.setNeedItem(ItemList.IMP_HEART.getId(), 2);
        quest.setRewardCloseRate(NpcList.ELWOOD.getId(), 5);
        quest.setRewardCloseRate(NpcList.LONGSILVER.getId(), 5);
        quest.setRewardMoney(100000L);
        quest.setRewardItem(ItemList.STAT_POINT.getId(), 20);
        Config.unloadObject(quest);
        
        quest = new Quest(QuestList.WORK_TO_KILL_DEVIL3, NpcList.LONGSILVER.getId(), 195L);
        quest.setNeedItem(ItemList.SUCCUBUS_SOUL.getId(), 1);
        quest.setRewardCloseRate(NpcList.ELWOOD.getId(), 10);
        quest.setRewardCloseRate(NpcList.LONGSILVER.getId(), 10);
        quest.setRewardCloseRate(NpcList.FREY.getId(), 5);
        quest.setRewardCloseRate(NpcList.HIBIS.getId(), 5);
        quest.setRewardCloseRate(NpcList.SHADOW_BACK.getId(), 5);
        quest.setRewardMoney(500000L);
        quest.setRewardItem(ItemList.STAT_POINT.getId(), 50);
        quest.setRewardItem(ItemList.ADV_STAT.getId(), 30);
        quest.setRewardItem(ItemList.HIGH_ELIXIR.getId(), 3);
        Config.unloadObject(quest);
        
        quest = new Quest(QuestList.SECRET_REQUEST1, NpcList.JOON_SIK.getId(), 200L);
        quest.setNeedItem(ItemList.MAGIC_STONE.getId(), 30);
        quest.setRewardCloseRate(NpcList.JOON_SIK.getId(), 5);
        quest.setRewardExp(500000L);
        quest.setRewardMoney(200000L);
        Config.unloadObject(quest);
        
        quest = new Quest(QuestList.SECRET_REQUEST2, NpcList.JOON_SIK.getId(), 201L);
        quest.setNeedItem(ItemList.HARPY_WING.getId(), 5);
        quest.setNeedItem(ItemList.HARPY_NAIL.getId(), 5);
        quest.setRewardCloseRate(NpcList.JOON_SIK.getId(), 5);
        quest.setRewardExp(500000L);
        quest.setRewardMoney(200000L);
        Config.unloadObject(quest);
    
        quest = new Quest(QuestList.SECRET_REQUEST3, NpcList.JOON_SIK.getId(), 202L);
        quest.setNeedItem(ItemList.GOLEM_CORE.getId(), 3);
        quest.setRewardCloseRate(NpcList.JOON_SIK.getId(), 5);
        quest.setRewardExp(500000L);
        quest.setRewardMoney(200000L);
        Config.unloadObject(quest);
    
        quest = new Quest(QuestList.SECRET_REQUEST4, NpcList.JOON_SIK.getId(), 203L);
        quest.setNeedItem(ItemList.PIECE_OF_MAGIC.getId(), 15);
        quest.setRewardCloseRate(NpcList.JOON_SIK.getId(), 5);
        quest.setRewardExp(500000L);
        quest.setRewardMoney(200000L);
        Config.unloadObject(quest);
        
        quest = new Quest(QuestList.SECRET_REQUEST5, NpcList.KANG_TAE_GONG.getId(), 204L);
        Config.unloadObject(quest);
        
        Logger.i("ObjectMaker", "Quest making is done!");
    }

}
