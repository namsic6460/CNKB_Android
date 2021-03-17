package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;

import java.util.Map;

import lkd.namsic.Game.Exception.ValueRangeException;
import lombok.Getter;

@Getter
public class RangeIntegerMap<T> {

    @NonNull
    Map<T, Integer> min;

    @NonNull
    Map<T, Integer> max;

    public RangeIntegerMap(@NonNull Map<T, Integer> min, @NonNull Map<T, Integer> max) {
        T key;
        Integer minValue, maxValue;
        for(Map.Entry<T, Integer> minEntry : min.entrySet()) {
            key = minEntry.getKey();
            minValue = minEntry.getValue();
            maxValue = max.get(key);

            if(maxValue != null) {
                if(maxValue < minValue) {
                    throw new ValueRangeException(key, minValue, maxValue);
                }
            }
        }

        this.min = min;
        this.max = max;
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

}
