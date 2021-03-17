package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.Game.Exception.ValueRangeException;
import lombok.Getter;
import lombok.Setter;

public class LimitInteger {

    @Getter
    @Setter
    private Integer minValue;

    @Getter
    @Setter
    private Integer maxValue;

    private int value;

    public LimitInteger() {
        new LimitInteger(0, 0, 0);
    }

    public LimitInteger(int value, @Nullable Integer minValue, @Nullable Integer maxValue) {
        if(minValue != null && maxValue != null && minValue > maxValue) {
            throw new RuntimeException("minValue can't be bigger than maxValue");
        }

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.set(value);
    }

    public int get() {
        int returnValue = value;

        if(minValue != null && returnValue < minValue) {
            returnValue = minValue;
        } else if(maxValue != null && returnValue > maxValue) {
            returnValue = maxValue;
        }

        return returnValue;
    }

    public void set(int setValue) {
        if (minValue != null && setValue < minValue) {
            throw new ValueRangeException(this);
        } else if (maxValue != null && setValue > maxValue) {
            throw new ValueRangeException(this);
        }

        this.value = setValue;
    }

    @NonNull
    @Override
    public String toString() {
        return "Value: " + this.value + ", Min: " + this.minValue + ", Max: " + this.maxValue;
    }

}
