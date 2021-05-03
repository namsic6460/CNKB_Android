package lkd.namsic.game.exception;

public class InvalidNumberException extends RuntimeException {

    public InvalidNumberException(Object value) {
        super(String.valueOf(value));
    }

}
