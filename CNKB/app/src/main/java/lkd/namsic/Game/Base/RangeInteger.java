package lkd.namsic.Game.Base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RangeInteger {

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

}
