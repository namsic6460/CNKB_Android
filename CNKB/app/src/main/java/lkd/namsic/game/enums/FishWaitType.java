package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.exception.WeirdCommandException;

public enum FishWaitType {

    NONE,
    SHAKE("흔들기", "shake", "s"),
    WAIT("기다리기", "wait", "w"),
    PULL("당기기", "pull", "p"),
    RESIST("버티기", "resist", "r"),
    CATCH("잡기", "catch", "c");

    private final List<String> texts;

    FishWaitType() {
        texts = null;
    }

    FishWaitType(@NonNull String...texts) {
        this.texts = Arrays.asList(texts);
    }

    @NonNull
    public static FishWaitType parseWaitType(@NonNull String command) throws WeirdCommandException {
        if(SHAKE.texts.contains(command)) {
            return SHAKE;
        } else if(WAIT.texts.contains(command)) {
            return WAIT;
        } else if(PULL.texts.contains(command)) {
            return PULL;
        } else if(RESIST.texts.contains(command)) {
            return RESIST;
        } else if(CATCH.texts.contains(command)) {
            return CATCH;
        }

        throw new WeirdCommandException();
    }

}
