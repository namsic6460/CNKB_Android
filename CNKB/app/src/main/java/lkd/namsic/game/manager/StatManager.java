package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.UnhandledEnumException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Player;

public class StatManager {

    private static final StatManager instance = new StatManager();

    public static StatManager getInstance() {
        return instance;
    }

    public void increaseStat(@NonNull Player self, @NonNull StatType statType, int stat) {
        int sp = self.getSp();
        int requiredSp = statType.getUseSp() * stat;

        if(statType.equals(StatType.AGI)) {
            int maxRemainStat = Config.MAX_AGI - self.getStat(statType);

            if(stat > maxRemainStat) {
                throw new WeirdCommandException(StatType.AGI.getDisplayName() + " 스텟은 " +
                        Config.MAX_AGI + "가 최대입니다\n증가 가능한 최대 스텟: " + maxRemainStat);
            }
        }

        try {
            Config.checkStatType(statType);
        } catch (UnhandledEnumException e) {
            throw new WeirdCommandException("증가시킬 수 없는 스텟입니다");
        }

        if(sp < requiredSp) {
            throw new WeirdCommandException(statType.getDisplayName() + "(을/를) " + stat + "만큼 올리기 위한 SP가 " +
                    (requiredSp - sp) + "만큼 부족합니다");
        }

        int prevStat = self.getStat(statType);
        self.addBasicStat(statType, stat);
        self.setSp(self.getSp() + (-1 * requiredSp));

        if(statType.equals(StatType.MAXHP)) {
            self.addBasicStat(StatType.HP, stat);
        } else if(statType.equals(StatType.MAXMN)) {
            self.addBasicStat(StatType.MN, stat);
        }

        self.replyPlayer("스텟이 성공적으로 증가되었습니다\n" +
                statType.getDisplayName() + ": " + prevStat + " -> " + self.getStat(statType) + "\n" +
                "남은 SP: " + (sp - requiredSp));
    }

}
