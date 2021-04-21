package lkd.namsic.game.exception;

import java.util.Map;

import lkd.namsic.game.base.LimitClass;
import lkd.namsic.game.Config;

public class NumberRangeException extends RuntimeException {

    public NumberRangeException(int value, int minValue) {
        throw new NumberRangeException(value, minValue, Integer.MAX_VALUE);
    }

    public NumberRangeException(Integer value, Integer minValue, int maxValue) {
        throw new NumberRangeException(value.intValue(), minValue.intValue(), maxValue);
    }

    public NumberRangeException(int value, int minValue, int maxValue) {
        super("Value: " + value + ", Min: " + minValue + ", Max: " + maxValue);
    }

    public NumberRangeException(double value, double minValue, double maxValue) {
        super("Value: " + value + "D, Min: " + minValue + "D, Max: " + maxValue + "D");
    }

    public NumberRangeException(long value, long minValue) {
        throw new NumberRangeException(value, minValue, Long.MAX_VALUE);
    }

    public NumberRangeException(long value, long minValue, long maxValue) {
        super("Value: " + value + "L, Min: " + minValue + "L, Max: " + maxValue + "L");
    }

    public NumberRangeException(LimitClass<?> limitClass) {
        super(limitClass.toString());
    }

    public NumberRangeException(Object key, Integer minValue, Integer maxValue) {
        super(key.toString() + ": {Min: " + minValue + ", Max: " + maxValue + "}");
    }

    public NumberRangeException(Map<?, ?> map1, Map<?, ?> map2) {
        super(Config.mapToString(map1) + ", " + Config.mapToString(map2));
    }

}
