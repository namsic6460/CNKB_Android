package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.game.exception.NumberRangeException;
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
        if(min == null && max == null) {
            throw new RuntimeException("both minValue and maxValue can't be null");
        }

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
            throw new NumberRangeException(setValue, this);
        } else if (max != null && setValue > max) {
            throw new NumberRangeException(setValue, this);
        }

        this.value = setValue;
    }

    public void add(long addValue) {
        this.set(this.get() + addValue);
    }

    @NonNull
    @Override
    public String toString() {
        return "Value: " + this.value + "L, Min: " + this.min + "L, Max: " + this.max + "L";
    }

}
