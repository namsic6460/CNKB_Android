package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Base.RangeInteger;
import lkd.namsic.Game.Base.RangeIntegerMap;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.EquipType;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Equipment extends Item {

    //Handle Level - {Reinforce Level, Lv Increase}
    private final static Map<Integer, Map<Integer, Integer>> lvIncrease = new HashMap<Integer, Map<Integer, Integer>>() {{
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

    @SuppressWarnings("ConstantConditions")
    public static int getLvIncrease(int handleLv, int nextReinforceCount) {
        if(handleLv < Config.MIN_HANDLE_LV || handleLv > Config.MAX_HANDLE_LV) {
            throw new NumberRangeException(handleLv, Config.MIN_HANDLE_LV, Config.MAX_HANDLE_LV);
        }

        if(nextReinforceCount < Config.MIN_REINFORCE_COUNT + 1 || nextReinforceCount > Config.MAX_REINFORCE_COUNT) {
            throw new NumberRangeException(nextReinforceCount, Config.MIN_REINFORCE_COUNT, Config.MAX_REINFORCE_COUNT);
        }

        return lvIncrease.get(handleLv).get(nextReinforceCount);
    }

    @Setter
    @NonNull
    EquipType equipType;

    LimitInteger reinforceCount = new LimitInteger(Config.MIN_REINFORCE_COUNT, Config.MIN_REINFORCE_COUNT, Config.MAX_REINFORCE_COUNT);
    RangeInteger limitLv = new RangeInteger(Config.MIN_LV, Config.MAX_LV);

    @Setter
    int lvDown = 0;

    RangeIntegerMap<StatType> limitStat = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>()
    );
    Map<StatType, Integer> basicStat = new HashMap<>();
    Map<StatType, Integer> reinforceStat = new HashMap<>();

    public Equipment(@NonNull EquipType equipType, @NonNull String name, @NonNull String description) {
        super(name, description);
        this.id.setId(Id.EQUIPMENT);

        this.equipType = equipType;
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
        Integer value = this.basicStat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
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
        Integer value = this.reinforceStat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public Equipment addReinforceStat(@NonNull StatType statType, int stat) {
        return this.setReinforceStat(statType, this.getReinforceStat(statType) + stat);
    }

    public int getStat(@NonNull StatType statType) {
        Config.checkStatType(statType);
        return this.getBasicStat(statType) + this.getReinforceStat(statType);
    }

}
