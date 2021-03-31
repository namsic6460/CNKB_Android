package lkd.namsic.Game.Enum;

import lkd.namsic.Game.Exception.UnhandledEnumException;

public enum Id {

    ACHIEVE,
    BOSS,
    CHAT,
    EQUIPMENT,
    ITEM,
    MONSTER,
    NPC,
    PLAYER,
    QUEST,
    RESEARCH,
    SKILL;

    public static void checkEntityId(Id id) throws UnhandledEnumException {
        if(!(id.equals(Id.BOSS) || id.equals(Id.MONSTER) || id.equals(Id.NPC) || id.equals(Id.PLAYER))) {
            throw new UnhandledEnumException(id);
        }
    }

}
