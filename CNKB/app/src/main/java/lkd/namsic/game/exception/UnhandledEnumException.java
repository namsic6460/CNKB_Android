package lkd.namsic.game.exception;

public class UnhandledEnumException extends RuntimeException {

    public UnhandledEnumException(Enum<?> e) {
        super(e.toString());
    }

}
