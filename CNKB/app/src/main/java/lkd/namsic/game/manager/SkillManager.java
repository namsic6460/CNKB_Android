package lkd.namsic.game.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;
import java.util.Set;

import lkd.namsic.game.base.SkillUse;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.object.Skill;

public class SkillManager {

    private static final SkillManager instance = new SkillManager();

    public static SkillManager getInstance() {
        return instance;
    }

    private static final String RESIST = "대상이 스킬 저항에 성공했습니다";

    public long checkUse(@NonNull Entity self, @NonNull String objectName, @Nullable String other) {
        Long skillId = SkillList.findByName(objectName);

        if(skillId == null) {
            throw new WeirdCommandException("알 수 없는 스킬입니다");
        }

        if(!self.getSkill().contains(skillId)) {
            throw new WeirdCommandException("보유하지 않은 스킬입니다");
        }

        Skill skill = Config.getData(Id.SKILL, skillId);

        SkillUse use = skill.getSkillUse();
        if(use == null) {
            throw new WeirdCommandException("사용이 불가능한 스킬입니다");
        }

        use.checkUse(self, other);
        return skillId;
    }

    @NonNull
    public String use(@NonNull Entity self, long skillId, @Nullable String other, @NonNull Set<Player> playerSet) {
        Skill skill = Config.getData(Id.SKILL, skillId);
        String skillName = skill.getName();

        String msg = self.getFightName() + " (이/가) " + skillName + " (을/를) 사용했습니다";
        if(self.getId().getId().equals(Id.PLAYER)) {
            Player.replyPlayersExcept(playerSet, msg, (Player) self);
        } else {
            Player.replyPlayers(playerSet, msg);
        }

        SkillUse use = Objects.requireNonNull(skill.getSkillUse());
        String result = use.tryUse(self, other);

        if(self.getId().getId().equals(Id.PLAYER)) {
            Player player = (Player) self;

            if(use.getWaitTurn() == 0) {
                msg = result + " 에게 " + skillName + " (을/를) 사용했습니다";
            } else {
                player.setVariable(Variable.FIGHT_CASTING_SKILL, skillId);
                player.setVariable(Variable.FIGHT_SKILL_CAST_WAIT_TURN, use.getWaitTurn());
                msg = result + " 에게 " + skillName + " 캐스팅을 시작했습니다";
            }

            player.addLog(LogData.TOTAL_SKILL_USE, 1);
            player.replyPlayer(msg);

            if(player.getObjectVariable(Variable.RESISTED_SKILL, false)) {
                player.removeVariable(Variable.RESISTED_SKILL);
                player.replyPlayer(RESIST);
            }
        }

        return "";
    }

}
