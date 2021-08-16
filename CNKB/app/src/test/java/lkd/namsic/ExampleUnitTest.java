package lkd.namsic;

import org.junit.Test;

import java.util.Random;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.MonsterList;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Monster;

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

    public long getNeedExp(int lv) {
        long needExp;
        if (lv <= 100) {
            needExp = 10_000 + 5_000 * lv;
        } else if (lv <= 300) {
            needExp = -200_000 + (long) (Math.pow(lv - 10, 2.5) * 2) + 10_000 * lv;
        } else if (lv <= 500) {
            needExp = -50_000_000 + 200_000 * lv;
        } else if(lv <= 750) {
            needExp = -50_000_000 + (long) (Math.pow(lv, 3.1) / 1.5);
        } else if(lv <= 950) {
            needExp = 45_000_000L * (lv - 750) + 1_000_000_000L;
        } else {
            needExp = -30_000_000_000L + (long) Math.pow(lv - 600, 4.2);
        }

        return needExp;
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