package lkd.namsic.game.exception;

public class DoingFilterException extends RuntimeException {

    public DoingFilterException() {
        super("다른 무언가를 하는 도중 사용할 수 없는 명령어입니다");
    }

}
