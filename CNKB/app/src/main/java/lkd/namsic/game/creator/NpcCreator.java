package lkd.namsic.game.creator;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.enums.object.QuestList;
import lkd.namsic.game.object.Npc;
import lkd.namsic.setting.Logger;

public class NpcCreator implements Creatable {

    @Override
    public void start() {
        Npc npc;

        npc = new Npc(NpcList.SECRET, 0L);
        npc.getLocation().set(0, 0, 1, 1);
        Config.unloadObject(npc);

        npc = new Npc(NpcList.ABEL, 0L);
        npc.getLocation().set(0, 0, 1, 1);
        Config.unloadObject(npc);


        npc = new Npc(NpcList.NOAH, 5L);
        npc.getLocation().set(0, 0, 16, 16);

        npc.setBaseChat(new ChatLimit(), 6L);

        ChatLimit chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(7, 11);
        npc.setBaseChat(chatLimit, 14L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(12, 14);
        npc.setBaseChat(chatLimit, 15L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(18, 22);
        npc.setBaseChat(chatLimit, 16L);

        npc.setChat(new ChatLimit(), 7L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.WORK_OF_MINER.getId());
        npc.setChat(chatLimit, 8L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.TRASH_COLLECTING.getId());
        npc.setChat(chatLimit, 9L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.NEED_FIRE.getId());
        chatLimit.getClearedQuest().put(QuestList.WORK_OF_MINER.getId(), 5);
        chatLimit.getClearedQuest().put(QuestList.TRASH_COLLECTING.getId(), 5);
        chatLimit.getNotClearedQuest().add(QuestList.NEED_FIRE.getId());
        npc.setChat(chatLimit, 17L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.TOO_STRONG_FIRE.getId());
        chatLimit.getClearedQuest().put(QuestList.NEED_FIRE.getId(), 1);
        chatLimit.getNotClearedQuest().add(QuestList.TOO_STRONG_FIRE.getId());
        npc.setChat(chatLimit, 20L);

        chatLimit = new ChatLimit();
        chatLimit.getNotClearedQuest().add(QuestList.LV50.getId());
        chatLimit.getNotRunningQuest().add(QuestList.LV50.getId());
        chatLimit.getLimitQuest().addMin(QuestList.WORK_OF_MINER.getId(), 5);
        chatLimit.getLimitQuest().addMin(QuestList.TRASH_COLLECTING.getId(), 5);
        npc.setChat(chatLimit, 78L);

        Config.unloadObject(npc);


        npc = new Npc(NpcList.HYEONG_SEOK, 25L);
        npc.getLocation().set(0, 0, 40, 40);

        npc.setBaseChat(new ChatLimit(), 26L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(22, 23);
        chatLimit.getLimitHour2().set(0, 6);
        npc.setBaseChat(chatLimit, 27L);

        npc.setChat(new ChatLimit(), 28L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.GOLD_RING_GIFT.getId());
        chatLimit.getNotClearedQuest().add(QuestList.GOLD_RING_GIFT.getId());
        chatLimit.getLimitQuest().addMin(QuestList.WORK_OF_MINER.getId(), 10);
        npc.setChat(chatLimit, 66L);

        Config.unloadObject(npc);


        npc = new Npc(NpcList.BOAM_E, 29L);
        npc.getLocation().set(0, 0, 41, 40);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(6, 23);
        npc.setBaseChat(chatLimit, 30L);

        npc.setChat(new ChatLimit(), 31L);

        for (int i = 0; i < Config.GEMS.length; i++) {
            chatLimit = new ChatLimit();
            chatLimit.getLimitQuest().addMin(QuestList.STICKY_SLIME.getId(), 1);
            chatLimit.getNotRunningQuest().add(QuestList.GEM_COLLECTING_QUARTZ.getId() + i);
            chatLimit.getNotClearedQuest().add(QuestList.GEM_COLLECTING_QUARTZ.getId() + i);
            npc.setChat(chatLimit, 98 + (i * 2));
        }

        Config.unloadObject(npc);


        npc = new Npc(NpcList.EL, 32L);
        npc.getLocation().set(0, 0, 41, 40);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(6, 23);
        npc.setBaseChat(chatLimit, 33L);

        npc.setChat(new ChatLimit(), 34L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.ANOTHER_PRESENT.getId());
        chatLimit.getNotClearedQuest().add(QuestList.ANOTHER_PRESENT.getId());
        chatLimit.getClearedQuest().put(QuestList.GOLD_RING_GIFT.getId(), 1);
        npc.setChat(chatLimit, 74L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.STICKY_SLIME.getId());
        chatLimit.getNotClearedQuest().add(QuestList.STICKY_SLIME.getId());
        chatLimit.getClearedQuest().put(QuestList.ANOTHER_PRESENT.getId(), 1);
        npc.setChat(chatLimit, 89L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.HERB_COLLECTING.getId());
        chatLimit.getClearedQuest().put(QuestList.ANOTHER_PRESENT.getId(), 1);
        npc.setChat(chatLimit, 93L);

        Config.unloadObject(npc);


        npc = new Npc(NpcList.JOON_SIK, 35L);
        npc.getLocation().set(1, 1, 32, 32);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(8, 22);
        npc.setBaseChat(chatLimit, 36L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(23, 23);
        chatLimit.getLimitHour2().set(0, 7);
        npc.setBaseChat(chatLimit, 38L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(8, 22);
        npc.setChat(chatLimit, 37L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(23, 23);
        chatLimit.getLimitHour2().set(0, 7);
        npc.setChat(chatLimit, 131L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitQuest().addMin(QuestList.NEED_FISHING_ROD_ITEM.getId(), 1);
        chatLimit.getNotClearedQuest().add(QuestList.MEMORIAL_CEREMONY.getId());
        chatLimit.getNotRunningQuest().add(QuestList.MEMORIAL_CEREMONY.getId());
        npc.setChat(chatLimit, 81L);

        Config.unloadObject(npc);


        npc = new Npc(NpcList.KANG_TAE_GONG, 39L);
        npc.getLocation().set(0, 1, 32, 32);

        npc.setBaseChat(new ChatLimit(), 40L);

        npc.setChat(new ChatLimit(), 41L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.NEED_FISHING_ROD_ITEM.getId());
        chatLimit.getNotClearedQuest().add(QuestList.NEED_FISHING_ROD_ITEM.getId());
        chatLimit.getLimitQuest().addMin(QuestList.TRASH_COLLECTING.getId(), 5);
        npc.setChat(chatLimit, 58L);

        Config.unloadObject(npc);


        npc = new Npc(NpcList.PEDRO, 42L);
        npc.getLocation().set(0, 0, 64, 64);

        npc.setBaseChat(new ChatLimit(), 43L);

        npc.setChat(new ChatLimit(), 44L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.NEED_COAL.getId());
        chatLimit.getLimitQuest().addMax(QuestList.NEED_COAL.getId(), 3);
        npc.setChat(chatLimit, 70L);

        Config.unloadObject(npc);


        npc = new Npc(NpcList.MOO_MYEONG, 45L);
        npc.getLocation().set(1, 0, 1, 1);

        npc.setBaseChat(new ChatLimit(), 46L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitLv().set(1, 10);
        npc.setChat(chatLimit, 47L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitLv().set(11, Config.MAX_LV);
        chatLimit.getNotClearedQuest().add(QuestList.PROVE_EXPERIENCE.getId());
        chatLimit.getNotRunningQuest().add(QuestList.PROVE_EXPERIENCE.getId());
        npc.setChat(chatLimit, 52L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitCloseRate().getMin().put(NpcList.MOO_MYEONG.getId(), 10);
        npc.setChat(chatLimit, 56L);

        Config.unloadObject(npc);


        npc = new Npc(NpcList.SELINA, 48L);
        npc.getLocation().set(1, 0, 2, 2);

        npc.setBaseChat(new ChatLimit(), 49L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(9, 20);
        npc.setChat(chatLimit, 50L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(21, 23);
        chatLimit.getLimitHour2().set(0, 8);
        chatLimit.getLimitCloseRate().addMax(NpcList.SELINA.getId(), 9);
        npc.setChat(chatLimit, 51L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitCloseRate().addMin(NpcList.SELINA.getId(), 10);
        npc.setChat(chatLimit, 57L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitCloseRate().addMin(NpcList.SELINA.getId(), 10);
        chatLimit.getNotRunningQuest().add(QuestList.POWER_OF_TOKEN.getId());
        npc.setChat(chatLimit, 62L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitCloseRate().addMin(NpcList.SELINA.getId(), 10);
        chatLimit.getNotRunningQuest().add(QuestList.MAGIC_OF_SPIDER_EYE.getId());
        npc.setChat(chatLimit, 85L);

        Config.unloadObject(npc);


        npc = new Npc(NpcList.SYLVIA, 128L);
        npc.getLocation().set(3, 1, 16, 16);

        Config.unloadObject(npc);


        Config.ID_COUNT.put(Id.NPC, Math.max(Config.ID_COUNT.get(Id.NPC), 12L));
        Logger.i("ObjectMaker", "Npc making is done!");
    }

}
