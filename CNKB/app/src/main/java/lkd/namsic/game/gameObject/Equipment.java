package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.RangeInteger;
import lkd.namsic.game.base.RangeIntegerMap;
import lkd.namsic.game.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Equipment extends Item {

    //Handle Level - {Reinforce Level, Lv Increase}
    private final static Map<Integer, Map<Integer, Integer>> LV_INCREASE = new HashMap<Integer, Map<Integer, Integer>>() {{
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
    private final static Map<Integer, Map<Integer, Double>> PERCENT = new HashMap<Integer, Map<Integer, Double>>() {{
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

    public static int getLvIncrease(int handleLv, int nextReinforceCount) {
        if(handleLv < Config.MIN_HANDLE_LV || handleLv > Config.MAX_HANDLE_LV) {
            throw new NumberRangeException(handleLv, Config.MIN_HANDLE_LV, Config.MAX_HANDLE_LV);
        }

        if(nextReinforceCount < Config.MIN_REINFORCE_COUNT + 1 || nextReinforceCount > Config.MAX_REINFORCE_COUNT) {
            throw new NumberRangeException(nextReinforceCount, Config.MIN_REINFORCE_COUNT, Config.MAX_REINFORCE_COUNT);
        }

        return LV_INCREASE.get(handleLv).get(nextReinforceCount);
    }

    @Setter
    @NonNull
    EquipType equipType;

    final LimitInteger reinforceCount = new LimitInteger(Config.MIN_REINFORCE_COUNT, Config.MIN_REINFORCE_COUNT, Config.MAX_REINFORCE_COUNT);
    final LimitInteger reinforceFloor = new LimitInteger(0, 0, Integer.MAX_VALUE);
    final RangeInteger limitLv = new RangeInteger(Config.MIN_LV, Config.MAX_LV);

    @Setter
    int lvDown = 0;
    double reinforcePercent;

    final RangeIntegerMap<StatType> limitStat = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>()
    );
    final Map<StatType, Integer> basicStat = new HashMap<>();
    final Map<StatType, Integer> reinforceStat = new HashMap<>();

    public Equipment(@NonNull EquipType equipType, @NonNull String name, @NonNull String description) {
        super(name, description);
        this.id.setId(Id.EQUIPMENT);
        this.reinforcePercent = this.getReinforcePercent();

        this.equipType = equipType;
    }

    public double getReinforcePercent() {
        double basicPercent = PERCENT.get(this.handleLv.get()).get(this.reinforceCount.get() + 1);
        double value = this.reinforcePercent + basicPercent * (Config.REINFORCE_FLOOR_MULTIPLE * this.getReinforceFloor().get());
        return Math.min(Math.floor(value * 10000) / 10000, 1);
    }

    public RangeInteger getTotalLimitLv() {
        return new RangeInteger(this.limitLv.getMin() - this.lvDown, this.limitLv.getMax() - this.lvDown);
    }

    public Equipment setBasicStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat == 0) {
            this.basicStat.remove(statType);
        } else {
            this.basicStat.put(statType, stat);
        }

        return this;
    }

    public int getBasicStat(@NonNull StatType statType) {
        Config.checkStatType(statType);
        return this.basicStat.getOrDefault(statType, 0);
    }

    public Equipment addBasicStat(@NonNull StatType statType, int stat) {
        return this.setBasicStat(statType, this.getBasicStat(statType) + stat);
    }

    public Equipment setReinforceStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat == 0) {
            this.reinforceStat.remove(statType);
        } else {
            this.reinforceStat.put(statType, stat);
        }

        return this;
    }

    public int getReinforceStat(@NonNull StatType statType) {
        Config.checkStatType(statType);
        return this.reinforceStat.getOrDefault(statType, 0);
    }

    public Equipment addReinforceStat(@NonNull StatType statType, int stat) {
        return this.setReinforceStat(statType, this.getReinforceStat(statType) + stat);
    }

    public int getStat(@NonNull StatType statType) {
        Config.checkStatType(statType);
        return this.getBasicStat(statType) + this.getReinforceStat(statType);
    }

    public void successReinforce(@NonNull Map<StatType, Integer> increaseReinforceStat,
                                 @NonNull Map<StatType, Integer> increaseMinLimitStat,
                                 @NonNull Map<StatType, Integer> increaseMaxLimitStat) {
        this.reinforceFloor.set(0);
        this.reinforceCount.add(1);
        this.limitLv.add(getLvIncrease(this.handleLv.get(), this.reinforceCount.get()));

        for(Map.Entry<StatType, Integer> entry : increaseReinforceStat.entrySet()) {
            this.addReinforceStat(entry.getKey(), entry.getValue());
        }

        for(Map.Entry<StatType, Integer> entry : increaseMinLimitStat.entrySet()) {
            this.getLimitStat().addMin(entry.getKey(), entry.getValue());
        }

        for(Map.Entry<StatType, Integer> entry : increaseMaxLimitStat.entrySet()) {
            this.getLimitStat().addMax(entry.getKey(), entry.getValue());
        }

        this.reinforcePercent = this.getReinforcePercent();
    }

    public void failReinforce() {
        this.reinforceFloor.add(1);
        this.reinforcePercent = this.getReinforcePercent();
    }

}
