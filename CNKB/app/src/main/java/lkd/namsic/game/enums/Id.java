package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import lkd.namsic.game.exception.UnhandledEnumException;

public enum Id {

    ACHIEVE,
    BOSS,
    CHAT,
    EQUIPMENT,
    FARM,
    ITEM,
    MONSTER,
    NPC,
    PLANT,
    PLAYER,
    QUEST,
    RESEARCH,
    SHOP,
    SKILL;

    public static void checkEntityId(@NonNull Id id) throws UnhandledEnumException {
        if(!(id.equals(Id.BOSS) || id.equals(Id.MONSTER) || id.equals(Id.NPC) || id.equals(Id.PLAYER))) {
            throw new UnhandledEnumException(id);
        }
    }

}
