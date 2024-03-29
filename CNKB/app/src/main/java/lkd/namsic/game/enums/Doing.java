package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public enum Doing {
    
    NONE("없음"),
    ADVENTURE("모험"),
    APPRAISE("감정"),
    CHAT("대화"),
    FIGHT("전투"),
    FIGHT_ONE("1대1 전투"),
    FISH("낚시"),
    GATHER("채집"),
    HARVEST("농사"),
    MINE("광질"),
    REINFORCE("강화"),
    RESPAWN("리스폰"),
    REST("휴식"),
    SHOP("상점"),
    WAIT_RESPONSE("대화(대답 대기)");
    
    @NonNull
    @Getter
    private final String displayName;
    
    Doing(@NonNull String displayName) {
        this.displayName = displayName;
    }
    
    @NonNull
    public static List<Doing> fightableList() {
        List<Doing> doingList = new ArrayList<>(3);
        doingList.add(Doing.NONE);
        doingList.add(Doing.ADVENTURE);
        doingList.add(Doing.FIGHT);
        
        return doingList;
    }
    
    @NonNull
    public static List<Doing> fightList() {
        List<Doing> doingList = new ArrayList<>(2);
        doingList.add(Doing.FIGHT);
        doingList.add(Doing.FIGHT_ONE);
        
        return doingList;
    }
    
}
