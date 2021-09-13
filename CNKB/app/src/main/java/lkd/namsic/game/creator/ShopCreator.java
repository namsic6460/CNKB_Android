package lkd.namsic.game.creator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.object.Shop;
import lkd.namsic.setting.Logger;

public class ShopCreator implements Creatable {

    @Override
    public void start() {
        Shop shop;
        Set<Long> idSet;


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

        idSet = new HashSet<>(Arrays.asList(ItemList.SHEEP_LEATHER.getId(), ItemList.WOOL.getId()));
        shop.addSimpleMap(idSet, "양", "sheep");

        idSet = new HashSet<>(Arrays.asList(ItemList.ZOMBIE_HEAD.getId(), ItemList.ZOMBIE_SOUL.getId(), ItemList.ZOMBIE_HEART.getId()));
        shop.addSimpleMap(idSet, "좀비", "zombie");

        idSet = new HashSet<>(Arrays.asList(ItemList.SPIDER_LEG.getId(), ItemList.SPIDER_EYE.getId()));
        shop.addSimpleMap(idSet, "거미", "spider");

        idSet = new HashSet<>(Arrays.asList(ItemList.OAK_TOOTH.getId(), ItemList.OAK_LEATHER.getId()));
        shop.addSimpleMap(idSet, "오크", "oak");

        Config.unloadObject(shop);


        shop = new Shop(NpcList.EL);

        shop.addSellItem(ItemList.FLOWER, 10L);
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

        shop.addSellItem(ItemList.PIECE_OF_GEM, 20000L);

        shop.addBuyItem(ItemList.STONE_LUMP, 30L);
        shop.addBuyItem(ItemList.COAL, 5L);

        for(ItemList gem : Config.GEMS) {
            shop.addBuyItem(gem, 1000L);
        }

        shop.addBuyItem(ItemList.QUARTZ, 20L);
        shop.addBuyItem(ItemList.GOLD, 100L);
        shop.addBuyItem(ItemList.WHITE_GOLD, 500L);

        idSet = Arrays.stream(Config.GEMS).map(ItemList::getId).collect(Collectors.toSet());
        shop.addSimpleMap(idSet, "감정", "appraise", "apr");

        idSet = new HashSet<>(idSet);
        idSet.remove(ItemList.QUARTZ.getId());
        idSet.remove(ItemList.GOLD.getId());
        idSet.remove(ItemList.WHITE_GOLD.getId());
        shop.addSimpleMap(idSet, "보석", "gem");

        Config.unloadObject(shop);


        shop = new Shop(NpcList.JOON_SIK);

        idSet = new HashSet<>();
        for(long itemId = ItemList.COMMON_FISH1.getId(); itemId <= ItemList.COMMON_FISH5.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 50L);
            idSet.add(itemId);
        }

        shop.addSimpleMap(idSet, "일반", "common");

        idSet = new HashSet<>();
        for(long itemId = ItemList.RARE_FISH1.getId(); itemId <= ItemList.RARE_FISH6.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 100L);
            idSet.add(itemId);
        }

        shop.addSimpleMap(idSet, "희귀", "rare");

        idSet = new HashSet<>();
        for(long itemId = ItemList.SPECIAL_FISH1.getId(); itemId <= ItemList.SPECIAL_FISH10.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 220L);
            idSet.add(itemId);
        }

        shop.addSimpleMap(idSet, "특별", "special");

        idSet = new HashSet<>();
        for(long itemId = ItemList.UNIQUE_FISH1.getId(); itemId <= ItemList.UNIQUE_FISH7.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 500L);
            idSet.add(itemId);
        }

        shop.addSimpleMap(idSet, "유일", "unique");

        idSet = new HashSet<>();
        for(long itemId = ItemList.LEGENDARY_FISH1.getId(); itemId <= ItemList.LEGENDARY_FISH4.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 1000L);
            idSet.add(itemId);
        }

        shop.addSimpleMap(idSet, "전설", "legendary", "legend");

        idSet = new HashSet<>();
        for(long itemId = ItemList.MYSTIC_FISH1.getId(); itemId <= ItemList.MYSTIC_FISH2.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 10000L);
            idSet.add(itemId);
        }

        shop.addSimpleMap(idSet, "신화", "mystic");

        Config.unloadObject(shop);


        shop = new Shop(NpcList.SELINA);

        shop.addSellItem(ItemList.EMPTY_SPHERE, 1000L);
        shop.addSellItem(ItemList.PIECE_OF_LOW_AMULET, 1000L);
        shop.addSellItem(ItemList.PIECE_OF_AMULET, 9000L);
        shop.addSellItem(ItemList.PIECE_OF_HIGH_AMULET, 85000L);
        shop.addSellItem(ItemList.SKILL_BOOK_MAGIC_BALL, 1000L);
        shop.addSellItem(ItemList.SKILL_BOOK_SMITE, 3000L);
        shop.addSellItem(ItemList.SKILL_BOOK_LASER, 10000L);

        idSet = new HashSet<>();
        for(long itemId = ItemList.RED_SPHERE.getId(); itemId <= ItemList.WHITE_SPHERE.getId(); itemId++) {
            shop.addBuyItem(Objects.requireNonNull(ItemList.idMap.get(itemId)), 90L);
            idSet.add(itemId);
        }

        shop.addSimpleMap(idSet, "구체", "sphere");

        Config.unloadObject(shop);


        shop = new Shop(NpcList.ELWOOD);

        shop.addSellItem(ItemList.CONFIRMED_LOW_RECIPE, 750L);

        shop.addBuyItem(ItemList.MAGIC_STONE, 120L);
        shop.addBuyItem(ItemList.HORN_OF_IMP, 150L);
        shop.addBuyItem(ItemList.IMP_HEART, 300L);
        shop.addBuyItem(ItemList.HORN_OF_LOW_DEVIL, 250L);
        shop.addBuyItem(ItemList.LOW_DEVIL_SOUL, 1500L);
        shop.addBuyItem(ItemList.HARPY_WING, 200L);
        shop.addBuyItem(ItemList.HARPY_NAIL, 200L);
        shop.addBuyItem(ItemList.GOLEM_CORE, 500L);
        shop.addBuyItem(ItemList.PIECE_OF_MAGIC, 150L);
        shop.addBuyItem(ItemList.OWLBEAR_LEATHER, 400L);
        shop.addBuyItem(ItemList.OWLBEAR_HEAD, 800L);
        shop.addBuyItem(ItemList.HARDENED_SLIME, 500L);

        idSet = new HashSet<>();
        idSet.add(ItemList.HORN_OF_IMP.getId());
        idSet.add(ItemList.IMP_HEART.getId());
        shop.addSimpleMap(idSet, "임프", "imp");

        idSet = new HashSet<>();
        idSet.add(ItemList.HORN_OF_LOW_DEVIL.getId());
        idSet.add(ItemList.LOW_DEVIL_SOUL.getId());
        shop.addSimpleMap(idSet, "하급 악마", "low devil");

        idSet = new HashSet<>();
        idSet.add(ItemList.HARPY_WING.getId());
        idSet.add(ItemList.HARPY_NAIL.getId());
        shop.addSimpleMap(idSet, "하피", "harpy");

        idSet = new HashSet<>();
        idSet.add(ItemList.OWLBEAR_LEATHER.getId());
        idSet.add(ItemList.OWLBEAR_HEAD.getId());
        shop.addSimpleMap(idSet, "아울베어", "owlbear");

        Config.unloadObject(shop);


        shop = new Shop(NpcList.FREY);

        shop.addSellItem(ItemList.ELIXIR_HERB, 22L);
        shop.addSellItem(ItemList.SMALL_PURIFYING_SEED, 10000L);
        shop.addSellItem(ItemList.PURIFYING_SEED, 100_000L);
        shop.addSellItem(ItemList.BIG_PURIFYING_SEED, 1_500_000L);
        shop.addSellItem(ItemList.FARM_EXPAND_DEED, 50000L);

        shop.addBuyItem(ItemList.HERB, 1L);
        shop.addBuyItem(ItemList.LEAF, 1L);
        shop.addBuyItem(ItemList.ELIXIR_HERB, 15L);

        Config.unloadObject(shop);


        Logger.i("ObjectMaker", "Shop making is done!");
    }

}
