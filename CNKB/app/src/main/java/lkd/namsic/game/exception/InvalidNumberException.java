package lkd.namsic.game.exception;

public class InvalidNumberException extends RuntimeException {

    public InvalidNumberException(int value) {
        super(String.valueOf(value));
    }

}
