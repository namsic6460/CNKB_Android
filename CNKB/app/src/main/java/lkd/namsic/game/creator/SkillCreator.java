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
        createSkill(SkillList.MAGIC_BALL, "마법 공격력에 해당하는 마법 데미지를 가한다\n[회피 불가] [치명타 불가]");

        Logger.i("ObjectMaker", "Skill making is done!");
    }

}
