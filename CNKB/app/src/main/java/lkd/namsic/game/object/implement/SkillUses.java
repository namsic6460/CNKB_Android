package lkd.namsic.game.object.implement;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lkd.namsic.game.base.SkillUse;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EventList;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Player;

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

        put(SkillList.SCAR.getId(), new SkillUse(0, 5) {
            @Override
            public int getMaxTargetCount() {
                return 3;
            }

            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                int atk = self.getBasicStat(StatType.ATK);
                int staticDmg = atk / 10;

                Player player;
                int damage, drain, heal;
                boolean isCrit = false;

                int totalDmg = 0;
                int totalDra = 0;
                int critCount = 0;

                for (Entity target : targets) {
                    player = target.getId().getId().equals(Id.PLAYER) ? (Player) target : null;
                    damage = 0;
                    drain = 0;
                    heal = 0;

                    if(player != null) {
                        player.setPrintDeathMsg(false);
                    }

                    for (int i = 0; i < 3; i++) {
                        self.damage(target, 0, 0, staticDmg, true, true, false);

                        damage += self.getLastDamage();
                        drain += self.getLastDrain();
                        heal += target.getLastHeal();

                        if (self.isLastCrit()) {
                            isCrit = true;
                            critCount++;
                        }
                    }

                    totalDmg += damage;
                    totalDra += drain;

                    if (player != null) {
                        player.printDamagedMsg(self, damage, drain, isCrit, heal);

                        if(player.getKiller() != null) {
                            player.replyPlayer(Player.DEATH_MSG, player.getDeathMsg());
                        }
                    }

                    if (isCrit) {
                        target.setVariable(Variable.SCAR_ENTITY, self);
                        target.setVariable(Variable.SCAR_BLOOD, 3);
                        target.setVariable(Variable.SCAR_BLOOD_DAMAGE,
                                Math.max(atk * 0.2, self.getVariable(Variable.SCAR_BLOOD_DAMAGE)));

                        target.addEvent(EventList.SCAR_BLOOD);
                        target.addEvent(EventList.SCAR_END);
                    }
                }

                if (self.getId().getId().equals(Id.PLAYER)) {
                    ((Player) self).printDamageMsg(targets, totalDmg, totalDra, critCount);
                }
            }
        });

        put(SkillList.CHARM.getId(), new SkillUse(0, 15) {
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                Entity target = Objects.requireNonNull(targets.get(0));

                if (Math.random() * 0.5 + (target.getStat(StatType.MDEF) - self.getStat(StatType.MATK)) < Math.random()) {
                    target.setVariable(Variable.CHARM_ENTITY, self);

                    target.addEvent(EventList.CHARM);
                    target.addEvent(EventList.CHARM_END);
                } else {
                    self.setVariable(Variable.SKILL_RESIST, true);

                    if (target.getId().getId().equals(Id.PLAYER)) {
                        ((Player) target).replyPlayer(Emoji.focus(SkillList.CHARM.getDisplayName()) + " 스킬 저항에 성공했습니다");
                    }
                }
            }
        });
    }};

}
