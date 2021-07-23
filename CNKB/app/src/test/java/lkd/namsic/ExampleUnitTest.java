package lkd.namsic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.junit.Test;

import java.io.File;
import java.io.Serializable;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.object_list.QuestList;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.json.EntityAdapter;
import lkd.namsic.setting.FileManager;

public class ExampleUnitTest implements Serializable {

    @Test
    public void evalTest() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        Npc npc = new Npc("준식");
        npc.getLocation().set(1, 1,32, 32);
        npc.setFirstChat(35L);

        ChatLimit chatLimit = new ChatLimit();
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
        npc.setChat(chatLimit, 39L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitQuest().addMin(QuestList.NEED_FISHING_ROD_ITEM.getId(), 1);
        chatLimit.getNotClearedQuest().add(QuestList.MEMORIAL_CEREMONY.getId());
        chatLimit.getNotRunningQuest().add(QuestList.MEMORIAL_CEREMONY.getId());
        npc.setChat(chatLimit, 81L);

        System.out.println(npc.getAvailableChat(new Player("a", "a", "a", "a")));
    }

    @Test
    public void recover() throws Exception {
        File[] files = new File("C:\\Users\\user\\Downloads\\players").listFiles();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Player.class, new EntityAdapter<>(Player.class))
                .create();

        Player player;
        String json;
        for(File file : files) {
            json = FileManager.read(file);
            player = gson.fromJson(json, Player.class);

            player.getEquipEvents().clear();
            player.getEquipStat().clear();
            player.getEquipped().clear();
            player.getEquipInventory().clear();

            json = gson.toJson(player);
            FileManager.save(file.getAbsolutePath(), json);
        }
    }

}