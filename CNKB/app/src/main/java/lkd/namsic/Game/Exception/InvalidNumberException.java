package lkd.namsic.Game.Exception;

public class InvalidNumberException extends RuntimeException {

    public InvalidNumberException(int value) {
        super(String.valueOf(value));
    }

}
