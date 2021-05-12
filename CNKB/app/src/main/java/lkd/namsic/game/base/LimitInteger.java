package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;

public class LimitInteger implements LimitClass<Integer> {

    @Getter
    @Setter
    private Integer min;

    @Getter
    @Setter
    private Integer max;

    private int value;

    public LimitInteger(int value, @Nullable Integer min, @Nullable Integer max) {
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
    public Integer get() {
        return this.value;
    }

    @Override
    public void set(@NonNull Integer setValue) {
        if (min != null && setValue < min) {
            throw new NumberRangeException(this);
        } else if (max != null && setValue > max) {
            throw new NumberRangeException(this);
        }

        this.value = setValue;
    }

    public void add(int addValue) {
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

        if(obj instanceof LimitInteger) {
            return this.get().equals(((LimitInteger) obj).get());
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
