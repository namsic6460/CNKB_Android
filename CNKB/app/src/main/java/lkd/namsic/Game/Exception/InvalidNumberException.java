package lkd.namsic.Game.Exception;

import java.util.Map;

import lkd.namsic.Game.Base.LimitId;
import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Base.LimitLong;
import lkd.namsic.Game.Config;

public class InvalidNumberException extends RuntimeException {

    public InvalidNumberException(int value) {
        super(String.valueOf(value));
    }

    public InvalidNumberException(long value) {
        super(String.valueOf(value));
    }

}
