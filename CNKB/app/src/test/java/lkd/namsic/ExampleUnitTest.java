package lkd.namsic;

import org.junit.Test;

import java.io.Serializable;
import java.util.Random;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.enums.object.ShopList;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.object.Shop;

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

    @Test
    public void shopPriceTest() {
        Shop shop = new Shop(ShopList.EL_FLOWER);

        shop.addSellItem(ItemList.HERB, 3L);
        shop.addSellItem(ItemList.LEAF, 3L);
        shop.addSellItem(ItemList.LOW_HP_POTION, 50L);
        shop.addSellItem(ItemList.HP_POTION, 500L);
        shop.addSellItem(ItemList.HIGH_HP_POTION, 2500L);
        shop.addSellItem(ItemList.LOW_MP_POTION, 50L);
        shop.addSellItem(ItemList.MP_POTION, 500L);
        shop.addSellItem(ItemList.HIGH_MP_POTION, 2500L);
        shop.addSellItem(ItemList.ELIXIR_HERB, 30L);
        shop.addSellItem(ItemList.LOW_ELIXIR, 150L);
        shop.addSellItem(ItemList.ELIXIR, 1500L);
        shop.addSellItem(ItemList.HIGH_ELIXIR, 7500L);

        shop.addBuyItem(ItemList.HERB, 1L);
        shop.addBuyItem(ItemList.LEAF, 1L);
        shop.addBuyItem(ItemList.ELIXIR_HERB, 15L);
        shop.addBuyItem(ItemList.FLOWER, 10L);

        Player player = new Player("", "", "", "");

        System.out.println(shop.getSellPrice(player, ItemList.HIGH_HP_POTION.getId(), NpcList.EL.getId()));

        player.setCloseRate(NpcList.EL.getId(), 100);
        System.out.println(shop.getSellPrice(player, ItemList.HIGH_HP_POTION.getId(), NpcList.EL.getId()));
    }

}