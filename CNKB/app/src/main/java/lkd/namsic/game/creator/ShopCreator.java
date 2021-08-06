package lkd.namsic.game.creator;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.ShopList;
import lkd.namsic.game.object.Shop;
import lkd.namsic.setting.Logger;

public class ShopCreator implements Creatable {

    @Override
    public void start() {
        Shop shop;


        shop = new Shop(ShopList.EL_FLOWER);

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

        Config.unloadObject(shop);


        shop = new Shop(ShopList.HYEONG_SEOK_REINFORCE);

        shop.addSellItem(ItemList.LOW_REINFORCE_STONE, 50L);
        shop.addSellItem(ItemList.REINFORCE_STONE, 800L);
        shop.addSellItem(ItemList.HIGH_REINFORCE_STONE, 5000L);

        Config.unloadObject(shop);


        Config.ID_COUNT.put(Id.SHOP, Math.max(Config.ID_COUNT.get(Id.SHOP), 2L));
        Logger.i("ObjectMaker", "Shop making is done!");
    }

}
