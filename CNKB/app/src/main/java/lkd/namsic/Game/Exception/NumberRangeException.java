package lkd.namsic.Game.Exception;

import java.util.Map;

import lkd.namsic.Game.Base.LimitDouble;
import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Config;

public class NumberRangeException extends RuntimeException {

    public NumberRangeException(int value, int minValue) {
        super("Value: " + value + ", Min: " + minValue);
    }

    public NumberRangeException(int value, int minValue, int maxValue) {
        super("Value: " + value + ", Min: " + minValue + ", Max: " + maxValue);
    }

    public NumberRangeException(double value, double minValue, double maxValue) {
        super("Value: " + value + ", Min: " + minValue + ", Max: " + maxValue);
    }

    public NumberRangeException(LimitInteger limitInteger) {
        super(limitInteger.toString());
    }

    public NumberRangeException(LimitDouble limitDouble) {
        super(limitDouble.toString());
    }

    public NumberRangeException(Object key, Integer minValue, Integer maxValue) {
        super(key.toString() + ": {Min: " + minValue + ", Max: " + maxValue + "}");
    }

    public NumberRangeException(Map<?, ?> map1, Map<?, ?> map2) {
        super(Config.mapsToString(map1, map2));
    }

}
