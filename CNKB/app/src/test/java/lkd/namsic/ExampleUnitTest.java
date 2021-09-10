package lkd.namsic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.object.BossList;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.json.ChatLimitAdapter;
import lkd.namsic.game.json.LocationAdapter;
import lkd.namsic.game.json.NpcAdapter;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Boss;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Npc;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.FileManager;

public class ExampleUnitTest {

    @Test
    public void evalTest() {
        try {
            System.out.println(FightManager.class.getDeclaredMethod("getInstance").invoke(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        Map<Entity, List<Entity>> castingMap = new ConcurrentHashMap<>();

        Boss boss = new Boss(BossList.WOLF_OF_MOON, "");
        castingMap.put(boss, new ArrayList<>());

        System.out.println(castingMap);

        castingMap.remove(boss);

        System.out.println(castingMap);
    }

    @Test
    public void loadTest() throws Exception {
        String path = "C:\\Users\\user\\Downloads\\cnkb\\";
        File[] files = Objects.requireNonNull(new File(path + "players").listFiles());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Npc.class, new NpcAdapter())
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .registerTypeAdapter(ChatLimit.class, new ChatLimitAdapter())
                .create();

        for(File file : files) {
            String jsonStr = FileManager.read(file);

            Player player = gson.fromJson(jsonStr, Player.class);
            for(long equipId : player.getEquipInventory()) {
                Equipment equipment = gson.fromJson(FileManager.read(new File(path + "EQUIPMENT\\" + equipId + ".json")), Equipment.class);

                if(equipment.getOriginalId() == EquipList.SILVER_HELMET.getId()) {
                    System.out.println("Helmet: " + equipId + "(" + player.getName() + ")");
                } else if(equipment.getOriginalId() == EquipList.SILVER_SHOES.getId()) {
                    System.out.println("Shoes: " + equipId + "(" + player.getName() + ")");
                }
            }
        }

        System.out.println("DONE");
    }

    @Test
    public void reinforceTest() {
        int total = 0;

        Equipment equipment = new Equipment(EquipType.WEAPON, EquipList.HEAD_HUNTER_1, null, null);
        equipment.setHandleLv(6);

        Random random = new Random();
        double basePercent;
        double percent;
        for (int i = 0; i < Config.MAX_REINFORCE_COUNT; i++) {
            basePercent = equipment.getReinforcePercent(1);

            for (int j = 1; ; j++) {
                total++;
                percent = equipment.getReinforcePercent(1);

                if (random.nextDouble() < percent) {
                    equipment.successReinforce();
                    System.out.println(i + "(" + Config.getDisplayPercent(basePercent) + "): " + j);

                    break;
                } else {
                    equipment.failReinforce(percent);
                }
            }
        }

        System.out.println(total);
    }

}