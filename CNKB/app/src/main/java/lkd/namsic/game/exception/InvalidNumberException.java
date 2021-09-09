package lkd.namsic.game.exception;

import androidx.annotation.NonNull;

import lombok.Getter;

public class InvalidNumberException extends RuntimeException {

    @Getter
    final
    Object value;

    public InvalidNumberException(@NonNull Object value) {
        super(value.toString());
        this.value = value;
    }

}
