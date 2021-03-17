package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.Game.Exception.ValueRangeException;
import lombok.Getter;
import lombok.Setter;

public class LimitDouble {

    @Getter
    @Setter
    private Double minValue;

    @Getter
    @Setter
    private Double maxValue;

    private double value;

    public LimitDouble() {
        new LimitDouble(0, 0D, 0D);
    }

    public LimitDouble(double value, @Nullable Double minValue, @Nullable Double maxValue) {
        if(minValue != null && maxValue != null && minValue > maxValue) {
            throw new RuntimeException("minValue can't be bigger than maxValue");
        }

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.set(value);
    }

    public double get() {
        double returnValue = value;

        if(minValue != null && returnValue < minValue) {
            returnValue = minValue;
        } else if(maxValue != null && returnValue > maxValue) {
            returnValue = maxValue;
        }

        return returnValue;
    }

    public void set(double setValue) {
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
