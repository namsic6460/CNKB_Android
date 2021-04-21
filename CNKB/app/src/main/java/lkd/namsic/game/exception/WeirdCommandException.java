package lkd.namsic.game.exception;

public class WeirdCommandException extends RuntimeException {

    public WeirdCommandException() {
        super("잘못된 명령어입니다");
    }

}
