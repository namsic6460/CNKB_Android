package lkd.namsic.game.base;

import androidx.annotation.NonNull;

public abstract class EquipUse extends LimitUse {
    
    public EquipUse(int useHp, int useMn) {
        super(useHp, useMn);
    }
    
    public EquipUse(int useHp, double useHpTotal, double useHpCurrent, int useMn, double useMnTotal, double useMnCurrent) {
        super(useHp, useHpTotal, useHpCurrent, useMn, useMnTotal, useMnCurrent);
    }
    
    @NonNull
    @Override
    public String getUseName() {
        return "장비를";
    }
    
}
