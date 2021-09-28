package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.UnhandledEnumException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Player;

public class StatManager {

    private static final StatManager instance = new StatManager();

    public static StatManager getInstance() {
        return instance;
    }

    public void displayStatInfo(@NonNull Player self) {
        self.replyPlayer("스텟에 관한 정보는 전체보기로 확인해주세요",
                "---스텟 정보---\n" +
                        Emoji.LIST + " 최대 체력(MaxHp) : 대상이 가질 수 있는 최대량의 체력을 나타냅니다 (" +
                        StatType.MAXHP.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 체력(Hp) : 대상이 현재 가지고 있는 체력을 나타냅니다 (-)\n\n" +
                        Emoji.LIST + " 최대 마나(MaxMn) : 대상이 가질 수 있는 최대량의 마나를 나타냅니다 (" +
                        StatType.MAXMN.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 마나(Mn) : 대상이 현재 가지고 있는 마나를 나타냅니다 (-)\n\n" +
                        Emoji.LIST + " 공격력(Atk) : 대상의 물리 공격력을 나타냅니다 (" +
                        StatType.ATK.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 마법 공격력(MAtk) : 대상의 마법 공격력을 나타냅니다 (" +
                        StatType.MATK.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 민첩(Agi) : 치명타 확률을 나타냅니다 (스텟 1당 치명타 확률 " + (Config.CRIT_PER_AGI * 100) + "%) (" +
                        StatType.AGI.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 공격 속도(Ats) : 전투에서 턴을 가져올 상대적 우선권 나타냅니다 (" +
                        StatType.ATS.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 방어력(Def) : 물리 데미지를 방어하는 수치를 나타냅니다 (" +
                        StatType.DEF.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 마법 방어력(MDef) : 마법 데미지를 방어하는 수치를 나타냅니다 (" +
                        StatType.MDEF.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 방어 관통력(Bre) : 공격 시 대상의 방어력을 상쇄하는 수치를 나타냅니다 (" +
                        StatType.BRE.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 마법 관통력(MBre) : 공격 시 대상의 마법 방어력을 상쇄하는 수치를 나타냅니다 (" +
                        StatType.MBRE.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 흡수력(Dra) : 공격 시 최종 물리 데미지의 일부 %를 회복하는 수치를 나타냅니다 (최대: 100) (" +
                        StatType.DRA.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 마법 흡수력(MDra) : 공격 시 최종 마법 데미지의 일부 %를 회복하는 수치를 나타냅니다 (최대: 100) (" +
                        StatType.MDRA.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 회피(Eva) : 공격을 회피할 수 있는 수치를 나타냅니다 (회피 - 정확도 <= " + Config.MAX_EVADE + ") (" +
                        StatType.EVA.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 정확도(Acc) : 공격 시 대상의 회피를 상쇄하는 수치를 나타냅니다 (" +
                        StatType.ACC.getUseSp() + "SP)"
        );
    }

    public void increaseStat(@NonNull Player self, @NonNull StatType statType, int stat) {
        int sp = self.getSp();
        int requiredSp = statType.getUseSp() * stat;

        if(statType.equals(StatType.AGI)) {
            int maxRemainStat = Config.MAX_AGI - self.getBasicStat(statType);

            if(stat > maxRemainStat) {
                throw new WeirdCommandException("민첩 스텟은 " + Config.MAX_AGI +
                        "가 최대입니다(순수 스텟)\n증가 가능한 최대 스텟: " + maxRemainStat);
            }
        } else if(statType.equals(StatType.DRA) || statType.equals(StatType.MDRA)) {
            int maxRemainStat = Config.MAX_DRA - self.getBasicStat(statType);

            if(stat > maxRemainStat) {
                throw new WeirdCommandException(statType.getDisplayName() + " 스텟은 " + Config.MAX_AGI +
                        "가 최대입니다(순수 스텟)\n증가 가능한 최대 스텟: " + maxRemainStat);
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
