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
        
        createSkill(SkillList.SCAR, "[5 마나] [최대 3 대상]\n모든 대상에게 공격력의 10% 에 해당하는 데미지를 3번 가한다\n" +
                "만약 3번의 공격 중 치명타가 터졌다면 대상에게 3턴간 공격력의 20%의 고정 데미지를 주는 출혈을 일으킨다", null);
        
        createSkill(SkillList.CHARM, "[15 마나] [저항 가능]\n대상의 턴이 오면 턴을 강제로 빼앗고 " +
                "해당 턴의 공격을 무조건 치명타로 만든다", null);
        
        createSkill(SkillList.STRINGS_OF_LIFE, null,
                "[전투 당 1회]\n체력이 최대 체력의 50% 이하일 때 죽음에 이르는 피해를 입을 시 체력을 1 남기고 생존한다");
        
        createSkill(SkillList.RESIST, "[10 마나]\n3턴간 받는 모든 물리 피해를 60% 줄여 받는다", null);
        
        createSkill(SkillList.RUSH, "[5 마나] [치명타 불가]\n" +
                        "대상의 위치까지 필드를 이동하고 이동한 거리와 공격력에 비례하여 데미지를 가한다", null);

        createSkill(SkillList.ROAR, "[2 마나]\n모든 적의 공격속도를 레벨 차이에 비례하여 30초간 감소시킨다\n" +
                "(1.5레벨 차이당 1%) (최대 30%)", null);

        createSkill(SkillList.HOWLING_OF_WOLF, "[30 마나] [획득 불가]\n" +
                "주변에 라이칸스로프 두 마리를 소환하여 전투에 난입시킨다", null);
        
        createSkill(SkillList.CUTTING_MOONLIGHT, "[8 마나]\n" + "다른 타입의(n 설정 달빛 베기) 적 전체에게 " +
                "(공격력 * 0.9) 또는 (마법 공격력 * 0.9) 중 더 높은 스텟으로 물리 또는 마법 데미지를 가한다",null);
        
        createSkill(SkillList.SPAWN_IMP, "[50 마나] [획득 불가]\n" +
            "주변에 임프 세 마리를 소환하여 전투에 난입시킨다", null);
        
        createSkill(SkillList.ESSENCE_DRAIN, "[10 마나] [회피 불가] [치명타 불가]\n" +
            "대상에게 (마법 공격력 * 1) 에 해당하는 마법 데미지를 가하고 그 데미지만큼 회복한다", null);
        
        createSkill(SkillList.MAGIC_DRAIN, "[회피 불가]\n대상의 최대 마나의 10% 에 해당하는 " +
            "마나를 없애고, 자신의 최대 마나의 10% 에 해당하는 마나를 회복한다", null);
        
        createSkill(SkillList.MANA_EXPLOSION, "[50 마나] [회피 불가] [3 턴]\n필드 거리 16 내의 " +
            "모든 대상에게 (마법 공격력 * 5) 에 해당하는 마법 데미지를 가한다", null);

        Logger.i("ObjectMaker", "Skill making is done!");
    }

}
