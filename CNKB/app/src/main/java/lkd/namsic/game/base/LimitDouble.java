package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;

public class LimitDouble implements LimitClass<Double> {

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

    @NonNull
    @Override
    public Double get() {
        return this.value;
    }

    @Override
    public void set(@NonNull Double setValue) {
        if (min != null && setValue < min) {
            throw new NumberRangeException(this);
        } else if (max != null && setValue > max) {
            throw new NumberRangeException(this);
        }

        this.value = setValue;
    }

    public void add(double addValue) {
        this.set(this.get() + addValue);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null) {
            return false;
        }

        if(obj instanceof LimitDouble) {
            return this.get().equals(((LimitDouble) obj).get());
        } else {
            return false;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Value: " + this.value + ", Min: " + this.min + ", Max: " + this.max;
    }

}
