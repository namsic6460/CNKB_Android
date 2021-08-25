package lkd.namsic.game.creator;

import java.util.Objects;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.object.Shop;
import lkd.namsic.setting.Logger;

public class ShopCreator implements Creatable {

    @Override
    public void start() {
        Shop shop;


        shop = new Shop(NpcList.NOAH);

        shop.addSellItem(ItemList.LOW_RECIPE, 800L);

        shop.addBuyItem(ItemList.SHEEP_LEATHER, 10L);
        shop.addBuyItem(ItemList.WOOL, 5L);
        shop.addBuyItem(ItemList.PIG_HEAD, 60L);
        shop.addBuyItem(ItemList.LEATHER, 30L);
        shop.addBuyItem(ItemList.ZOMBIE_HEAD, 60L);
        shop.addBuyItem(ItemList.ZOMBIE_SOUL, 500L);
        shop.addBuyItem(ItemList.ZOMBIE_HEART, 100L);
        shop.addBuyItem(ItemList.PIECE_OF_SLIME, 65L);
        shop.addBuyItem(ItemList.SPIDER_LEG, 125L);
        shop.addBuyItem(ItemList.SPIDER_EYE, 150L);
        shop.addBuyItem(ItemList.MAGIC_STONE, 110L);
        shop.addBuyItem(ItemList.OAK_TOOTH, 200L);
        shop.addBuyItem(ItemList.OAK_LEATHER, 175L);
        shop.addBuyItem(ItemList.PIECE_OF_BONE, 30L);
        shop.addBuyItem(ItemList.HORN_OF_IMP, 150L);
        shop.addBuyItem(ItemList.IMP_HEART, 300L);
        shop.addBuyItem(ItemList.HORN_OF_LOW_DEVIL, 250L);
        shop.addBuyItem(ItemList.LOW_DEVIL_SOUL, 1500L);
        shop.addBuyItem(ItemList.HARPY_WING, 200L);
        shop.addBuyItem(ItemList.HARPY_NAIL, 200L);
        shop.addBuyItem(ItemList.GOLEM_CORE, 500L);

        Config.unloadObject(shop);


        shop = new Shop(NpcList.EL);

        shop.addSellItem(ItemList.HERB, 3L);
        shop.addSellItem(ItemList.LEAF, 3L);
        shop.addSellItem(ItemList.LOW_HP_POTION, 50L);
        shop.addSellItem(ItemList.HP_POTION, 400L);
        shop.addSellItem(ItemList.HIGH_HP_POTION, 1500L);
        shop.addSellItem(ItemList.LOW_MP_POTION, 50L);
        shop.addSellItem(ItemList.MP_POTION, 400L);
        shop.addSellItem(ItemList.HIGH_MP_POTION, 1500L);
        shop.addSellItem(ItemList.ELIXIR_HERB, 25L);
        shop.addSellItem(ItemList.LOW_ELIXIR, 130L);
        shop.addSellItem(ItemList.ELIXIR, 1000L);
        shop.addSellItem(ItemList.HIGH_ELIXIR, 4000L);
        shop.addSellItem(ItemList.SMALL_GOLD_SEED, 10000L);
        shop.addSellItem(ItemList.GOLD_SEED, 100_000L);
        shop.addSellItem(ItemList.BIG_GOLD_SEED, 500_000L);
        shop.addSellItem(ItemList.SMALL_EXP_SEED, 10000L);
        shop.addSellItem(ItemList.EXP_SEED, 100_000L);
        shop.addSellItem(ItemList.BIG_EXP_SEED, 1_000_000L);

        shop.addBuyItem(ItemList.HERB, 1L);
        shop.addBuyItem(ItemList.LEAF, 1L);
        shop.addBuyItem(ItemList.ELIXIR_HERB, 15L);
        shop.addBuyItem(ItemList.FLOWER, 10L);

        Config.unloadObject(shop);


        shop = new Shop(NpcList.HYEONG_SEOK);

        shop.addSellItem(ItemList.LOW_REINFORCE_STONE, 50L);
        shop.addSellItem(ItemList.REINFORCE_STONE, 800L);
        shop.addSellItem(ItemList.HIGH_REINFORCE_STONE, 5000L);
        shop.addSellItem(ItemList.EQUIP_SAFENER, 2500L);
        shop.addSellItem(ItemList.REINFORCE_MULTIPLIER, 1500L);
        shop.addSellItem(ItemList.GLOW_REINFORCE_MULTIPLIER, 5000L);

        Config.unloadObject(shop);


        shop = new Shop(NpcList.PEDRO);

        shop.addBuyItem(ItemList.STONE_LUMP, 30L);
        shop.addBuyItem(ItemList.COAL, 5L);

        for(ItemList gem : Config.GEMS) {
            shop.addBuyItem(gem, 1000L);
        }

        shop.addBuyItem(ItemList.QUARTZ, 20L);
        shop.addBuyItem(ItemList.GOLD, 100L);
        shop.addBuyItem(ItemList.WHITE_GOLD, 500L);

        Config.unloadObject(shop);


        shop = new Shop(NpcList.JOON_SIK);

        for(long itemId = ItemList.COMMON_FISH1.getId(); itemId <= ItemList.COMMON_FISH5.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 35L);
        }

        for(long itemId = ItemList.RARE_FISH1.getId(); itemId <= ItemList.RARE_FISH6.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 70L);
        }

        for(long itemId = ItemList.SPECIAL_FISH1.getId(); itemId <= ItemList.SPECIAL_FISH10.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 180L);
        }

        for(long itemId = ItemList.UNIQUE_FISH1.getId(); itemId <= ItemList.UNIQUE_FISH7.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 350L);
        }

        for(long itemId = ItemList.LEGENDARY_FISH1.getId(); itemId <= ItemList.LEGENDARY_FISH4.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 700L);
        }

        for(long itemId = ItemList.MYSTIC_FISH1.getId(); itemId <= ItemList.MYSTIC_FISH2.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 2000L);
        }

        Config.unloadObject(shop);


        shop = new Shop(NpcList.SELINA);

        shop.addSellItem(ItemList.EMPTY_SPHERE, 1000L);
        shop.addSellItem(ItemList.SKILL_BOOK_MAGIC_BALL, 1000L);
        shop.addSellItem(ItemList.SKILL_BOOK_SMITE, 3000L);
        shop.addSellItem(ItemList.SKILL_BOOK_LASER, 10000L);

        for(long itemId = ItemList.RED_SPHERE.getId(); itemId <= ItemList.WHITE_SPHERE.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 90L);
        }

        Config.unloadObject(shop);


        Logger.i("ObjectMaker", "Shop making is done!");
    }

}
