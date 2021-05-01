package lkd.namsic.game.base;

import androidx.annotation.Nullable;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RangeInteger implements Serializable {

    int min;
    int max;

    public RangeInteger(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean isInRange(int value) {
        return value >= this.min && value <= this.max;
    }

    public void set(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public void add(int value) {
        this.add(value, value);
    }

    public void add(int minValue, int maxValue) {
        this.min += minValue;
        this.max += maxValue;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof RangeInteger) {
            RangeInteger o = (RangeInteger) obj;
            return this.min == o.min && this.max == o.max;
        } else {
            return false;
        }
    }
}
