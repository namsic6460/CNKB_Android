package lkd.namsic.game.enums;

import lombok.Getter;

public enum MonsterType {
    
    GOOD("우호"),
    MIDDLE("중립"),
    BAD("적대");
    
    @Getter
    private final String displayName;
    
    MonsterType(String displayName) {
        this.displayName = displayName;
    }
    
}
