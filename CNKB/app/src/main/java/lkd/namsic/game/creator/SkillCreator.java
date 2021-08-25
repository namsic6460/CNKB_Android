package lkd.namsic.game.creator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.object.Skill;
import lkd.namsic.setting.Logger;

public class SkillCreator implements Creatable {

    private void createSkill(@NonNull SkillList skillData, @Nullable String activeDes, @Nullable String passiveDes) {
        Skill skill = new Skill(skillData, activeDes, passiveDes);
        Config.unloadObject(skill);
    }

    @Override
    public void start() {
        createSkill(SkillList.MAGIC_BALL, "[1 마나] [회피 불가] [치명타 불가]\n" +
                "(마법 공격력 * 1) 에 해당하는 마법 데미지를 가한다",null);
        createSkill(SkillList.SMITE, "[5 마나]\n(공격력 * 0.5) 에 해당하는 물리 데미지 + (마법 공격력 * 0.5) 에 해당하는 " +
                "마법 데미지 + ((공격력 + 마법 공격력) * 0.1) 에 해당하는 고정 데미지를 가한다",
                "공격 전 마법 데미지가 0이면 물리 데미지의 5%에 해당하는 마법 데미지를 가한다");
        createSkill(SkillList.LASER, "[10 마나] [1 턴]\n(마법 공격력 * 2.5) 에 해당하는 마법 데미지를 가한다", null);
        createSkill(SkillList.SCAR, "[5 마나] [최대 3 대상]\n모든 대상에게 공격력의 10% 에 해당하는 고정 데미지를 가한다\n" +
                "만약 3번의 공격 중 치명타가 터졌다면 대상에게 3턴간 공격력의 20% 데미지를 주는 출혈을 일으킨다", null);
        createSkill(SkillList.CHARM, "[15 마나] [저항 가능]\n대상의 턴이 오면 턴을 강제로 빼앗고 " +
                "해당 턴의 공격을 무조건 치명타로 만든다", null);

        Logger.i("ObjectMaker", "Skill making is done!");
    }

}
