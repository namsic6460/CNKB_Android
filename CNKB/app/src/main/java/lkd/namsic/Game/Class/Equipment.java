package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.EquipType;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.UnhandledEnumException;
import lkd.namsic.Game.Exception.ValueRangeException;
import lombok.Getter;
import lombok.Setter;

@Getter
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

    public Equipment(@NonNull EquipType equipType, @NonNull String name) {
        new Equipment(equipType, name, "", Config.MIN_HANDLE_LV);
    }

    public Equipment(@NonNull EquipType equipType, @NonNull String name, @NonNull String description, int handleLv) {
        new Equipment(equipType, name, description, handleLv, null, new ArrayList<Map<Long, Integer>>());
    }

    public Equipment(@NonNull EquipType equipType, @NonNull String name, @NonNull String description, int handleLv,
                     @Nullable Use use, @NonNull List<Map<Long, Integer>> recipe) {
        new Equipment(equipType, name, description, handleLv, use, recipe, Config.MIN_REINFORCE_COUNT,
                Config.MAX_REINFORCE_COUNT, Config.MIN_LV, 0, new HashMap<StatType, Integer>(),
                new HashMap<StatType, Integer>(), new HashMap<StatType, Integer>());
    }

    public Equipment(@NonNull EquipType equipType, @NonNull String name, @NonNull String description, int handleLv,
                     @Nullable Use use, @NonNull List<Map<Long, Integer>> recipe, int reinforceCount, int maxReinforceCount,
                     int limitLv, int lvDown, @NonNull Map<StatType, Integer> limitStat,
                     @NonNull Map<StatType, Integer> basicStat, @NonNull Map<StatType, Integer> reinforceStat) {
        super(name, description, handleLv, use, recipe);
        this.id.setId(Id.EQUIPMENT);

        this.equipType = equipType;
        this.reinforceCount.setMaxValue(maxReinforceCount);
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
            throw e;
        }
    }

    public void setLimitStat(@NonNull StatType statType, int stat) {
        if(statType.equals(StatType.MAXHP) || statType.equals(StatType.MAXMN)) {
            throw new UnhandledEnumException(statType);
        }

        if(stat < 0) {
            throw new ValueRangeException(stat, 0, Integer.MAX_VALUE);
        }

        if(stat == 0) {
            this.limitStat.remove(statType);
        } else {
            this.limitStat.put(statType, stat);
        }
    }

    public int getLimitStat(@NonNull StatType statType) {
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
            throw e;
        }

        return this;
    }

    public Equipment setBasicStat(@NonNull StatType statType, int stat) {
        if(statType.equals(StatType.MAXHP) || statType.equals(StatType.MAXMN)) {
            throw new UnhandledEnumException(statType);
        }

        if(stat == 0) {
            this.basicStat.remove(statType);
        } else {
            this.basicStat.put(statType, stat);
        }

        return this;
    }

    public int getBasicStat(@NonNull StatType statType) {
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
            throw e;
        }

        return this;
    }

    public Equipment setReinforceStat(@NonNull StatType statType, int stat) {
        if(statType.equals(StatType.MAXHP) || statType.equals(StatType.MAXMN)) {
            throw new UnhandledEnumException(statType);
        }

        if(stat == 0) {
            this.reinforceStat.remove(statType);
        } else {
            this.reinforceStat.put(statType, stat);
        }

        return this;
    }

    public int getReinforceStat(@NonNull StatType statType) {
        Integer value = this.reinforceStat.get(statType);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void revalidateStat() {
        for(StatType statType : StatType.values()) {
            if(statType.equals(StatType.MAXHP) || statType.equals(StatType.MAXMN)) {
                continue;
            }

            this.stat.put(statType, this.getBasicStat(statType) + this.getReinforceStat(statType));
        }
    }

}
