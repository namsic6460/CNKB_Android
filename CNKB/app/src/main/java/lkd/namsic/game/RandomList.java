package lkd.namsic.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static final Map<Integer, Map<Long, Integer>> EXPLORE_LIST = new HashMap<Integer, Map<Long, Integer>>() {{
        put(1, new HashMap<Long, Integer>() {{
            put(1L, 150000);
            put(2L, 150000);
            put(3L, 150000);
            put(4L, 150000);
            put(10L, 150000);
            put(11L, 150000);
            put(7L, 100000);
        }});
        put(2, new HashMap<Long, Integer>() {{
            put(1L, 140000);
            put(2L, 140000);
            put(3L, 140000);
            put(4L, 140000);
            put(10L, 140000);
            put(11L, 140000);
            put(7L, 100000);
            put(118L, 40000);
            put(115L, 20000);
        }});
        put(3, new HashMap<Long, Integer>() {{
            put(1L, 100000);
            put(2L, 100000);
            put(3L, 100000);
            put(4L, 100000);
            put(10L, 100000);
            put(11L, 100000);
            put(7L, 100000);
            put(118L, 50000);
            put(115L, 50000);
            put(14L, 100000);
            put(17L, 100000);
        }});
        put(4, new HashMap<Long, Integer>() {{
            put(1L, 70000);
            put(2L, 70000);
            put(3L, 70000);
            put(4L, 70000);
            put(10L, 70000);
            put(11L, 70000);
            put(7L, 80000);
            put(118L, 100000);
            put(115L, 100000);
            put(14L, 150000);
            put(17L, 150000);
        }});
        put(5, new HashMap<Long, Integer>() {{
            put(1L, 50000);
            put(2L, 50000);
            put(3L, 50000);
            put(4L, 50000);
            put(10L, 50000);
            put(11L, 50000);
            put(7L, 50000);
            put(118L, 100000);
            put(115L, 100000);
            put(14L, 150000);
            put(17L, 150000);
            put(5L, 100000);
            put(97L, 50000);
        }});
        put(6, new HashMap<Long, Integer>() {{
            put(1L, 50000);
            put(2L, 50000);
            put(3L, 50000);
            put(4L, 50000);
            put(10L, 50000);
            put(11L, 50000);
            put(7L, 50000);
            put(118L, 100000);
            put(115L, 50000);
            put(116L, 50000);
            put(14L, 100000);
            put(17L, 100000);
            put(5L, 100000);
            put(97L, 50000);
            put(112L, 50000);
            put(98L, 50000);
        }});
        put(7, new HashMap<Long, Integer>() {{
            put(1L, 40000);
            put(2L, 40000);
            put(3L, 40000);
            put(4L, 40000);
            put(10L, 40000);
            put(11L, 40000);
            put(7L, 40000);
            put(118L, 100000);
            put(116L, 100000);
            put(14L, 80000);
            put(17L, 80000);
            put(15L, 30000);
            put(18L, 30000);
            put(5L, 100000);
            put(97L, 75000);
            put(112L, 50000);
            put(98L, 75000);
        }});
        put(8, new HashMap<Long, Integer>() {{
            put(1L, 30000);
            put(2L, 30000);
            put(3L, 30000);
            put(4L, 30000);
            put(10L, 30000);
            put(11L, 30000);
            put(7L, 30000);
            put(118L, 70000);
            put(116L, 100000);
            put(15L, 100000);
            put(18L, 100000);
            put(5L, 100000);
            put(97L, 100000);
            put(112L, 120000);
            put(98L, 100000);
        }});
        put(9, new HashMap<Long, Integer>() {{
            put(1L, 20000);
            put(2L, 20000);
            put(3L, 20000);
            put(4L, 20000);
            put(10L, 20000);
            put(11L, 20000);
            put(7L, 20000);
            put(118L, 50000);
            put(116L, 100000);
            put(15L, 100000);
            put(18L, 100000);
            put(5L, 60000);
            put(97L, 100000);
            put(112L, 150000);
            put(119L, 50000);
            put(122L, 50000);
            put(98L, 100000);
        }});
        put(10, new HashMap<Long, Integer>() {{
            put(1L, 30000);
            put(2L, 30000);
            put(3L, 30000);
            put(4L, 30000);
            put(10L, 30000);
            put(11L, 30000);
            put(7L, 30000);
            put(118L, 40000);
            put(116L, 100000);
            put(15L, 100000);
            put(18L, 100000);
            put(5L, 50000);
            put(97L, 100000);
            put(112L, 100000);
            put(119L, 50000);
            put(122L, 50000);
            put(98L, 100000);
        }});
        put(11, new HashMap<Long, Integer>() {{
            put(1L, 10000);
            put(2L, 10000);
            put(3L, 10000);
            put(4L, 10000);
            put(10L, 10000);
            put(11L, 10000);
            put(7L, 10000);
            put(118L, 30000);
            put(116L, 50000);
            put(117L, 20000);
            put(15L, 100000);
            put(18L, 100000);
            put(5L, 50000);
            put(6L, 10000);
            put(97L, 100000);
            put(112L, 30000);
            put(113L, 100000);
            put(119L, 100000);
            put(122L, 100000);
            put(98L, 80000);
            put(99L, 60000);
        }});
        put(12, new HashMap<Long, Integer>() {{
            put(118L, 10000);
            put(117L, 50000);
            put(15L, 50000);
            put(18L, 50000);
            put(16L, 100000);
            put(19L, 100000);
            put(6L, 100000);
            put(97L, 100000);
            put(113L, 100000);
            put(119L, 100000);
            put(122L, 100000);
            put(98L, 80000);
            put(99L, 60000);
        }});
        put(13, new HashMap<Long, Integer>() {{
            put(117L, 100000);
            put(15L, 50000);
            put(18L, 50000);
            put(16L, 100000);
            put(19L, 100000);
            put(6L, 100000);
            put(97L, 100000);
            put(113L, 100000);
            put(119L, 100000);
            put(122L, 100000);
            put(99L, 100000);
        }});
        put(14, new HashMap<Long, Integer>() {{
            put(117L, 100000);
            put(15L, 25000);
            put(18L, 25000);
            put(16L, 100000);
            put(19L, 100000);
            put(6L, 100000);
            put(97L, 100000);
            put(113L, 100000);
            put(119L, 100000);
            put(122L, 100000);
            put(123L, 50000);
            put(99L, 100000);
        }});
        put(15, new HashMap<Long, Integer>() {{
            put(117L, 100000);
            put(16L, 100000);
            put(19L, 100000);
            put(6L, 100000);
            put(97L, 100000);
            put(113L, 100000);
            put(119L, 100000);
            put(123L, 100000);
            put(99L, 100000);
            put(125L, 50000);
            put(126L, 50000);
        }});
        put(16, new HashMap<Long, Integer>() {{
            put(117L, 100000);
            put(16L, 100000);
            put(19L, 100000);
            put(6L, 100000);
            put(97L, 50000);
            put(113L, 100000);
            put(114L, 50000);
            put(119L, 100000);
            put(120L, 50000);
            put(123L, 50000);
            put(99L, 50000);
            put(125L, 50000);
            put(126L, 50000);
            put(128L, 50000);
        }});
        put(17, new HashMap<Long, Integer>() {{
            put(117L, 100000);
            put(16L, 100000);
            put(19L, 100000);
            put(6L, 100000);
            put(97L, 50000);
            put(113L, 50000);
            put(114L, 50000);
            put(120L, 100000);
            put(123L, 50000);
            put(124L, 50000);
            put(99L, 50000);
            put(100L, 50000);
            put(125L, 50000);
            put(126L, 50000);
            put(128L, 50000);
        }});
        put(18, new HashMap<Long, Integer>() {{
            put(117L, 100000);
            put(16L, 100000);
            put(19L, 100000);
            put(6L, 100000);
            put(97L, 50000);
            put(114L, 100000);
            put(120L, 100000);
            put(124L, 100000);
            put(100L, 100000);
            put(125L, 50000);
            put(126L, 50000);
            put(128L, 50000);
        }});
        put(19, new HashMap<Long, Integer>() {{
            put(16L, 100000);
            put(19L, 100000);
            put(6L, 100000);
            put(97L, 50000);
            put(114L, 100000);
            put(120L, 100000);
            put(121L, 100000);
            put(124L, 100000);
            put(100L, 100000);
            put(125L, 50000);
            put(126L, 50000);
            put(128L, 50000);
        }});
        put(20, new HashMap<Long, Integer>() {{
            put(16L, 100000);
            put(19L, 100000);
            put(6L, 100000);
            put(97L, 50000);
            put(114L, 100000);
            put(120L, 100000);
            put(121L, 100000);
            put(124L, 100000);
            put(100L, 100000);
            put(125L, 50000);
            put(127L, 50000);
            put(128L, 50000);
        }});
    }};

}
