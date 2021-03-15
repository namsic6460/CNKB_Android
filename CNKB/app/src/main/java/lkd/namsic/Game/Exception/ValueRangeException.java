package lkd.namsic.Game.Exception;

import lkd.namsic.Game.Base.LimitDouble;
import lkd.namsic.Game.Base.LimitInteger;

public class ValueRangeException extends RuntimeException {

    public ValueRangeException(LimitInteger limitInteger) {
        super(limitInteger.toString());
    }

    public ValueRangeException(LimitDouble limitDouble) {
        super(limitDouble.toString());
    }

}
