package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.exception.WeirdCommandException;

public enum FishWaitType {

    NONE,
    SHAKE("흔들기", "shake"),
    WAIT("기다리기", "wait"),
    PULL("당기기", "pull"),
    RESIST("버티기", "resist"),
    CATCH("잡기", "catch");

    private final List<String> texts = new ArrayList<>(2);

    FishWaitType() {}

    FishWaitType(@NonNull String...texts) {
        this.texts.addAll(Arrays.asList(texts));
    }

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
