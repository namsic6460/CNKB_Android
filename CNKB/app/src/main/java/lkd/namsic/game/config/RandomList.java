package lkd.namsic.game.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lkd.namsic.game.enums.object_list.EquipList;
import lkd.namsic.game.enums.object_list.ItemList;

public class RandomList {

    //Handle Level - {Reinforce Level, Lv Increase}
    public final static Map<Integer, Map<Integer, Integer>> REINFORCE_LV_INCREASE = new HashMap<Integer, Map<Integer, Integer>>() {{
        put(1, new HashMap<Integer, Integer>() {{
            int[] values = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 10, 10, 20};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(2, new HashMap<Integer, Integer>() {{
            int[] values = {0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 10, 10, 20, 25};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(3, new HashMap<Integer, Integer>() {{
            int[] values = {0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 10, 15, 25, 35};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(4, new HashMap<Integer, Integer>() {{
            int[] values = {0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 10, 15, 20, 30, 40};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(5, new HashMap<Integer, Integer>() {{
            int[] values = {0, 0, 0, 0, 0, 0, 5, 5, 10, 10, 15, 20, 30, 40, 50};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(6, new HashMap<Integer, Integer>() {{
            int[] values = {0, 0, 0, 0, 0, 5, 5, 10, 10, 15, 15, 25, 35, 45, 60};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(7, new HashMap<Integer, Integer>() {{
            int[] values = {0, 0, 0, 0, 5, 5, 10, 10, 15, 20, 25, 30, 40, 55, 65};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(8, new HashMap<Integer, Integer>() {{
            int[] values = {0, 0, 0, 5, 5, 10, 10, 15, 20, 25, 35, 45, 60, 80, 100};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(9, new HashMap<Integer, Integer>() {{
            int[] values = {0, 0, 0, 10, 10, 15, 20, 30, 30, 50, 50, 70, 80, 100, 100};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(10, new HashMap<Integer, Integer>() {{
            int[] values = {0, 5, 5, 10, 10, 15, 20, 30, 40, 55, 70, 90, 100, 120, 150};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(11, new HashMap<Integer, Integer>() {{
            int[] values = {0, 10, 10, 20, 30, 50, 50, 50, 70, 100, 100, 100, 120, 150, 200};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(12, new HashMap<Integer, Integer>() {{
            int[] values = {10, 20, 20, 30, 50, 70, 80, 100, 110, 130, 150, 150, 170, 200, 250};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(13, new HashMap<Integer, Integer>() {{
            int[] values = {10, 20, 30, 50, 70, 100, 100, 120, 140, 160, 180, 200, 250, 300, 500};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
    }};

    //Handle Level - {Reinforce Level, Reinforce Success Percent}
    public final static Map<Integer, Map<Integer, Double>> REINFORCE_PERCENT = new HashMap<Integer, Map<Integer, Double>>() {{
        put(1, new HashMap<Integer, Double>() {{
            double[] values = {1, 1, 1, 1, 1, 0.95, 0.9, 0.85, 0.8, 0.75, 0.65, 0.55, 0.45, 0.3, 0.15};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(2, new HashMap<Integer, Double>() {{
            double[] values = {1, 1, 1, 1, 1, 0.95, 0.9, 0.85, 0.8, 0.75, 0.65, 0.55, 0.45, 0.3, 0.15};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(3, new HashMap<Integer, Double>() {{
            double[] values = {1, 1, 1, 1, 1, 0.95, 0.9, 0.85, 0.8, 0.75, 0.65, 0.55, 0.45, 0.3, 0.15};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(4, new HashMap<Integer, Double>() {{
            double[] values = {1, 1, 1, 1, 1, 0.95, 0.9, 0.85, 0.8, 0.75, 0.65, 0.55, 0.45, 0.3, 0.15};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(5, new HashMap<Integer, Double>() {{
            double[] values = {1, 1, 1, 1, 1, 0.95, 0.9, 0.85, 0.8, 0.75, 0.65, 0.55, 0.45, 0.3, 0.15};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(6, new HashMap<Integer, Double>() {{
            double[] values = {1, 1, 1, 0.9, 0.8, 0.7, 0.6, 0.5, 0.5, 0.5, 0.3, 0.1, 0.1, 0.05, 0.01};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(7, new HashMap<Integer, Double>() {{
            double[] values = {1, 1, 1, 0.9, 0.8, 0.7, 0.6, 0.5, 0.5, 0.5, 0.3, 0.1, 0.1, 0.05, 0.01};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(8, new HashMap<Integer, Double>() {{
            double[] values = {1, 1, 1, 0.9, 0.8, 0.7, 0.6, 0.5, 0.5, 0.5, 0.3, 0.1, 0.1, 0.05, 0.01};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(9, new HashMap<Integer, Double>() {{
            double[] values = {1, 0.9, 0.8, 0.7, 0.5, 0.5, 0.5, 0.35, 0.2, 0.1, 0.1, 0.1, 0.01, 0.005, 0.001};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(10, new HashMap<Integer, Double>() {{
            double[] values = {1, 0.9, 0.8, 0.7, 0.5, 0.5, 0.5, 0.35, 0.2, 0.1, 0.1, 0.1, 0.01, 0.005, 0.001};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(11, new HashMap<Integer, Double>() {{
            double[] values = {0.5, 0.5, 0.5, 0.4, 0.3, 0.2, 0.1, 0.03, 0.03, 0.03, 0.01, 0.01, 0.001, 0.0003, 0.0003};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(12, new HashMap<Integer, Double>() {{
            double[] values = {0.5, 0.5, 0.5, 0.4, 0.3, 0.2, 0.1, 0.02, 0.02, 0.01, 0.01, 0.001, 0.0002, 0.0002, 0.0001};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
        put(13, new HashMap<Integer, Double>() {{
            double[] values = {0.5, 0.4, 0.3, 0.2, 0.1, 0.05, 0.03, 0.02, 0.01, 0.003, 0.002, 0.001, 0.0001, 0.0001, 0.0001};
            for(int i = 0; i < 15; i++) {
                put(i + 1, values[i]);
            }
        }});
    }};

    public static final List<List<Double>> MINE_PERCENT = Arrays.asList(
            Arrays.asList(50D, 45D, 40D, 35D, 30D, 30D, 30D, 25D, 20D),
            Arrays.asList(35D, 35D, 20D, 10D, 0D, 0D, 0D, 0D, 0D),
            Arrays.asList(14D, 15D, 20D, 15D, 0D, 0D, 0D, 0D, 0D),
            Arrays.asList(1D, 4.5D, 14D, 20D, 15D, 0D, 0D, 0D, 0D),
            Arrays.asList(0D, 0.5D, 5.5D, 12D, 40D, 20D, 0D, 0D, 0D),
            Arrays.asList(0D, 0D, 0.5D, 7.6D, 10D, 30D, 25D, 0D, 0D),
            Arrays.asList(0D, 0D, 0D, 0.4D, 4.98D, 15D, 25D, 20D, 0D),
            Arrays.asList(0D, 0D, 0D, 0D, 0.019D, 4.8D, 15D, 32.5D, 30.5D),
            Arrays.asList(0D, 0D, 0D, 0D, 0.001D, 0.149D, 4.79D, 17.5D, 30.3D),
            Arrays.asList(0D, 0D, 0D, 0D, 0D, 0.001D, 0.2095D, 4.99D, 19.042D),
            Arrays.asList(0D, 0D, 0D, 0D, 0D, 0D, 0.0005D, 0.0055D, 0.1577D),
            Arrays.asList(0D, 0D, 0D, 0D, 0D, 0D, 0D, 0.0001D, 0.0003D)
    );

    public static final List<List<Double>> FISH_PERCENT = Arrays.asList(
            Arrays.asList(35D, 25D, 15D, 5D, 1D, 0D, 0D, 0D, 0D),
            Arrays.asList(55D, 50D, 35D, 25D, 15D, 10D, 9D, 7D, 4D),
            Arrays.asList(10D, 25D, 40D, 40D, 30D, 25D, 20D, 10D, 5D),
            Arrays.asList(0D, 0D, 10D, 30D, 50D, 45D, 30D, 30D, 25D),
            Arrays.asList(0D, 0D, 0D, 0D, 4D, 20D, 40D, 5D, 60D),
            Arrays.asList(0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 1D)
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
            put(ItemList.LOW_CRAFT_GUIDE.getId(), 50000);
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
            put(ItemList.LOW_CRAFT_GUIDE.getId(), 75000);
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
            put(ItemList.LOW_CRAFT_GUIDE.getId(), 100000);
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
            put(ItemList.LOW_CRAFT_GUIDE.getId(), 100000);
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
            put(ItemList.LOW_CRAFT_GUIDE.getId(), 100000);
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
            put(ItemList.LOW_CRAFT_GUIDE.getId(), 80000);
            put(ItemList.CRAFT_GUIDE.getId(), 60000);
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
            put(ItemList.LOW_CRAFT_GUIDE.getId(), 80000);
            put(ItemList.CRAFT_GUIDE.getId(), 60000);
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
            put(ItemList.CRAFT_GUIDE.getId(), 100000);
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
            put(ItemList.CRAFT_GUIDE.getId(), 100000);
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
            put(ItemList.CRAFT_GUIDE.getId(), 100000);
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
            put(ItemList.CRAFT_GUIDE.getId(), 50000);
            put(ItemList.PIECE_OF_GEM.getId(), 50000);
            put(ItemList.GEM_ABRASIVE_MATERIAL.getId(), 50000);
            put(ItemList.WEAPON_SAFENER.getId(), 50000);
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
            put(ItemList.CRAFT_GUIDE.getId(), 50000);
            put(ItemList.HIGH_CRAFT_GUIDE.getId(), 50000);
            put(ItemList.PIECE_OF_GEM.getId(), 50000);
            put(ItemList.GEM_ABRASIVE_MATERIAL.getId(), 50000);
            put(ItemList.WEAPON_SAFENER.getId(), 50000);
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
            put(ItemList.HIGH_CRAFT_GUIDE.getId(), 100000);
            put(ItemList.PIECE_OF_GEM.getId(), 50000);
            put(ItemList.GEM_ABRASIVE_MATERIAL.getId(), 50000);
            put(ItemList.WEAPON_SAFENER.getId(), 50000);
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
            put(ItemList.HIGH_CRAFT_GUIDE.getId(), 100000);
            put(ItemList.PIECE_OF_GEM.getId(), 50000);
            put(ItemList.GEM_ABRASIVE_MATERIAL.getId(), 50000);
            put(ItemList.WEAPON_SAFENER.getId(), 50000);
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
            put(ItemList.HIGH_CRAFT_GUIDE.getId(), 100000);
            put(ItemList.PIECE_OF_GEM.getId(), 50000);
            put(ItemList.GLOW_GEM_ABRASIVE_MATERIAL.getId(), 50000);
            put(ItemList.WEAPON_SAFENER.getId(), 50000);
        }});
    }};

    public static final double[][] HUNTER_TOKEN = new double[][] {
            { 0.1, 0, 0 },
            { 0.1, 0.05, 0 },
            { 0, 0.1, 0 },
            { 0, 0.15, 0 },
            { 0, 0.2, 0 },
            { 0, 0.25, 0 },
            { 0, 0.3, 0 },
            { 0, 0.25, 0.05 },
            { 0, 0.2, 0.1 },
            { 0, 0.1, 0.2 }
    };

    public static final double[][] FISH_TOKEN = new double[][] {
            {}, //Empty
            { 0.1, 0, 0 },
            { 0.05, 0.05, 0 },
            { 0.05, 0.1, 0 },
            { 0, 0.15, 0.02 },
            { 0, 0.2, 0.05 },
            { 0, 0.15, 0.1 }
    };

    public static final double[][] MINE_TOKEN = new double[][] {
            { 0.02, 0, 0},
            { 0.05, 0, 0},
            { 0.08, 0, 0},
            { 0.11, 0, 0},
            { 0.15, 0.02, 0},
            { 0.15, 0.05, 0},
            { 0.13, 0.08, 0},
            { 0.1, 0.1, 0},
            { 0.05, 0.15, 0},
            { 0.03, 0.15, 0},
            { 0, 0.15, 0.03},
            { 0, 0.12, 0.07}
    };

    public static final List<Long> jewelries = new ArrayList<Long>() {{
        for(int i = 0; i < 100; i++) {
            this.add(ItemList.QUARTZ.getId());
        }

        for (int i = 0; i < 50; i++) {
            this.add(ItemList.GOLD.getId());
        }

        for(int i = 0; i < 20; i++) {
            this.add(ItemList.WHITE_GOLD.getId());
        }

        this.add(ItemList.DIAMOND.getId());
        this.add(ItemList.ORICHALCON.getId());
        for(long itemId = ItemList.GARNET.getId(); itemId <= ItemList.TURQUOISE.getId(); itemId++) {
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
            ItemList.LOW_AMULET.getId()
    );

    public static final List<Long> middleRecipeItems = Arrays.asList(
            ItemList.GLOW_LAPIS.getId(),
            ItemList.GLOW_RED_STONE.getId(),
            ItemList.WHITE_GOLD.getId(),
            ItemList.MIDDLE_ALLOY.getId(),
            ItemList.AMULET.getId()
    );

    public static final List<Long> highRecipeItems = Arrays.asList(
            ItemList.LIQUID_STONE.getId(),
            ItemList.DIAMOND.getId(),
            ItemList.LAPIS_RED_STONE.getId(),
            ItemList.LANDIUM.getId(),
            ItemList.AITUME.getId(),
            ItemList.HIGH_ALLOY.getId(),
            ItemList.HIGH_AMULET.getId()
    );

    public static final List<Long> lowRecipeEquips = Arrays.asList(
            EquipList.WOODEN_SWORD.getId(),
            EquipList.IRON_SWORD.getId(),
            EquipList.MIX_SWORD.getId()
    );

    public static final List<Long> middleRecipeEquips = Arrays.asList(
            EquipList.HEART_BREAKER_1.getId(),
            EquipList.HEAD_HUNTER_1.getId(),
            EquipList.GHOST_SWORD_1.getId()
    );

    public static final List<Long> highRecipeEquips = Collections.emptyList();

    public static final List<Long> lowAmulets = Collections.singletonList(EquipList.HEALTH_AMULET.getId());

    public static final List<Long> middleAmulets = Collections.singletonList(EquipList.BLOOD_AMULET.getId());

    public static final List<Long> highAmulets = Collections.singletonList(EquipList.DRAGON_AMULET.getId());

}
