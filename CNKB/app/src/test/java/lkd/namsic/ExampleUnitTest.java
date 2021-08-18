package lkd.namsic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.File;
import java.util.Random;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.MonsterList;
import lkd.namsic.game.json.ChatLimitAdapter;
import lkd.namsic.game.json.LocationAdapter;
import lkd.namsic.game.json.NpcAdapter;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Monster;
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
        Monster monster = new Monster(MonsterList.OAK);
        monster.getLv().set(90);

        monster.setBasicStat(StatType.MAXHP, 300);
        monster.setBasicStat(StatType.HP, 300);
        monster.setBasicStat(StatType.ATK, 40);
        monster.setBasicStat(StatType.ATS, 150);
        monster.setBasicStat(StatType.DEF, 30);
        monster.setBasicStat(StatType.MDEF, 15);
        monster.setBasicStat(StatType.BRE, 50);
        monster.setBasicStat(StatType.ACC, 60);
        monster.setBasicStat(StatType.EVA, 30);

        System.out.println(monster.getBasicStat());

        monster.randomLevel();

        System.out.println(monster.getLv().get());
        System.out.println(monster.getBasicStat());
    }

    @Test
    public void loadTest() throws Exception {
        File[] files = new File("C:\\Users\\user\\Downloads\\players").listFiles();
        Gson gson  =new GsonBuilder()
                .registerTypeAdapter(Npc.class, new NpcAdapter())
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .registerTypeAdapter(ChatLimit.class, new ChatLimitAdapter())
                .create();

        assert files != null;

        String jsonString;
        for(File file : files) {
            jsonString = FileManager.read(file);

            try {
                gson.fromJson(jsonString, Player.class);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(file.getName());
                System.out.println(jsonString);

                return;
            }
        }
    }

    @Test
    public void reinforceTest() {
        int total = 0;

        Equipment equipment = new Equipment(EquipType.WEAPON, EquipList.HEAD_HUNTER_1, "");
        equipment.getHandleLv().set(6);

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