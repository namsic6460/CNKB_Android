package lkd.namsic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.enums.object_list.QuestList;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.gameObject.Use;
import lkd.namsic.game.json.ChatLimitAdapter;
import lkd.namsic.game.json.LocationAdapter;
import lkd.namsic.game.json.NpcAdapter;
import lkd.namsic.game.json.UseAdapter;

public class ExampleUnitTest {

    @Test
    public void evalTest() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        System.out.println("n eval Player.getMethod(\"setBuffStat\", StatType, int).invoke(self,StatType.getDeclaredMethod(\"valueOf\", String).invoke(null, \"ATK\"), toInt(\"0\")))"
                .replaceAll("toInt\\(",
                "Double.getMethod(\"intValue\").invoke(Double.getDeclaredMethod(\"valueOf\", String).invoke(null, "));
    }

    @Test
    public void test() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Npc.class, new NpcAdapter())
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .registerTypeAdapter(Use.class, new UseAdapter())
                .registerTypeAdapter(ChatLimit.class, new ChatLimitAdapter())
                .setVersion(1.0)
                .setPrettyPrinting()
                .create();

        Npc npc = new Npc("노아");
        npc.getLocation().set(0, 0, 16, 16);
        npc.setFirstChat(5L);

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

        String jsonString = gson.toJson(npc);
        System.out.println(jsonString);

        npc = gson.fromJson(jsonString, Npc.class);
        System.out.println(npc.getBaseChat().toString());
    }

}