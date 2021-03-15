package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.Game.Exception.ValueRangeException;

public class LimitDouble {

    private Double minValue;
    private boolean throwMin;
    private Double maxValue;
    private boolean throwMax;

    private Double value;

    public LimitDouble() {
        new LimitDouble(null);
    }

    public LimitDouble(Double value) {
        new LimitDouble(value, null, null);
    }

    public LimitDouble(Double value, Double minValue, Double maxValue) {
        new LimitDouble(value, minValue, false, maxValue, false);
    }

    public LimitDouble(Double value, Double minValue, boolean throwMin, Double maxValue, boolean throwMax) {
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

    public Double get() {
        if(value == null) {
            return value;
        }

        double returnValue = value;

        if(minValue != null && returnValue < minValue) {
            returnValue = minValue;
        } else if(maxValue != null && returnValue > maxValue) {
            returnValue = maxValue;
        }

        return returnValue;
    }

    public void set(@Nullable Double setValue) {
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
