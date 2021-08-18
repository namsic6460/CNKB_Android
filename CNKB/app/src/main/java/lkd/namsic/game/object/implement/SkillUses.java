package lkd.namsic.game.object.implement;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lkd.namsic.game.base.SkillUse;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.object.Entity;

public class SkillUses {

    public final static Map<Long, SkillUse> MAP = new HashMap<Long, SkillUse>() {{
        put(SkillList.MAGIC_BALL.getId(), new SkillUse(0, 2) {
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                Entity target = Objects.requireNonNull(targets.get(0));
                self.damage(target, 0, self.getBasicStat(StatType.MATK),
                        5, false, false, true);
            }
        });
    }};

}
