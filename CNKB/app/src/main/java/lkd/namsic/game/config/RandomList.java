package lkd.namsic.game.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;

public class RandomList {

    public final static int[][] REINFORCE_LV_INCREASE = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 10, 30},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 10, 20, 50},
            {0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 10, 20, 30, 50, 100},
            {0, 0, 5, 5, 5, 5, 5, 5, 10, 15, 20, 30, 50, 100, 100},
            {5, 5, 5, 5, 5, 5, 5, 10, 15, 25, 30, 50, 100, 100, 100},
            {5, 5, 5, 5, 5, 10, 15, 25, 45, 65, 80, 100, 100, 100, 100},
            {5, 5, 5, 10, 15, 25, 40, 60, 80, 100, 100, 100, 100, 100, 150},
            {5, 10, 15, 20, 25, 40, 60, 80, 100, 100, 100, 100, 100, 150, 150},
            {10, 20, 20, 20, 40, 40, 60, 80, 100, 100, 100, 100, 150, 150, 150},
            {20, 40, 40, 40, 40, 40, 60, 80, 100, 100, 150, 150, 150, 150, 200},
            {25, 50, 50, 50, 50, 50, 75, 75, 100, 100, 150, 150, 200, 200, 200},
            {30, 60, 60, 60, 60, 80, 80, 100, 150, 150, 200, 200, 200, 200, 200},
            {50, 100, 100, 100, 100, 150, 150, 150, 150, 200, 200, 200, 250, 250, 500}
    };

    public final static double[][] REINFORCE_PERCENT = new double[][]{
            {1, 1, 1, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0.1, 0.1, 0.05},
            {1, 1, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0.1, 0.08, 0.05, 0.03},
            {1, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0.08, 0.05, 0.05, 0.05, 0.03, 0.01},
            {1, 0.8, 0.7, 0.6, 0.5, 0.4, 0.25, 0.1, 0.08, 0.06, 0.04, 0.03, 0.03, 0.03, 0.01},
            {1, 0.8, 0.7, 0.6, 0.45, 0.3, 0.15, 0.08, 0.05, 0.05, 0.03, 0.03, 0.03, 0.01, 0.01},
            {0.8, 0.7, 0.6, 0.45, 0.3, 0.15, 0.08, 0.05, 0.03, 0.03, 0.03, 0.03, 0.02, 0.01, 0.01},
            {0.8, 0.7, 0.6, 0.45, 0.3, 0.15, 0.08, 0.05, 0.03, 0.03, 0.02, 0.01, 0.005, 0.005, 0.005},
            {0.8, 0.65, 0.5, 0.35, 0.2, 0.1, 0.07, 0.04, 0.02, 0.02, 0.01, 0.01, 0.005, 0.005, 0.003},
            {0.8, 0.65, 0.5, 0.35, 0.2, 0.1, 0.07, 0.04, 0.02, 0.01, 0.005, 0.005, 0.005, 0.003, 0.001},
            {0.8, 0.6, 0.4, 0.2, 0.1, 0.08, 0.06, 0.04, 0.02, 0.01, 0.005, 0.005, 0.003, 0.001, 0.001},
            {0.6, 0.4, 0.2, 0.1, 0.08, 0.06, 0.04, 0.02, 0.01, 0.005, 0.005, 0.003, 0.001, 0.001, 0.001},
            {0.5, 0.3, 0.2, 0.1, 0.08, 0.06, 0.04, 0.02, 0.01, 0.005, 0.003, 0.001, 0.001, 0.001, 0.001},
            {0.3, 0.1, 0.05, 0.03, 0.01, 0.005, 0.003, 0.001, 0.001, 0.001, 0.001, 0.001, 0.001, 0.001, 0.001},
    };

    public static final List<List<Double>> MINE_PERCENT = Arrays.asList(
            Arrays.asList(50D, 35D, 14D, 1D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D),
            Arrays.asList(45D, 35D, 15D, 4.5D, 0.5D, 0D, 0D, 0D, 0D, 0D, 0D, 0D),
            Arrays.asList(40D, 20D, 20D, 14D, 5.5D, 0.5D, 0D, 0D, 0D, 0D, 0D, 0D),
            Arrays.asList(35D, 10D, 15D, 20D, 12D, 7.6D, 0.4D, 0D, 0D, 0D, 0D, 0D),
            Arrays.asList(30D, 0D, 0D, 15D, 40D, 10D, 4.98D, 0.019D, 0.001D, 0D, 0D, 0D),
            Arrays.asList(30D, 0D, 0D, 0D, 20D, 30D, 15D, 4.85D, 0.149D, 0.001D, 0D, 0D),
            Arrays.asList(30D, 0D, 0D, 0D, 0D, 25D, 25D, 15D, 4.79D, 0.2095D, 0.0005D, 0D),
            Arrays.asList(25D, 0D, 0D, 0D, 0D, 0D, 20D, 32.5D, 17.5D, 4.99D, 0.009D, 0.001D),
            Arrays.asList(20D, 0D, 0D, 0D, 0D, 0D, 0D, 30.5D, 30.3D, 19.042D, 0.1577D, 0.0003D)
    );

    public static final List<List<Long>> MINE_OUTPUT = Arrays.asList(
            Collections.singletonList(ItemList.STONE.getId()),
            Collections.singletonList(ItemList.COAL.getId()),
            Arrays.asList(ItemList.COPPER.getId(), ItemList.LEAD.getId(), ItemList.TIN.getId(), ItemList.NICKEL.getId()),
            Arrays.asList(ItemList.IRON.getId(), ItemList.LITHIUM.getId()),
            Arrays.asList(ItemList.LAPIS.getId(), ItemList.RED_STONE.getId()),
            Collections.singletonList(ItemList.SILVER.getId()),
            Collections.singletonList(ItemList.GLOW_STONE.getId()),
            Arrays.asList(ItemList.FIRE_QUARTZ.getId(), ItemList.DARK_QUARTZ.getId()),
            Arrays.asList(ItemList.GLOW_LAPIS.getId(), ItemList.GLOW_RED_STONE.getId()),
            Arrays.asList(ItemList.HARD_COAL.getId(), ItemList.TITANIUM.getId()),
            Collections.singletonList(ItemList.ORICHALCON.getId()),
            Arrays.asList(ItemList.LAPIS_RED_STONE.getId(), ItemList.LANDIUM.getId(), ItemList.AITUME.getId())
    );

    public static final List<Integer> MINE_LV_REQUIREMENT = Arrays.asList(
            30, 100, 300, 800, 2000, 5000, 15000, 50000
    );

    public static final List<List<Integer>> FISH_PERCENT = Arrays.asList(
            Arrays.asList(70, 30, 0, 0, 0, 0, 0),
            Arrays.asList(50, 40, 10, 0, 0, 0, 0),
            Arrays.asList(10, 50, 35, 5, 0, 0, 0),
            Arrays.asList(0, 30, 50, 20, 0, 0, 0),
            Arrays.asList(0, 10, 45, 40, 5, 0, 0),
            Arrays.asList(0, 0, 20, 50, 30, 0, 0),
            Arrays.asList(0, 0, 10, 40, 45, 5, 0),
            Arrays.asList(0, 0, 5, 20, 65, 10, 0),
            Arrays.asList(0, 0, 0, 15, 65, 15, 5)
    );

    public static final List<Integer> FISH_LV_REQUIREMENT = Arrays.asList(
            10, 30, 50, 100, 300, 800, 1000, 2000
    );

    public static final List<Long> FARM_UPGRADE_PRICE = Arrays.asList(
            100_000L, 500_000L, 3_000_000L, 10_000_000L
    );

    public static final Map<Integer, Map<Long, Integer>> ADVENTURE_LIST = new HashMap<Integer, Map<Long, Integer>>() {{
        put(1, new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 150000);
            put(ItemList.BRANCH.getId(), 150000);
            put(ItemList.LEAF.getId(), 150000);
            put(ItemList.GRASS.getId(), 150000);
            put(ItemList.DIRT.getId(), 150000);
            put(ItemList.SAND.getId(), 150000);
            put(ItemList.HERB.getId(), 100000);
        }});
        put(2, new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 140000);
            put(ItemList.BRANCH.getId(), 140000);
            put(ItemList.LEAF.getId(), 140000);
            put(ItemList.GRASS.getId(), 140000);
            put(ItemList.DIRT.getId(), 140000);
            put(ItemList.SAND.getId(), 140000);
            put(ItemList.HERB.getId(), 100000);
            put(ItemList.EMPTY_SPHERE.getId(), 40000);
            put(ItemList.LOW_ADV_TOKEN.getId(), 20000);
        }});
        put(3, new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 100000);
            put(ItemList.BRANCH.getId(), 100000);
            put(ItemList.LEAF.getId(), 100000);
            put(ItemList.GRASS.getId(), 100000);
            put(ItemList.DIRT.getId(), 100000);
            put(ItemList.SAND.getId(), 100000);
            put(ItemList.HERB.getId(), 100000);
            put(ItemList.EMPTY_SPHERE.getId(), 50000);
            put(ItemList.LOW_ADV_TOKEN.getId(), 50000);
            put(ItemList.LOW_HP_POTION.getId(), 100000);
            put(ItemList.LOW_MP_POTION.getId(), 100000);
        }});
        put(4, new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 70000);
            put(ItemList.BRANCH.getId(), 70000);
            put(ItemList.LEAF.getId(), 70000);
            put(ItemList.GRASS.getId(), 70000);
            put(ItemList.DIRT.getId(), 70000);
            put(ItemList.SAND.getId(), 70000);
            put(ItemList.HERB.getId(), 80000);
            put(ItemList.EMPTY_SPHERE.getId(), 100000);
            put(ItemList.LOW_ADV_TOKEN.getId(), 100000);
            put(ItemList.LOW_HP_POTION.getId(), 150000);
            put(ItemList.LOW_MP_POTION.getId(), 150000);
        }});
        put(5, new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 50000);
            put(ItemList.BRANCH.getId(), 50000);
            put(ItemList.LEAF.getId(), 50000);
            put(ItemList.GRASS.getId(), 50000);
            put(ItemList.DIRT.getId(), 50000);
            put(ItemList.SAND.getId(), 50000);
            put(ItemList.HERB.getId(), 50000);
            put(ItemList.EMPTY_SPHERE.getId(), 100000);
            put(ItemList.LOW_ADV_TOKEN.getId(), 100000);
            put(ItemList.LOW_HP_POTION.getId(), 150000);
            put(ItemList.LOW_MP_POTION.getId(), 150000);
            put(ItemList.SMALL_GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 50000);
        }});
        put(6, new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 50000);
            put(ItemList.BRANCH.getId(), 50000);
            put(ItemList.LEAF.getId(), 50000);
            put(ItemList.GRASS.getId(), 50000);
            put(ItemList.DIRT.getId(), 50000);
            put(ItemList.SAND.getId(), 50000);
            put(ItemList.HERB.getId(), 50000);
            put(ItemList.EMPTY_SPHERE.getId(), 100000);
            put(ItemList.LOW_ADV_TOKEN.getId(), 50000);
            put(ItemList.ADV_TOKEN.getId(), 50000);
            put(ItemList.LOW_HP_POTION.getId(), 100000);
            put(ItemList.LOW_MP_POTION.getId(), 100000);
            put(ItemList.SMALL_GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 50000);
            put(ItemList.LOW_EXP_POTION.getId(), 50000);
            put(ItemList.LOW_RECIPE.getId(), 50000);
        }});
        put(7, new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 40000);
            put(ItemList.BRANCH.getId(), 40000);
            put(ItemList.LEAF.getId(), 40000);
            put(ItemList.GRASS.getId(), 40000);
            put(ItemList.DIRT.getId(), 40000);
            put(ItemList.SAND.getId(), 40000);
            put(ItemList.HERB.getId(), 40000);
            put(ItemList.EMPTY_SPHERE.getId(), 100000);
            put(ItemList.ADV_TOKEN.getId(), 100000);
            put(ItemList.LOW_HP_POTION.getId(), 80000);
            put(ItemList.LOW_MP_POTION.getId(), 80000);
            put(ItemList.HP_POTION.getId(), 30000);
            put(ItemList.MP_POTION.getId(), 30000);
            put(ItemList.SMALL_GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 75000);
            put(ItemList.LOW_EXP_POTION.getId(), 50000);
            put(ItemList.LOW_RECIPE.getId(), 75000);
        }});
        put(8, new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 30000);
            put(ItemList.BRANCH.getId(), 30000);
            put(ItemList.LEAF.getId(), 30000);
            put(ItemList.GRASS.getId(), 30000);
            put(ItemList.DIRT.getId(), 30000);
            put(ItemList.SAND.getId(), 30000);
            put(ItemList.HERB.getId(), 30000);
            put(ItemList.EMPTY_SPHERE.getId(), 70000);
            put(ItemList.ADV_TOKEN.getId(), 100000);
            put(ItemList.HP_POTION.getId(), 100000);
            put(ItemList.MP_POTION.getId(), 100000);
            put(ItemList.SMALL_GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 100000);
            put(ItemList.LOW_EXP_POTION.getId(), 120000);
            put(ItemList.LOW_RECIPE.getId(), 100000);
        }});
        put(9, new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 20000);
            put(ItemList.BRANCH.getId(), 20000);
            put(ItemList.LEAF.getId(), 20000);
            put(ItemList.GRASS.getId(), 20000);
            put(ItemList.DIRT.getId(), 20000);
            put(ItemList.SAND.getId(), 20000);
            put(ItemList.HERB.getId(), 20000);
            put(ItemList.EMPTY_SPHERE.getId(), 50000);
            put(ItemList.ADV_TOKEN.getId(), 100000);
            put(ItemList.HP_POTION.getId(), 100000);
            put(ItemList.MP_POTION.getId(), 100000);
            put(ItemList.SMALL_GOLD_BAG.getId(), 60000);
            put(ItemList.ADV_STAT.getId(), 100000);
            put(ItemList.LOW_EXP_POTION.getId(), 150000);
            put(ItemList.LOW_REINFORCE_STONE.getId(), 50000);
            put(ItemList.PIECE_OF_LOW_AMULET.getId(), 50000);
            put(ItemList.LOW_RECIPE.getId(), 100000);
        }});
        put(10, new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 30000);
            put(ItemList.BRANCH.getId(), 30000);
            put(ItemList.LEAF.getId(), 30000);
            put(ItemList.GRASS.getId(), 30000);
            put(ItemList.DIRT.getId(), 30000);
            put(ItemList.SAND.getId(), 30000);
            put(ItemList.HERB.getId(), 30000);
            put(ItemList.EMPTY_SPHERE.getId(), 40000);
            put(ItemList.ADV_TOKEN.getId(), 100000);
            put(ItemList.HP_POTION.getId(), 100000);
            put(ItemList.MP_POTION.getId(), 100000);
            put(ItemList.SMALL_GOLD_BAG.getId(), 50000);
            put(ItemList.ADV_STAT.getId(), 100000);
            put(ItemList.LOW_EXP_POTION.getId(), 100000);
            put(ItemList.LOW_REINFORCE_STONE.getId(), 50000);
            put(ItemList.PIECE_OF_LOW_AMULET.getId(), 50000);
            put(ItemList.LOW_RECIPE.getId(), 100000);
        }});
        put(11, new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 10000);
            put(ItemList.BRANCH.getId(), 10000);
            put(ItemList.LEAF.getId(), 10000);
            put(ItemList.GRASS.getId(), 10000);
            put(ItemList.DIRT.getId(), 10000);
            put(ItemList.SAND.getId(), 10000);
            put(ItemList.HERB.getId(), 10000);
            put(ItemList.EMPTY_SPHERE.getId(), 30000);
            put(ItemList.ADV_TOKEN.getId(), 50000);
            put(ItemList.HIGH_ADV_TOKEN.getId(), 20000);
            put(ItemList.HP_POTION.getId(), 100000);
            put(ItemList.MP_POTION.getId(), 100000);
            put(ItemList.SMALL_GOLD_BAG.getId(), 50000);
            put(ItemList.GOLD_BAG.getId(), 10000);
            put(ItemList.ADV_STAT.getId(), 100000);
            put(ItemList.LOW_EXP_POTION.getId(), 30000);
            put(ItemList.EXP_POTION.getId(), 100000);
            put(ItemList.LOW_REINFORCE_STONE.getId(), 100000);
            put(ItemList.PIECE_OF_LOW_AMULET.getId(), 100000);
            put(ItemList.LOW_RECIPE.getId(), 80000);
            put(ItemList.RECIPE.getId(), 60000);
        }});
        put(12, new HashMap<Long, Integer>() {{
            put(ItemList.EMPTY_SPHERE.getId(), 10000);
            put(ItemList.HIGH_ADV_TOKEN.getId(), 50000);
            put(ItemList.HP_POTION.getId(), 50000);
            put(ItemList.MP_POTION.getId(), 50000);
            put(ItemList.HIGH_HP_POTION.getId(), 100000);
            put(ItemList.HIGH_MP_POTION.getId(), 100000);
            put(ItemList.GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 100000);
            put(ItemList.EXP_POTION.getId(), 100000);
            put(ItemList.LOW_REINFORCE_STONE.getId(), 100000);
            put(ItemList.PIECE_OF_LOW_AMULET.getId(), 100000);
            put(ItemList.LOW_RECIPE.getId(), 80000);
            put(ItemList.RECIPE.getId(), 60000);
        }});
        put(13, new HashMap<Long, Integer>() {{
            put(ItemList.HIGH_ADV_TOKEN.getId(), 100000);
            put(ItemList.HP_POTION.getId(), 50000);
            put(ItemList.MP_POTION.getId(), 50000);
            put(ItemList.HIGH_HP_POTION.getId(), 100000);
            put(ItemList.HIGH_MP_POTION.getId(), 100000);
            put(ItemList.GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 100000);
            put(ItemList.EXP_POTION.getId(), 100000);
            put(ItemList.LOW_REINFORCE_STONE.getId(), 100000);
            put(ItemList.PIECE_OF_LOW_AMULET.getId(), 100000);
            put(ItemList.RECIPE.getId(), 100000);
        }});
        put(14, new HashMap<Long, Integer>() {{
            put(ItemList.HIGH_ADV_TOKEN.getId(), 100000);
            put(ItemList.HP_POTION.getId(), 25000);
            put(ItemList.MP_POTION.getId(), 25000);
            put(ItemList.HIGH_HP_POTION.getId(), 100000);
            put(ItemList.HIGH_MP_POTION.getId(), 100000);
            put(ItemList.GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 100000);
            put(ItemList.EXP_POTION.getId(), 100000);
            put(ItemList.LOW_REINFORCE_STONE.getId(), 100000);
            put(ItemList.PIECE_OF_LOW_AMULET.getId(), 100000);
            put(ItemList.PIECE_OF_AMULET.getId(), 50000);
            put(ItemList.RECIPE.getId(), 100000);
        }});
        put(15, new HashMap<Long, Integer>() {{
            put(ItemList.HIGH_ADV_TOKEN.getId(), 100000);
            put(ItemList.HIGH_HP_POTION.getId(), 100000);
            put(ItemList.HIGH_MP_POTION.getId(), 100000);
            put(ItemList.GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 100000);
            put(ItemList.EXP_POTION.getId(), 100000);
            put(ItemList.LOW_REINFORCE_STONE.getId(), 100000);
            put(ItemList.PIECE_OF_AMULET.getId(), 100000);
            put(ItemList.RECIPE.getId(), 100000);
            put(ItemList.PIECE_OF_GEM.getId(), 50000);
            put(ItemList.GEM_ABRASIVE_MATERIAL.getId(), 50000);
        }});
        put(16, new HashMap<Long, Integer>() {{
            put(ItemList.HIGH_ADV_TOKEN.getId(), 100000);
            put(ItemList.HIGH_HP_POTION.getId(), 100000);
            put(ItemList.HIGH_MP_POTION.getId(), 100000);
            put(ItemList.GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 50000);
            put(ItemList.EXP_POTION.getId(), 100000);
            put(ItemList.HIGH_EXP_POTION.getId(), 50000);
            put(ItemList.LOW_REINFORCE_STONE.getId(), 100000);
            put(ItemList.REINFORCE_STONE.getId(), 50000);
            put(ItemList.PIECE_OF_AMULET.getId(), 50000);
            put(ItemList.RECIPE.getId(), 50000);
            put(ItemList.PIECE_OF_GEM.getId(), 50000);
            put(ItemList.GEM_ABRASIVE_MATERIAL.getId(), 50000);
            put(ItemList.EQUIP_SAFENER.getId(), 50000);
        }});
        put(17, new HashMap<Long, Integer>() {{
            put(ItemList.HIGH_ADV_TOKEN.getId(), 100000);
            put(ItemList.HIGH_HP_POTION.getId(), 100000);
            put(ItemList.HIGH_MP_POTION.getId(), 100000);
            put(ItemList.GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 50000);
            put(ItemList.EXP_POTION.getId(), 50000);
            put(ItemList.HIGH_EXP_POTION.getId(), 50000);
            put(ItemList.REINFORCE_STONE.getId(), 100000);
            put(ItemList.PIECE_OF_AMULET.getId(), 50000);
            put(ItemList.PIECE_OF_HIGH_AMULET.getId(), 50000);
            put(ItemList.RECIPE.getId(), 50000);
            put(ItemList.HIGH_RECIPE.getId(), 50000);
            put(ItemList.PIECE_OF_GEM.getId(), 50000);
            put(ItemList.GEM_ABRASIVE_MATERIAL.getId(), 50000);
            put(ItemList.EQUIP_SAFENER.getId(), 50000);
        }});
        put(18, new HashMap<Long, Integer>() {{
            put(ItemList.HIGH_ADV_TOKEN.getId(), 100000);
            put(ItemList.HIGH_HP_POTION.getId(), 100000);
            put(ItemList.HIGH_MP_POTION.getId(), 100000);
            put(ItemList.GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 50000);
            put(ItemList.HIGH_EXP_POTION.getId(), 100000);
            put(ItemList.REINFORCE_STONE.getId(), 100000);
            put(ItemList.PIECE_OF_HIGH_AMULET.getId(), 100000);
            put(ItemList.HIGH_RECIPE.getId(), 100000);
            put(ItemList.PIECE_OF_GEM.getId(), 50000);
            put(ItemList.GEM_ABRASIVE_MATERIAL.getId(), 50000);
            put(ItemList.EQUIP_SAFENER.getId(), 50000);
        }});
        put(19, new HashMap<Long, Integer>() {{
            put(ItemList.HIGH_HP_POTION.getId(), 100000);
            put(ItemList.HIGH_MP_POTION.getId(), 100000);
            put(ItemList.GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 50000);
            put(ItemList.HIGH_EXP_POTION.getId(), 100000);
            put(ItemList.REINFORCE_STONE.getId(), 100000);
            put(ItemList.HIGH_REINFORCE_STONE.getId(), 100000);
            put(ItemList.PIECE_OF_HIGH_AMULET.getId(), 100000);
            put(ItemList.HIGH_RECIPE.getId(), 100000);
            put(ItemList.PIECE_OF_GEM.getId(), 50000);
            put(ItemList.GEM_ABRASIVE_MATERIAL.getId(), 50000);
            put(ItemList.EQUIP_SAFENER.getId(), 50000);
        }});
        put(20, new HashMap<Long, Integer>() {{
            put(ItemList.HIGH_HP_POTION.getId(), 100000);
            put(ItemList.HIGH_MP_POTION.getId(), 100000);
            put(ItemList.GOLD_BAG.getId(), 100000);
            put(ItemList.ADV_STAT.getId(), 50000);
            put(ItemList.HIGH_EXP_POTION.getId(), 100000);
            put(ItemList.REINFORCE_STONE.getId(), 100000);
            put(ItemList.HIGH_REINFORCE_STONE.getId(), 100000);
            put(ItemList.PIECE_OF_HIGH_AMULET.getId(), 100000);
            put(ItemList.HIGH_RECIPE.getId(), 100000);
            put(ItemList.PIECE_OF_GEM.getId(), 50000);
            put(ItemList.GLOW_GEM_ABRASIVE_MATERIAL.getId(), 50000);
            put(ItemList.EQUIP_SAFENER.getId(), 50000);
        }});
    }};

    public static final double[][] HUNTER_TOKEN = new double[][]{
            {0.1, 0, 0},
            {0.15, 0.05, 0},
            {0.1, 0.1, 0},
            {0.05, 0.1, 0},
            {0, 0.15, 0},
            {0, 0.2, 0},
            {0, 0.2, 0.05},
            {0, 0.15, 0.05},
            {0, 0.1, 0.05},
            {0, 0.05, 0.1},
            {0, 0.05, 0.1},
            {0, 0, 0.1},
            {0, 0, 0.1},
            {0, 0, 0.1},
            {0, 0, 0.1},
            {0, 0, 0.1},
            {0, 0, 0.1},
            {0, 0, 0.1},
            {0, 0, 0.1},
            {0, 0, 0.1},
    };

    public static final double[][] FISH_TOKEN = new double[][]{
            {}, //Empty
            {0.1, 0, 0},
            {0.15, 0.05, 0},
            {0.1, 0.05, 0},
            {0.05, 0.1, 0.02},
            {0, 0.15, 0.05},
            {0, 0.1, 0.1}
    };

    public static final double[][] MINE_TOKEN = new double[][]{
            {0.02, 0, 0},
            {0.05, 0, 0},
            {0.08, 0, 0},
            {0.11, 0, 0},
            {0.15, 0.02, 0},
            {0.15, 0.05, 0},
            {0.13, 0.08, 0},
            {0.1, 0.1, 0},
            {0.05, 0.15, 0},
            {0.03, 0.15, 0},
            {0, 0.15, 0.03},
            {0, 0.12, 0.07}
    };

    public static final List<Long> jewelries = new ArrayList<Long>() {{
        for (int i = 0; i < 100; i++) {
            this.add(ItemList.QUARTZ.getId());
        }

        for (int i = 0; i < 50; i++) {
            this.add(ItemList.GOLD.getId());
        }

        for (int i = 0; i < 20; i++) {
            this.add(ItemList.WHITE_GOLD.getId());
        }

        this.add(ItemList.DIAMOND.getId());
        this.add(ItemList.ORICHALCON.getId());
        for (long itemId = ItemList.GARNET.getId(); itemId <= ItemList.TURQUOISE.getId(); itemId++) {
            this.add(itemId);
        }
    }};

    public static long getRandomJewelry() {
        return jewelries.get(new Random().nextInt(jewelries.size()));
    }

    public static final List<Long> lowRecipeItems = Arrays.asList(
            ItemList.PIECE_OF_MANA.getId(),
            ItemList.STONE.getId(),
            ItemList.GOLD.getId(),
            ItemList.LOW_ALLOY.getId(),
            ItemList.LOW_AMULET.getId(),
            ItemList.HARD_IRON.getId(),
            ItemList.LOW_ELIXIR.getId(),
            ItemList.REINFORCE_STONE.getId(),
            ItemList.HARDENED_SLIME.getId()
    );

    public static final List<Long> middleRecipeItems = Arrays.asList(
            ItemList.GLOW_LAPIS.getId(),
            ItemList.GLOW_RED_STONE.getId(),
            ItemList.WHITE_GOLD.getId(),
            ItemList.ALLOY.getId(),
            ItemList.AMULET.getId(),
            ItemList.ELIXIR.getId(),
            ItemList.HIGH_REINFORCE_STONE.getId(),
            ItemList.MAGIC_STONE.getId(),
            ItemList.TITANIUM_STEEL.getId(),
            ItemList.TRACE_OF_SKY.getId(),
            ItemList.TRACE_OF_EARTH.getId(),
            ItemList.TRACE_OF_WATER.getId(),
            ItemList.TRACE_OF_FIRE.getId()
    );

    public static final List<Long> highRecipeItems = Arrays.asList(
            ItemList.LIQUID_STONE.getId(),
            ItemList.DIAMOND.getId(),
            ItemList.LAPIS_RED_STONE.getId(),
            ItemList.LANDIUM.getId(),
            ItemList.AITUME.getId(),
            ItemList.HIGH_ALLOY.getId(),
            ItemList.HIGH_AMULET.getId(),
            ItemList.HIGH_ELIXIR.getId()
    );

    public static final List<Long> lowRecipeEquips = Arrays.asList(
            EquipList.WOODEN_SWORD.getId(),
            EquipList.IRON_SWORD.getId(),
            EquipList.MIX_SWORD_1.getId(),
            EquipList.LEATHER_HELMET.getId(),
            EquipList.LEATHER_CHESTPLATE.getId(),
            EquipList.LEATHER_LEGGINGS.getId(),
            EquipList.LEATHER_SHOES.getId(),
            EquipList.LOW_ALLOY_HELMET.getId(),
            EquipList.LOW_ALLOY_CHESTPLATE.getId(),
            EquipList.LOW_ALLOY_LEGGINGS.getId(),
            EquipList.LOW_ALLOY_SHOES.getId(),
            EquipList.LOW_MANA_SWORD.getId(),
            EquipList.WOOL_HELMET.getId(),
            EquipList.MINER_SHOES.getId(),
            EquipList.BONE_SWORD.getId(),
            EquipList.BASIC_STAFF.getId(),
            EquipList.SEA_STAFF.getId(),
            EquipList.BONE_HELMET.getId(),
            EquipList.BONE_CHESTPLATE.getId(),
            EquipList.BONE_LEGGINGS.getId(),
            EquipList.BONE_SHOES.getId(),
            EquipList.YIN_YANG_SWORD_1.getId(),
            EquipList.GARNET_EARRING.getId(),
            EquipList.AMETHYST_EARRING.getId(),
            EquipList.AQUAMARINE_EARRING.getId(),
            EquipList.DIAMOND_EARRING.getId(),
            EquipList.EMERALD_EARRING.getId(),
            EquipList.PEARL_EARRING.getId(),
            EquipList.RUBY_EARRING.getId(),
            EquipList.PERIDOT_EARRING.getId(),
            EquipList.SAPPHIRE_EARRING.getId(),
            EquipList.OPAL_EARRING.getId(),
            EquipList.TOPAZ_EARRING.getId(),
            EquipList.TURQUOISE_EARRING.getId()
    );

    public static final List<Long> middleRecipeEquips = Arrays.asList(
            EquipList.HEART_BREAKER_1.getId(),
            EquipList.HEAD_HUNTER_1.getId(),
            EquipList.GHOST_SWORD_1.getId(),
            EquipList.QUARTZ_SWORD.getId(),
            EquipList.SLIME_HELMET.getId(),
            EquipList.SLIME_CHESTPLATE.getId(),
            EquipList.SLIME_LEGGINGS.getId(),
            EquipList.SLIME_SHOES.getId(),
            EquipList.WEIRD_LEGGINGS.getId(),
            EquipList.OAK_TOOTH_NECKLACE.getId(),
            EquipList.HARD_IRON_CHESTPLATE.getId(),
            EquipList.DEMON_STAFF.getId(),
            EquipList.DEMON_BONE_HELMET.getId(),
            EquipList.DEMON_BONE_CHESTPLATE.getId(),
            EquipList.DEMON_BONE_LEGGINGS.getId(),
            EquipList.DEMON_BONE_SHOES.getId(),
            EquipList.SILVER_SWORD.getId(),
            EquipList.SILVER_HELMET.getId(),
            EquipList.SILVER_CHESTPLATE.getId(),
            EquipList.SILVER_LEGGINGS.getId(),
            EquipList.SILVER_SHOES.getId(),
            EquipList.MIX_SWORD_2.getId(),
            EquipList.ALLOY_HELMET.getId(),
            EquipList.ALLOY_CHESTPLATE.getId(),
            EquipList.ALLOY_LEGGINGS.getId(),
            EquipList.ALLOY_SHOES.getId(),
            EquipList.TITANIUM_HELMET.getId(),
            EquipList.TITANIUM_CHESTPLATE.getId(),
            EquipList.TITANIUM_LEGGINGS.getId(),
            EquipList.TITANIUM_SHOES.getId(),
            EquipList.HARPY_NAIL_GAUNTLETS.getId(),
            EquipList.HARDENED_SLIME_HELMET.getId(),
            EquipList.HARDENED_SLIME_CHESTPLATE.getId(),
            EquipList.HARDENED_SLIME_LEGGINGS.getId(),
            EquipList.HARDENED_SLIME_SHOES.getId(),
            EquipList.SILVER_RING.getId(),
            EquipList.HARPY_NAIL_NECKLACE.getId()
    );

    public static final List<Long> highRecipeEquips = Arrays.asList(
            EquipList.HEART_BREAKER_2.getId(),
            EquipList.GHOST_SWORD_2.getId(),
            EquipList.HEAD_HUNTER_2.getId(),
            EquipList.YIN_YANG_SWORD_2.getId(),
            EquipList.OWLBEAR_LEATHER_HELMET.getId(),
            EquipList.OWLBEAR_LEATHER_CHESTPLATE.getId(),
            EquipList.OWLBEAR_LEATHER_LEGGINGS.getId(),
            EquipList.OWLBEAR_LEATHER_SHOES.getId(),
            EquipList.ELEMENT_HEART_GEM.getId(),
            EquipList.GOLEM_HEART_GEM.getId()
    );

    public static final List<Long> lowAmulets = Arrays.asList(
            EquipList.HEALTH_AMULET.getId(),
            EquipList.REGENERATION_AMULET.getId(),
            EquipList.STRENGTH_AMULET.getId(),
            EquipList.PROTECTION_AMULET.getId()
    );

    public static final List<Long> middleAmulets = Arrays.asList(
            EquipList.BLOOD_AMULET.getId(),
            EquipList.NATURE_AMULET.getId(),
            EquipList.DEVIL_AMULET.getId()
    );

    public static final List<Long> highAmulets = Arrays.asList(
            EquipList.DRAGON_AMULET.getId(),
            EquipList.DEVIL_AMULET.getId()
    );

}
