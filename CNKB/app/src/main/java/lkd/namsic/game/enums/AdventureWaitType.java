package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.WeirdCommandException;

public enum AdventureWaitType {

    NONE,
    ONE,
    TWO,
    THREE,
    FOUR,
    WAIT;

    public static AdventureWaitType parseWaitType(@NonNull String command)
            throws NumberFormatException, NumberRangeException {
        int value = Integer.parseInt(command);

        if(value < 1 || value > 4) {
            throw new WeirdCommandException("알맞은 숫자를 입력해주세요 (범위 : 1~4)");
        }

        return AdventureWaitType.values()[value];
    }

}
