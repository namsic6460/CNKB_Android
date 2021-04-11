package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Map;

import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;

@Getter
public class RangeIntegerMap<T> implements Serializable {

    private Map<T, Integer> min;
    private Map<T, Integer> max;

    public RangeIntegerMap(@NonNull Map<T, Integer> min, @NonNull Map<T, Integer> max) {
        this.set(min, max);
    }

    public void set(Map<T, Integer> min, Map<T, Integer> max) {
        if(Config.compareMap(min, max, false)) {
            this.checkKeys(min, max);
        } else {
            throw new NumberRangeException(min, max);
        }

        this.min = min;
        this.max = max;
    }

    private void checkKeys(@NonNull Map<T, Integer> min, @NonNull Map<T, Integer> max) {
        for(T t : min.keySet()) {
            if(t instanceof StatType) {
                Config.checkStatType((StatType) t);
            }
        }

        for(T t : max.keySet()) {
            if(t instanceof StatType) {
                Config.checkStatType((StatType) t);
            }
        }
    }

    public boolean isInRange(@NonNull T key, int value) {
        Integer minValue = this.min.get(key);
        Integer maxValue = this.max.get(key);

        if(minValue != null && maxValue != null) {
            return minValue <= value && maxValue >= value;
        } else if(minValue == null && maxValue == null) {
            return true;
        } else if(minValue == null) {
            return value <= maxValue;
        } else {
            return value >= minValue;
        }
    }

    public boolean isInRange(@NonNull Map<T, Integer> map) {
        for(Map.Entry<T, Integer> entry : map.entrySet()) {
            if(!isInRange(entry.getKey(), entry.getValue())) {
                return false;
            }
        }

        return true;
    }

}
