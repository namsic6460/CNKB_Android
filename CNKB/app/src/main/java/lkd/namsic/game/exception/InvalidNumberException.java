package lkd.namsic.game.exception;

import lombok.Getter;
import lombok.NonNull;

public class InvalidNumberException extends RuntimeException {

    @Getter
    final
    Object value;

    public InvalidNumberException(@NonNull Object value) {
        super(value.toString());
        this.value = value;
    }

}
