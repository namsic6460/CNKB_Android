package lkd.namsic;

import org.junit.Test;

import java.io.Serializable;
import java.util.Random;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.object.Equipment;

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
    public void test() {
        double totalIncrease = 0;
        double statIncrease = Config.REINFORCE_EFFICIENCY + Config.REINFORCE_EFFICIENCY_PER_HANDLE_LV * 1;
        double newStatIncrease = statIncrease;

        System.out.println(Config.getDisplayPercent(statIncrease));

        for(int i = 0; i < 6; i++) {
            totalIncrease += newStatIncrease;
            newStatIncrease *= 1 + statIncrease;

            System.out.println(Config.getDisplayPercent(totalIncrease));
        }

        System.out.println(Config.getDisplayPercent(totalIncrease));
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