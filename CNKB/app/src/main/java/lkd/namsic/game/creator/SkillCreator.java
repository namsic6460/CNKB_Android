package lkd.namsic.game.creator;

import androidx.annotation.NonNull;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.object.Skill;
import lkd.namsic.setting.Logger;

public class SkillCreator implements Creatable {

    private void createSkill(@NonNull SkillList skillData, @NonNull String description) {
        Skill skill = new Skill(skillData, description);
        Config.unloadObject(skill);
    }

    @Override
    public void start() {
        createSkill(SkillList.MAGIC_BALL, "[1 마나] [회피 불가] [치명타 불가]\n(마법 공격력 * 1) 에 해당하는 마법 데미지를 가한다");
        createSkill(SkillList.SMITE, "[5 마나]\n(공격력 * 0.5) 에 해당하는 물리 데미지 + (마법 공격력 * 0.5) 에 해당하는 " +
                "마법 데미지 + ((공격력 + 마법 공격력) * 0.1) 에 해당하는 고정 데미지를 가한다\n" +
                "공격 전 마법 데미지가 0이면 물리 데미지의 5%에 해당하는 마법 데미지를 가한다");
        createSkill(SkillList.LASER, "[10 마나] [1 턴]\n(마법 공격력 * 2.5) 에 해당하는 마법 데미지를 가한다");

        Logger.i("ObjectMaker", "Skill making is done!");
    }

}
