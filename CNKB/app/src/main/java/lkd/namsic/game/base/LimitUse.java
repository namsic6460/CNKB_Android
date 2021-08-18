package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Entity;

public abstract class LimitUse extends BasicUse {

    public LimitUse(int useHp, int useMn) {
        this(useHp, 0, 0, useMn, 0, 0);
    }

    public LimitUse(int useHp, double useHpTotal, double useHpCurrent, int useMn, double useMnTotal, double useMnCurrent) {
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
    public abstract String getUseName();

    @Override
    public final void checkUse(@NonNull Entity self, @Nullable String other) {
        super.checkUse(self, other);

        int hp = self.getStat(StatType.HP);
        int maxHp = self.getStat(StatType.MAXHP);
        int mn = self.getStat(StatType.MN);
        int maxMn = self.getStat(StatType.MAXMN);

        int useHp = this.useHp + (int) (hp * this.useHpCurrent) + (int) (maxHp * this.useHpTotal);
        int useMn = this.useMn + (int) (hp * this.useMnCurrent) + (int) (maxMn * this.useMnTotal);

        if(hp <= useHp) {
            throw new WeirdCommandException("이 " + this.getUseName() + " 사용하기 위해서는 현재 체력이 " + useHp + " 초과여야 합니다");
        } else if(mn < useMn) {
            throw new WeirdCommandException("이 " + this.getUseName() + " 사용하기 위해서는 현재 마나가 " + useMn + " 이상이어야 합니다");
        }
    }

    @NonNull
    public String tryUse(@NonNull Entity self, @Nullable String other) {
        self.addBasicStat(StatType.HP, -1 * (useHp +
                (int) (self.getStat(StatType.HP) * this.useHpCurrent) +
                (int) (self.getStat(StatType.MAXHP) * this.useHpTotal)));
        self.addBasicStat(StatType.MN, -1 * (useMn +
                (int) (self.getStat(StatType.MN) * this.useMnCurrent) +
                (int) (self.getStat(StatType.MAXMN) * this.useMnTotal)));

        return this.use(self, other);
    }

    @NonNull
    public abstract String use(@NonNull Entity self, @Nullable String other);

}
