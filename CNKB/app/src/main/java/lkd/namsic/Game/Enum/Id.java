package lkd.namsic.Game.Enum;

import lkd.namsic.Game.Exception.UnhandledEnumException;
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
    public String value = null;

    Id() {}

    Id(String value) {
        this.value = value;
    }

    public static void checkEntityId(Id id) throws UnhandledEnumException {
        if(!(id.equals(Id.BOSS) || id.equals(Id.MONSTER) || id.equals(Id.NPC) || id.equals(Id.PLAYER))) {
            throw new UnhandledEnumException(id);
        }
    }

}
