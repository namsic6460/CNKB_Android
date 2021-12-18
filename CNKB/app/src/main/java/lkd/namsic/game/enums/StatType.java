package lkd.namsic.game.enums;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum StatType {
    
    MAXHP("최대 체력", 1),
    HP("현재 체력", 0),
    MAXMN("최대 마나", 5),
    MN("현재 마나", 0),
    ATK("공격력", 5),
    MATK("마법 공격력", 5),
    AGI("민첩", 1),
    ATS("공격속도", 1),
    DEF("방어력", 3),
    MDEF("마법 방어력", 3),
    BRE("방어 관통력", 2),
    MBRE("마법 방어 관통력", 2),
    DRA("흡수력", 5),
    MDRA("마법 흡수력", 5),
    EVA("회피", 1),
    ACC("정확도", 1);
    
    private static final Map<String, StatType> map = new HashMap<>();
    
    static {
        for(StatType statType : StatType.values()) {
            map.put(statType.displayName, statType);
        }
    }
    
    @Getter
    @NonNull
    private final String displayName;
    
    @Getter
    private final int useSp;
    
    StatType(@NonNull String displayName, int useSp) {
        this.displayName = displayName;
        this.useSp = useSp;
    }
    
    @Nullable
    public static StatType findByName(@NonNull String name) {
        StatType statType = map.get(name);
        
        if(statType == null) {
            try {
                statType = StatType.valueOf(name);
            } catch(IllegalArgumentException e) {
                return null;
            }
        }
        
        return statType;
    }
}
