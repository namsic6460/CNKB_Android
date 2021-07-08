package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.RangeInteger;
import lkd.namsic.game.base.RangeIntegerMap;
import lkd.namsic.game.config.Config;
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

    public static int getLvIncrease(int handleLv, int nextReinforceCount) {
        if(handleLv < Config.MIN_HANDLE_LV || handleLv > Config.MAX_HANDLE_LV) {
            throw new NumberRangeException(handleLv, Config.MIN_HANDLE_LV, Config.MAX_HANDLE_LV);
        }

        if(nextReinforceCount < Config.MIN_REINFORCE_COUNT + 1 || nextReinforceCount > Config.MAX_REINFORCE_COUNT) {
            throw new NumberRangeException(nextReinforceCount, Config.MIN_REINFORCE_COUNT, Config.MAX_REINFORCE_COUNT);
        }

        return RandomList.REINFORCE_LV_INCREASE.get(handleLv).get(nextReinforceCount);
    }

    @Setter
    @NonNull
    EquipType equipType;

    final LimitInteger reinforceCount = new LimitInteger(Config.MIN_REINFORCE_COUNT, Config.MIN_REINFORCE_COUNT, Config.MAX_REINFORCE_COUNT);
    final LimitInteger reinforceFloor = new LimitInteger(0, 0, null);
    final RangeInteger limitLv = new RangeInteger(Config.MIN_LV, Config.MAX_LV);

    @Setter
    int lvDown = 0;
    double reinforcePercent;

    final RangeIntegerMap<StatType> limitStat = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>(), StatType.class
    );
    final Map<StatType, Integer> basicStat = new HashMap<>();
    final Map<StatType, Integer> reinforceStat = new HashMap<>();

    public Equipment(@NonNull EquipType equipType, @NonNull String name, @NonNull String description) {
        super(name);
        this.description = description;

        this.id.setId(Id.EQUIPMENT);

        this.reinforcePercent = this.getReinforcePercent();
        this.description = description;
        this.equipType = equipType;
    }

    public double getReinforcePercent() {
        double basicPercent = RandomList.REINFORCE_PERCENT.get(this.handleLv.get()).get(this.reinforceCount.get() + 1);
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

    @NonNull
    @Override
    public String getName() {
        return super.getName() + " (+" + this.reinforceCount.get() + ")";
    }
}
