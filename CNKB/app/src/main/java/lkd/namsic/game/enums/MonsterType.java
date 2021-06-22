package lkd.namsic.game.enums;

public enum MonsterType {

    GOOD("우호"),
    MIDDLE("중립"),
    BAD("적대");

    public final String name;

    MonsterType(String name) {
        this.name = name;
    }

}
