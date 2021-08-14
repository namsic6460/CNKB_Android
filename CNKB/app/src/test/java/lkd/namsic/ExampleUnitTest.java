package lkd.namsic;

import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.util.Random;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.FileManager;

public class ExampleUnitTest implements Serializable {

    @Test
    public void evalTest() {
        try {
            System.out.println("test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws Exception {
        File[] files = new File("C:\\Users\\user\\Downloads\\players").listFiles();

        for(File file : files) {
            Player player = Config.fromJson(FileManager.read(file), Player.class);

            if(!player.getEquipInventory().isEmpty()) {
                System.out.println(player.getName() + ": " + player.getEquipInventory());
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