package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.exception.WeirdCommandException;

public enum FightWaitType {

    NONE,
    ATTACK("공격", "attack", "a"),
    DEFENCE("방어", "defence", "d"),
    WAIT("대기", "wait", "w"),
    ITEM("아이템", "item", "i"),
    EQUIP("장비", "equip", "e"),
    RUN("도주", "도망", "run", "r");

    private final List<String> texts = new ArrayList<>(3);

    FightWaitType() {}

    FightWaitType(@NonNull String...texts) {
        this.texts.addAll(Arrays.asList(texts));
    }

    public static FightWaitType parseWaitType(@NonNull String command) throws WeirdCommandException {
        if(ATTACK.texts.contains(command)) {
            return ATTACK;
        } else if(DEFENCE.texts.contains(command)) {
            return DEFENCE;
        } else if(WAIT.texts.contains(command)) {
            return WAIT;
        } else if(ITEM.texts.contains(command)) {
            return ITEM;
        } else if(EQUIP.texts.contains(command)) {
            return EQUIP;
        } else if(RUN.texts.contains(command)) {
            return RUN;
        }

        throw new WeirdCommandException();
    }

}
