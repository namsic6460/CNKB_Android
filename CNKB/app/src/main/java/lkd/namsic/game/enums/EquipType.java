package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.exception.WeirdCommandException;
import lombok.Getter;

public enum EquipType {
    
    WEAPON("무기", "weapon"),
    HELMET("투구", "helmet"),
    CHESTPLATE("갑옷", "chestplate", "chest"),
    LEGGINGS("바지", "leggings", "leg"),
    SHOES("신발", "shoes", "shoe"),
    RINGS("반지", "ring", "rings"),
    EARRINGS("귀걸이", "earrings", "earring"),
    NECKLACE("목걸이", "necklace"),
    GEM("보석", "gem"),
    HEART_GEM("심장 보석", "heart gem"),
    AMULET("부적", "amulet");
    
    public static EquipType findByName(@NonNull String name) {
        for(EquipType equipType : EquipType.values()) {
            if(equipType.getDisplayNames().contains(name)) {
                return equipType;
            }
        }
        
        throw new WeirdCommandException("알 수 없는 장비 유형입니다");
    }
    
    @NonNull
    @Getter
    final List<String> displayNames = new ArrayList<>();
    
    EquipType(@NonNull String... displayNames) {
        this.displayNames.addAll(Arrays.asList(displayNames));
    }
    
}
