package lkd.namsic.game.exception;

public class WeirdCommandException extends RuntimeException {

    public WeirdCommandException() {
        super("잘못된 명령어입니다. 다시 한번 확인해주세요");
    }

    public WeirdCommandException(String s) { super(s); }

}
