package lkd.namsic.game.object.implement;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lkd.namsic.game.base.Bool;
import lkd.namsic.game.base.IdClass;
import lkd.namsic.game.base.Int;
import lkd.namsic.game.base.WrappedObject;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EventList;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.event.DamageEvent;
import lkd.namsic.game.event.DamagedEvent;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.event.FightEndEvent;
import lkd.namsic.game.event.PreDamageEvent;
import lkd.namsic.game.event.SelfTurnEvent;
import lkd.namsic.game.event.StartFightEvent;
import lkd.namsic.game.event.TurnEvent;
import lkd.namsic.game.exception.EventRemoveException;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Player;

public class EntityEvents {

    private final static Map<Long, Event> MAP = new HashMap<Long, Event>() {{
        put(EventList.ENT_DAMAGED.getId(), new DamagedEvent() {
            @Override
            public void onDamaged(@NonNull Entity self, @NonNull Entity attacker,
                                  @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                int waitTurn = self.getVariable(Variable.ENT_WAIT_TURN);

                if(waitTurn == 0) {
                    self.removeVariable(Variable.ENT_WAIT_TURN);

                    if(attacker.getId().getId().equals(Id.PLAYER)) {
                        ((Player) attacker).replyPlayer("마수의 기운이 깨어났습니다!");
                    }

                    self.addBasicStat(StatType.ATK, 50);
                    self.addBasicStat(StatType.BRE, 50);

                    throw new EventRemoveException();
                } else {
                    self.setVariable(Variable.ENT_WAIT_TURN, waitTurn - 1);

                    if(attacker.getId().getId().equals(Id.PLAYER)) {
                        ((Player) attacker).replyPlayer("마수의 기운이 깨어나고 있습니다");
                    }
                }
            }
        });

        put(EventList.OAK_START_FIGHT.getId(), new StartFightEvent() {
            @Override
            public void onStartFight(@NonNull Entity self, @NonNull Entity enemy, boolean isOwner) {
                Object[] nearestEntity = self.getNearestEntity(Id.MONSTER);

                if(nearestEntity == null) {
                    return;
                }

                IdClass nearestAllyId = (IdClass) nearestEntity[0];
                int distant = (int) nearestEntity[1];

                if(distant > 32) {
                    return;
                }

                long fightId = FightManager.getInstance().getFightId(self.getId());
                FightManager.getInstance().fightId.put(nearestAllyId, fightId);

                Entity entity = Config.loadObject(Id.MONSTER, nearestAllyId.getObjectId());
                entity.setDoing(Doing.FIGHT);
                FightManager.getInstance().getEntitySet(fightId).add(entity);

                if(enemy.getId().getId().equals(Id.PLAYER)) {
                    Player player = (Player) enemy;

                    player.replyPlayer("주변에 있던 다른 오크가 분노하여 달려듭니다!");
                }
            }
        });

        put(EventList.SKILL_SMITE.getId(), new PreDamageEvent() {
            @Override
            public void onPreDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int physicDmg,
                                    @NonNull Int magicDmg, @NonNull Int staticDmg, boolean canCrit) {
                if(magicDmg.get() == 0) {
                    magicDmg.add((int) (physicDmg.get() * 0.05));
                }
            }
        });

        put(EventList.IMP_ATTACK.getId(), new DamageEvent() {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                 @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                int basicAtk = self.getVariable(Variable.IMP_ORIGINAL_ATK);
                int atk = self.getStat(StatType.ATK);

                if(basicAtk == 0) {
                    basicAtk = atk;
                    self.setVariable(Variable.IMP_ORIGINAL_ATK, basicAtk);
                }

                if(atk <= basicAtk * 2) {
                    self.addBasicStat(StatType.ATK, (int) (atk * 0.1));
                }
            }
        });

        put(EventList.SCAR_BLOOD.getId(), new TurnEvent() {
            @Override
            public void onTurn(@NonNull Entity self, @NonNull WrappedObject<Entity> wrappedAttacker) {
                int scarBlood = self.getVariable(Variable.SCAR_BLOOD);
                int scarBloodDmg = self.getVariable(Variable.SCAR_BLOOD_DAMAGE);

                if(scarBlood > 0) {
                    Entity attacker = Objects.requireNonNull(self.getObjectVariable(Variable.SCAR_ENTITY));
                    attacker.damage(self, scarBloodDmg, 0, 0, false, false, false);

                    scarBlood--;
                    if(scarBlood == 0) {
                        throw new EventRemoveException();
                    } else {
                        self.setVariable(Variable.SCAR_BLOOD, scarBlood);
                    }
                } else {
                    throw new EventRemoveException();
                }
            }
        });

        put(EventList.SCAR_END.getId(), new FightEndEvent() {
            @Override
            public void onFightEnd(@NonNull Entity self) {
                self.removeVariable(Variable.SCAR_ENTITY);
                self.removeVariable(Variable.SCAR_BLOOD);
                self.removeVariable(Variable.SCAR_BLOOD_DAMAGE);

                throw new EventRemoveException();
            }
        });

        put(EventList.CHARM.getId(), new SelfTurnEvent() {
            @Override
            public void onSelfTurn(@NonNull Entity self, @NonNull WrappedObject<Entity> wrappedAttacker) {
                Entity attacker = self.getObjectVariable(Variable.CHARM_ENTITY);
                if(attacker != null) {
                    wrappedAttacker.set(attacker);

                    self.setVariable(Variable.CHARM_SUCCESS, true);
                    self.addEvent(EventList.CHARM_ATTACK);
                    self.addEvent(EventList.CHARM_REMOVE);
                    
                    if(attacker.getId().getId().equals(Id.PLAYER)) {
                        ((Player) attacker).replyPlayer(Emoji.focus(SkillList.CHARM.getDisplayName()) +
                                " 스킬로 턴을 빼앗았습니다\n이번 턴의 공격이 강화됩니다");
                    }
                }

                throw new EventRemoveException();
            }
        });

        put(EventList.CHARM_END.getId(), new FightEndEvent() {
            @Override
            public void onFightEnd(@NonNull Entity self) {
                self.removeVariable(Variable.CHARM_ENTITY);
                self.removeVariable(Variable.CHARM_SUCCESS);

                throw new EventRemoveException();
            }
        });

        put(EventList.CHARM_ATTACK.getId(), new DamageEvent() {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                 @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                Entity entity = self.getObjectVariable(Variable.CHARM_ENTITY);

                if(entity != null && entity.getObjectVariable(Variable.CHARM_SUCCESS, false)) {
                    if (canCrit && !isCrit.get()) {
                        isCrit.set(true);
                        totalDmg.multiple(3);
                    }
                }

                throw new EventRemoveException();
            }
        });

        put(EventList.CHARM_REMOVE.getId(), new TurnEvent() {
            @Override
            public void onTurn(@NonNull Entity self, @NonNull WrappedObject<Entity> attacker) {
                if(self.getObjectVariable(Variable.CHARM_SUCCESS, false)) {
                    self.removeVariable(Variable.CHARM_SUCCESS);
                }

                throw new EventRemoveException();
            }
        });

        put(EventList.GOLEM_ATTACKED.getId(), new DamagedEvent() {
            @Override
            public void onDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int totalDmg,
                                  @NonNull Int totalDra, @NonNull Bool isCrit) {
                int def = self.getStat(StatType.DEF);

                if(def > 0) {
                    self.addBasicStat(StatType.DEF, Math.max(-1 * def, -10));
                }
            }
        });
    }};

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T extends Event> T getEvent(long eventId) {
        return (T) Objects.requireNonNull(MAP.get(eventId));
    }

}
