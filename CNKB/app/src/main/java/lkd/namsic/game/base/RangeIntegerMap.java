package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Map;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;

public class RangeIntegerMap<T> implements Serializable {

    final private String className;

    @Getter
    private Map<T, Integer> min;

    @Getter
    private Map<T, Integer> max;

    public RangeIntegerMap(@NonNull Map<T, Integer> min, @NonNull Map<T, Integer> max, Class<T> c) {
        this.set(min, max);
        this.className = c.getName();
    }

    public void set(Map<T, Integer> min, Map<T, Integer> max) {
        if(Config.compareMap(min, max, false, true, null)) {
            this.checkKeys(min, max);
        } else {
            throw new NumberRangeException(min, max);
        }

        this.min = min;
        this.max = max;
    }

    public void addMin(T t, int value) {
        Integer get = this.min.get(t);
        this.min.put(t, get == null ? value : get + value);
    }

    public void addMax(T t, int value) {
        Integer get = this.max.get(t);
        this.max.put(t, get == null ? value : get + value);
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
        return Config.compareMap(this.min, map, false, false, 0) &&
                Config.compareMap(this.max, map, true, true, null);
    }

    @NonNull
    @Override
    public String toString() {
        return this.getMin().toString() + ", " + this.getMax().toString();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof RangeIntegerMap) {
            RangeIntegerMap<?> map = (RangeIntegerMap<?>) obj;

            if(this.className.equals(map.className)) {
                return this.min.equals(map.min) && this.max.equals(map.max);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (min.hashCode() + "" + max.hashCode()).hashCode();
    }

}
