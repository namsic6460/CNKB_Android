package lkd.namsic;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.Serializable;

import lkd.namsic.game.base.Bool;
import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.ConcurrentArrayList;
import lkd.namsic.game.base.Int;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.base.Use;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.event.DamageEvent;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.gameObject.Entity;
import lkd.namsic.game.gameObject.Equipment;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.json.ChatLimitAdapter;
import lkd.namsic.game.json.EntityAdapter;
import lkd.namsic.game.json.EquipAdapter;
import lkd.namsic.game.json.LocationAdapter;
import lkd.namsic.game.json.NpcAdapter;
import lkd.namsic.game.json.UseAdapter;

public class ExampleUnitTest implements Serializable {

    @Test
    public void evalTest() {
        try {
            Config.class.getDeclaredField("OBJECT").get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
         Gson gson = new GsonBuilder()
                .registerTypeAdapter(Npc.class, new NpcAdapter())
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .registerTypeAdapter(Equipment.class, new EquipAdapter())
                .registerTypeAdapter(Player.class, new EntityAdapter<>(Player.class))
                .registerTypeAdapter(Use.class, new UseAdapter())
                .registerTypeAdapter(ChatLimit.class, new ChatLimitAdapter())
                .setVersion(1.0)
                .create();

        Player player = new Player("a", "a", "a", "a");
        player.addEvent(new DamageEvent(5) {
            @Override
            public boolean onDamage(@NonNull Entity self, @NonNull Entity victim, Int totalDmg, Int totalDra, Bool isCrit) {
                return false;
            }
        });
        player.getEquipEvents().put(EquipType.AMULET, new ConcurrentArrayList<Event>() {{
            add(new DamageEvent(0) {
                @Override
                public boolean onDamage(@NonNull Entity self, @NonNull Entity victim, Int totalDmg, Int totalDra, Bool isCrit) {
                    return false;
                }
            });
        }});

        String json = gson.toJson(player);
        System.out.println(json);

        player = gson.fromJson(json, Player.class);
        System.out.println(player.getEvents());
        System.out.println(player.getEquipEvents());
    }

}