package lkd.namsic.game.exception;

import lkd.namsic.game.enums.Doing;

public class DoingFilterException extends RuntimeException {

    public DoingFilterException(Doing doing) {
        super("다른 행동을 하는 도중 사용할 수 없는 명령어입니다\n현재 행동: " + doing.getDisplayName());
    }

}
