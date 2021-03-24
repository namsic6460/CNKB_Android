package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.EquipType;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.MapSetterException;
import lkd.namsic.Game.Exception.NumberRangeException;
import lkd.namsic.Game.Exception.UnhandledEnumException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Equipment extends Item {

    @Setter
    @NonNull
    EquipType equipType;

    LimitInteger reinforceCount = new LimitInteger(Config.MIN_REINFORCE_COUNT, Config.MIN_REINFORCE_COUNT, Config.MAX_REINFORCE_COUNT);
    LimitInteger limitLv = new LimitInteger(Config.MIN_LV, Config.MIN_LV, Config.MAX_LV);

    @Setter
    int lvDown = 0;

    Map<StatType, Integer> limitStat;
    Map<StatType, Integer> basicStat;
    Map<StatType, Integer> reinforceStat;
    Map<StatType, Integer> stat; //basic + reinforce

    public Equipment(@NonNull EquipType equipType, @NonNull String name, @NonNull String description, int handleLv,
                     @Nullable Use use, @NonNull Set<Map<Long, Integer>> recipe,
                     int reinforceCount, int maxReinforceCount, int limitLv, int lvDown,
                     @NonNull Map<StatType, Integer> limitStat,
                     @NonNull Map<StatType, Integer> basicStat,
                     @NonNull Map<StatType, Integer> reinforceStat) {
        super(name, description, handleLv, use, recipe);
        this.id.setId(Id.EQUIPMENT);

        this.equipType = equipType;
        this.reinforceCount.setMax(maxReinforceCount);
        this.reinforceCount.set(reinforceCount);
        this.limitLv.set(limitLv);
        this.lvDown = lvDown;
        this.setLimitStat(limitStat);
        this.setBasicStat(basicStat).setReinforceStat(reinforceStat).revalidateStat();
    }

    public int getTotalLimitLv() {
        int lvDown = this.limitLv.get() - this.lvDown;

        if(lvDown < Config.MIN_LV) {
            lvDown = Config.MIN_LV;
        } else if(lvDown > Config.MAX_LV) {
            lvDown = Config.MAX_LV;
        }

        return lvDown;
    }

    public void setLimitStat(@NonNull Map<StatType, Integer> limitStat) {
        Map<StatType, Integer> copy = new HashMap<>(this.limitStat);

        try {
            for(Map.Entry<StatType, Integer> entry : limitStat.entrySet()) {
                this.setLimitStat(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            this.limitStat = copy;
            throw new MapSetterException(copy, limitStat, e);
        }
    }

    public void setLimitStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat < 0) {
            throw new NumberRangeException(stat, 0);
        }

        if(stat == 0) {
            this.limitStat.remove(statType);
        } else {
            this.limitStat.put(statType, stat);
        }
    }

    public int getLimitStat(@NonNull StatType statType) {
        Config.checkStatType(statType);
        Integer value = this.limitStat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public Equipment setBasicStat(@NonNull Map<StatType, Integer> basicStat) {
        Map<StatType, Integer> copy = new HashMap<>(this.basicStat);

        try {
            for(Map.Entry<StatType, Integer> entry : basicStat.entrySet()) {
                this.setBasicStat(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            this.basicStat = copy;
            throw new MapSetterException(copy, basicStat, e);
        }

        return this;
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

    public Equipment setReinforceStat(@NonNull Map<StatType, Integer> reinforceStat) {
        Map<StatType, Integer> copy = new HashMap<>(this.reinforceStat);

        try {
            for(Map.Entry<StatType, Integer> entry : reinforceStat.entrySet()) {
                this.setReinforceStat(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            this.basicStat = copy;
            throw new MapSetterException(copy, reinforceStat, e);
        }

        return this;
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

    public void revalidateStat() {
        for(StatType statType : StatType.values()) {
            try {
                Config.checkStatType(statType);
            } catch (UnhandledEnumException e) {
                continue;
            }

            this.stat.put(statType, this.getBasicStat(statType) + this.getReinforceStat(statType));
        }
    }

}
