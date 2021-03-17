package lkd.namsic.Game.Exception;

import lkd.namsic.Game.Base.LimitDouble;
import lkd.namsic.Game.Base.LimitInteger;

public class ValueRangeException extends RuntimeException {

    public ValueRangeException(int value, int minValue, int maxValue) {
        super("Value: " + value + ", Min: " + minValue + ", Max: " + maxValue);
    }

    public ValueRangeException(double value, double minValue, double maxValue) {
        super("Value: " + value + ", Min: " + minValue + ", Max: " + maxValue);
    }

    public ValueRangeException(LimitInteger limitInteger) {
        super(limitInteger.toString());
    }

    public ValueRangeException(LimitDouble limitDouble) {
        super(limitDouble.toString());
    }

}
