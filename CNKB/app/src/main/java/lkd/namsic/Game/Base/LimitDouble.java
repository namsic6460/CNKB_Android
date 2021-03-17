package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.Game.Exception.ValueRangeException;
import lombok.Getter;
import lombok.Setter;

public class LimitDouble {

    @Getter
    @Setter
    private Double min;

    @Getter
    @Setter
    private Double max;

    private double value;

    public LimitDouble(double value, @Nullable Double min, @Nullable Double max) {
        if(min != null && max != null && min > max) {
            throw new RuntimeException("minValue can't be bigger than maxValue");
        }

        this.min = min;
        this.max = max;
        this.set(value);
    }

    public double get() {
        double returnValue = value;

        if(min != null && returnValue < min) {
            returnValue = min;
        } else if(max != null && returnValue > max) {
            returnValue = max;
        }

        return returnValue;
    }

    public void set(double setValue) {
        if (min != null && setValue < min) {
            throw new ValueRangeException(this);
        } else if (max != null && setValue > max) {
            throw new ValueRangeException(this);
        }

        this.value = setValue;
    }

    @NonNull
    @Override
    public String toString() {
        return "Value: " + this.value + ", Min: " + this.min + ", Max: " + this.max;
    }

}
