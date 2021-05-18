package lkd.namsic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.junit.Test;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.json.NpcSerializer;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Npc npc = new Npc("노아");
        npc.getId().setObjectId(3L);
        npc.getLocation().set(0, 0, 16, 16);
        npc.setFirstChat(5L);
        npc.setCommonChat(new ChatLimit(), 6L);
        npc.setChat(new ChatLimit(), 7L);
        ChatLimit chatLimit = new ChatLimit();
        chatLimit.getRunningQuest().add(1L);
        npc.setChat(chatLimit, 8L);
        chatLimit = new ChatLimit();
        chatLimit.getRunningQuest().add(2L);
        npc.setChat(chatLimit, 9L);

        JsonElement element = new NpcSerializer().serialize(npc, Npc.class, null);
        System.out.println(element.toString());
    }
}