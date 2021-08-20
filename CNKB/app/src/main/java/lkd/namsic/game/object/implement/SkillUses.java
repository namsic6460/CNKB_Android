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
        put(SkillList.MAGIC_BALL.getId(), new SkillUse(0, 1) {
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                Entity target = Objects.requireNonNull(targets.get(0));
                self.damage(target, 0, self.getBasicStat(StatType.MATK),
                        5, false, false, true);
            }
        });

        put(SkillList.SMITE.getId(), new SkillUse(0, 5) {
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                Entity target = Objects.requireNonNull(targets.get(0));

                int atk = self.getBasicStat(StatType.ATK);
                int matk = self.getBasicStat(StatType.MATK);
                self.damage(target, atk * 0.5, matk * 0.5, (atk + matk) * 0.1,
                        true, true, true);
            }
        });

        put(SkillList.LASER.getId(), new SkillUse(0, 10) {
            @Override
            public int getWaitTurn() {
                return 1;
            }

            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                Entity target = Objects.requireNonNull(targets.get(0));

                self.damage(target, 0, self.getBasicStat(StatType.MATK) * 2.25, 0,
                        true, true, true);
            }
        });
    }};

}
