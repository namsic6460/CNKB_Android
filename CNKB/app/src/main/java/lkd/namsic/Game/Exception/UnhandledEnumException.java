package lkd.namsic.Game.Exception;

public class UnhandledEnumException extends RuntimeException {

    public UnhandledEnumException(Enum<?> e) {
        super(e.toString());
    }

}
