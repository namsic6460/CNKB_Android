package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import lkd.namsic.game.exception.UnhandledEnumException;
import lombok.Getter;

public enum Id {

    ACHIEVE,
    BOSS("b"),
    CHAT,
    EQUIPMENT,
    ITEM,
    MONSTER("m"),
    NPC("n"),
    PLAYER("p"),
    QUEST,
    RESEARCH,
    SKILL;

    @Getter
    private String value = null;

    Id() {}

    Id(String value) {
        this.value = value;
    }

    public static void checkEntityId(@NonNull Id id) throws UnhandledEnumException {
        if(!(id.equals(Id.BOSS) || id.equals(Id.MONSTER) || id.equals(Id.NPC) || id.equals(Id.PLAYER))) {
            throw new UnhandledEnumException(id);
        }
    }

}
