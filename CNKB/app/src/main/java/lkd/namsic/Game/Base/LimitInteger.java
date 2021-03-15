package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.Game.Exception.ValueRangeException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LimitInteger {

    private Integer minValue;
    private boolean throwMin;
    private Integer maxValue;
    private boolean throwMax;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Integer value;

    public LimitInteger() {
        new LimitInteger(null);
    }

    public LimitInteger(Integer value) {
        new LimitInteger(value, null, null);
    }

    public LimitInteger(Integer value, Integer minValue, Integer maxValue) {
        new LimitInteger(value, minValue, false, maxValue, false);
    }

    public LimitInteger(Integer value, Integer minValue, boolean throwMin, Integer maxValue, boolean throwMax) {
        if(minValue != null && maxValue != null && minValue > maxValue) {
            throw new RuntimeException("minValue can't be bigger than maxValue");
        } else if(minValue == null && throwMin) {
            throw new RuntimeException("throwMin can't be true if minValue is null");
        } else if(maxValue == null && throwMax) {
            throw new RuntimeException("throwMax can't be true if maxValue is null");
        }

        this.value = value;
        this.minValue = minValue;
        this.throwMin = throwMin;
        this.maxValue = maxValue;
        this.throwMax = throwMax;
    }

    public Integer get() {
        if(value == null) {
            return value;
        }

        int returnValue = value;

        if(minValue != null && returnValue < minValue) {
            returnValue = minValue;
        } else if(maxValue != null && returnValue > maxValue) {
            returnValue = maxValue;
        }

        return returnValue;
    }

    public void set(@Nullable Integer setValue) {
        if(setValue != null) {
            if (minValue != null && setValue < minValue && throwMin) {
                throw new ValueRangeException(this);
            } else if (maxValue != null && setValue > maxValue && throwMax) {
                throw new ValueRangeException(this);
            }
        }

        this.value = setValue;
    }

    @NonNull
    @Override
    public String toString() {
        return "Value: " + this.value + ", Min: " + this.minValue + "(" + this.throwMin +
                "), Max: " + this.maxValue + "(" + this.throwMax + ")";
    }

}
