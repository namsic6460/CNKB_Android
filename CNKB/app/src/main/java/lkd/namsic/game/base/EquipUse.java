package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.EquipUseException;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.GameObject;

public abstract class EquipUse {

    public EquipUse(int useHp, int useMn) {
        this(useHp, 0, 0, useMn, 0, 0);
    }

    public EquipUse(int useHp, double useHpTotal, double useHpCurrent, int useMn, double useMnTotal, double useMnCurrent) {
        if(useHpTotal < 0 || useHpTotal > 0.9999) {
            throw new NumberRangeException(useHpTotal, 0, 0.9999);
        } else if(useHpCurrent < 0 || useHpCurrent > 0.9999) {
            throw new NumberRangeException(useHpCurrent, 0, 0.9999);
        } else if(useMnTotal < 0 || useMnTotal > 1) {
            throw new NumberRangeException(useMnTotal, 0, 1);
        } else if(useMnCurrent < 0 || useMnCurrent > 1) {
            throw new NumberRangeException(useMnCurrent, 0, 1);
        }

        this.useHp = useHp;
        this.useHpTotal = useHpTotal;
        this.useHpCurrent = useHpCurrent;
        this.useMn = useMn;
        this.useMnTotal = useMnTotal;
        this.useMnCurrent = useMnCurrent;
    }

    final int useHp;
    final double useHpTotal;
    final double useHpCurrent;
    final int useMn;
    final double useMnTotal;
    final double useMnCurrent;

    @NonNull
    public String tryUse(@NonNull Entity self, @Nullable String other) {
        int hp = self.getStat(StatType.HP);
        int maxHp = self.getStat(StatType.MAXHP);
        int mn = self.getStat(StatType.MN);
        int maxMn = self.getStat(StatType.MAXMN);

        int useHp = this.useHp + (int) (hp * this.useHpCurrent) + (int) (maxHp * this.useHpTotal);
        int useMn = this.useMn + (int) (hp * this.useMnCurrent) + (int) (maxMn * this.useMnTotal);

        if(hp <= useHp) {
            throw new EquipUseException("이 장비를 사용하기 위해서는 현재 체력이 " + useHp + " 초과여야 합니다");
        } else if(mn < useMn) {
            throw new EquipUseException("이 장비를 사용하기 위해서는 현재 마나가 " + useHp + " 이상이어야 합니다");
        }

        self.addBasicStat(StatType.HP, useHp * -1);
        self.addBasicStat(StatType.MN, useMn * -1);

        return this.use(self, other);
    }

    @NonNull
    public abstract String use(@NonNull Entity self, @Nullable String other);

}
