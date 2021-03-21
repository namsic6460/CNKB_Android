package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;

public class LimitLong implements LimitClass<Long> {

    @Getter
    @Setter
    private Long min;

    @Getter
    @Setter
    private Long max;

    private long value;

    public LimitLong(long value, @Nullable Long min, @Nullable Long max) {
        if(min != null && max != null && min > max) {
            throw new RuntimeException("minValue can't be bigger than maxValue");
        }

        this.min = min;
        this.max = max;
        this.set(value);
    }

    @NonNull
    @Override
    public Long get() {
        return this.value;
    }

    @Override
    public void set(@NonNull Long setValue) {
        if (min != null && setValue < min) {
            throw new NumberRangeException(this);
        } else if (max != null && setValue > max) {
            throw new NumberRangeException(this);
        }

        this.value = setValue;
    }

    @NonNull
    @Override
    public String toString() {
        return "Value: " + this.value + "L, Min: " + this.min + "L, Max: " + this.max + "L";
    }

}
