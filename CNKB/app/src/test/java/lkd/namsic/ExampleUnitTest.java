package lkd.namsic;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.object.Equipment;

public class ExampleUnitTest {

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
        String command = "장비 완화제 1";
        List<String> commands = Arrays.asList(command.split(" "));

        String itemName = command;
        String lastWord = commands.get(commands.size() - 1);

        int count = 1;
        try {
            count = Integer.parseInt(lastWord);
            itemName = Config.replaceLast(command, lastWord, "");
        } catch (NumberFormatException ignore) {}

        String other = null;
        String[] split = itemName.split(",");

        if(split.length == 2) {
            itemName = split[0];
            other = split[1].trim();
        }

        System.out.println(itemName);
        System.out.println(other);
        System.out.println(count);
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