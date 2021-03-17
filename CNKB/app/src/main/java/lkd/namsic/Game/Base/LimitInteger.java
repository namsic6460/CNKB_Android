package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;

public class LimitInteger {

    @Getter
    @Setter
    private Integer min;

    @Getter
    @Setter
    private Integer max;

    private int value;

    public LimitInteger(int value, @Nullable Integer min, @Nullable Integer max) {
        if(min != null && max != null && min > max) {
            throw new RuntimeException("minValue can't be bigger than maxValue");
        }

        this.min = min;
        this.max = max;
        this.set(value);
    }

    public int get() {
        int returnValue = value;

        if(min != null && returnValue < min) {
            returnValue = min;
        } else if(max != null && returnValue > max) {
            returnValue = max;
        }

        return returnValue;
    }

    public void set(int setValue) {
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
        return "Value: " + this.value + ", Min: " + this.min + ", Max: " + this.max;
    }

}
