package lkd.namsic.game.exception;

import androidx.annotation.NonNull;

public class WeirdCommandException extends RuntimeException {

    public WeirdCommandException() {
        super("잘못된 명령어입니다. 다시 한번 확인해주세요");
    }

    public WeirdCommandException(@NonNull String s) { super(s); }

}
