package lkd.namsic.game.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;
import java.util.Set;

import lkd.namsic.game.base.SkillUse;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.NpcList;
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

    public void displaySkillHelp(@NonNull Player self) {
        self.replyPlayer(
                "1. 스킬은 전투중에만 사용이 가능합니다\n" +
                        "2. 대부분의 스킬의 액티브는 대상이 필요합니다\n" +
                        "3. 스킬은 NPC 상점에서 스킬 북을 구매하여 사용하면 배울 수 있습니다(시작의 마을의 경우 " +
                        Emoji.focus(NpcList.SELINA.getDisplayName()) + ")\n"
        );
    }

    public void displaySkillList(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder("---보유 스킬 목록---");

        if(self.getSkill().isEmpty()) {
            self.replyPlayer("보유중인 스킬이 없습니다");
            return;
        }

        for(long skillId : self.getSkill()) {
            innerBuilder.append("\n- ")
                    .append(SkillList.findById(skillId));
        }

        self.replyPlayer("스킬 목록은 전체보기로 확인해주세요", innerBuilder.toString());
    }

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

    public void use(@NonNull Entity self, long skillId, @Nullable String other, @NonNull Set<Player> playerSet) {
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

        if(use.getWaitTurn() == 0) {
            msg = skillName + " (을/를) 사용했습니다";
        } else {
            self.setVariable(Variable.FIGHT_CASTING_SKILL, skillId);
            self.setVariable(Variable.FIGHT_SKILL_CAST_WAIT_TURN, use.getWaitTurn());

            msg = skillName + " 캐스팅을 시작했습니다";
        }

        if(self.getId().getId().equals(Id.PLAYER)) {
            Player player = (Player) self;

            if(!result.equals("")) {
                msg = result + " 에게 " + msg;
            }

            player.addLog(LogData.TOTAL_SKILL_USE, 1);
            player.replyPlayer(msg);
        }

        if(self.getObjectVariable(Variable.RESISTED_SKILL, false)) {
            self.removeVariable(Variable.RESISTED_SKILL);

            if(self.getId().getId().equals(Id.PLAYER)) {
                ((Player) self).replyPlayer(RESIST);
            }
        }
    }

}
