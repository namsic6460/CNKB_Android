package lkd.namsic.game.object.implement;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
import lkd.namsic.game.event.DeathEvent;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.event.EndFightEvent;
import lkd.namsic.game.event.InjectFightEvent;
import lkd.namsic.game.event.PreDamageEvent;
import lkd.namsic.game.event.PreDamagedEvent;
import lkd.namsic.game.event.PreTurnEvent;
import lkd.namsic.game.event.SelfTurnEvent;
import lkd.namsic.game.event.StartFightEvent;
import lkd.namsic.game.event.TurnEvent;
import lkd.namsic.game.exception.EventRemoveException;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Boss;
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

        put(EventList.OAK_START.getId(), new StartFightEvent() {
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

                Entity entity = Config.loadObject(Id.MONSTER, nearestAllyId.getObjectId());
                entity.setDoing(Doing.FIGHT);

                FightManager.getInstance().fightId.put(nearestAllyId, fightId);
                FightManager.getInstance().getEntitySet(fightId).add(entity);

                if(enemy.getId().getId().equals(Id.PLAYER)) {
                    Player player = (Player) enemy;
                    player.replyPlayer("주변에 있던 다른 오크가 분노하여 달려듭니다!");
                }
            }
        });

        put(EventList.SKILL_SMITE_PRE_DAMAGE.getId(), new PreDamageEvent() {
            @Override
            public void onPreDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int physicDmg,
                                    @NonNull Int magicDmg, @NonNull Int staticDmg, boolean canCrit) {
                if(magicDmg.get() == 0) {
                    magicDmg.add((int) (physicDmg.get() * 0.05));
                }
            }
        });

        put(EventList.IMP_DAMAGE.getId(), new DamageEvent() {
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

        put(EventList.SCAR_TURN.getId(), new TurnEvent() {
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

        put(EventList.SCAR_END.getId(), new EndFightEvent() {
            @Override
            public void onEndFight(@NonNull Entity self) {
                self.removeVariable(Variable.SCAR_ENTITY);
                self.removeVariable(Variable.SCAR_BLOOD);
                self.removeVariable(Variable.SCAR_BLOOD_DAMAGE);

                throw new EventRemoveException();
            }
        });

        put(EventList.CHARM_SELF_TURN.getId(), new SelfTurnEvent() {
            @Override
            public void onSelfTurn(@NonNull Entity self, @NonNull WrappedObject<Entity> wrappedAttacker) {
                Entity entity = self.getObjectVariable(Variable.CHARM_ENTITY);
                if(entity != null) {
                    self.removeVariable(Variable.CHARM_ENTITY);

                    wrappedAttacker.set(entity);

                    entity.addEvent(EventList.CHARM_DAMAGE);
                    entity.addEvent(EventList.CHARM_TURN);
                    
                    if(entity.getId().getId().equals(Id.PLAYER)) {
                        ((Player) entity).replyPlayer(Emoji.focus(SkillList.CHARM.getDisplayName()) +
                                " 스킬로 턴을 빼앗았습니다\n이번 턴의 공격이 강화됩니다");
                    }
                }

                self.removeEvent(EventList.CHARM_END);
                throw new EventRemoveException();
            }
        });

        put(EventList.CHARM_END.getId(), new EndFightEvent() {
            @Override
            public void onEndFight(@NonNull Entity self) {
                self.removeVariable(Variable.CHARM_ENTITY);

                self.removeEvent(EventList.CHARM_SELF_TURN);
                throw new EventRemoveException();
            }
        });

        put(EventList.CHARM_DAMAGE.getId(), new DamageEvent() {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                 @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                if (canCrit && !isCrit.get()) {
                    isCrit.set(true);
                    totalDmg.multiple(3);
                }

                self.removeEvent(EventList.CHARM_TURN);
                throw new EventRemoveException();
            }
        });

        put(EventList.CHARM_TURN.getId(), new TurnEvent() {
            @Override
            public void onTurn(@NonNull Entity self, @NonNull WrappedObject<Entity> attacker) {
                self.removeEvent(EventList.CHARM_DAMAGE);
                throw new EventRemoveException();
            }
        });

        put(EventList.GOLEM_DAMAGED.getId(), new DamagedEvent() {
            @Override
            public void onDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int totalDmg,
                                  @NonNull Int totalDra, @NonNull Bool isCrit) {
                int def = self.getStat(StatType.DEF);

                if(def > 0) {
                    self.addBasicStat(StatType.DEF, Math.max(-1 * def, -10));
                }
            }
        });

        put(EventList.SKILL_STRINGS_OF_LIFE_START.getId(), new StartFightEvent() {
            @Override
            public void onStartFight(@NonNull Entity self, @NonNull Entity enemy, boolean isOwner) {
                self.addEvent(EventList.STRINGS_OF_LIFE_DEATH);
                self.addEvent(EventList.STRINGS_OF_LIFE_END);
            }
        });

        put(EventList.SKILL_STRINGS_OF_LIFE_INJECT.getId(), new InjectFightEvent() {
            @Override
            public void onInjectFight(@NonNull Entity self, @NonNull Entity enemy) {
                self.addEvent(EventList.STRINGS_OF_LIFE_DEATH);
                self.addEvent(EventList.STRINGS_OF_LIFE_END);
            }
        });

        put(EventList.STRINGS_OF_LIFE_DEATH.getId(), new DeathEvent() {
            @Override
            public void onDeath(@NonNull Entity self, int beforeHp, Int afterHp) {
                afterHp.set(1);
                
                if(self.getId().getId().equals(Id.PLAYER)) {
                    ((Player) self).replyPlayer(SkillList.STRINGS_OF_LIFE.getDisplayName() + " 의 효과로 버텼습니다");
                }

                self.removeEvent(EventList.STRINGS_OF_LIFE_END);
                throw new EventRemoveException();
            }
        });

        put(EventList.STRINGS_OF_LIFE_END.getId(), new EndFightEvent() {
            @Override
            public void onEndFight(@NonNull Entity self) {
                self.removeEvent(EventList.STRINGS_OF_LIFE_DEATH);
                throw new EventRemoveException();
            }
        });

        put(EventList.RESIST_PRE_DAMAGED.getId(), new PreDamagedEvent() {
            @Override
            public void onPreDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int physicDmg,
                                     @NonNull Int magicDmg, @NonNull Int staticDmg, boolean canCrit) {
                physicDmg.multiple(0.4);
            }
        });

        put(EventList.RESIST_TURN.getId(), new TurnEvent() {
            @Override
            public void onTurn(@NonNull Entity self, @NonNull WrappedObject<Entity> attacker) {
                int resist = self.getVariable(Variable.RESIST);

                if(resist == 1) {
                    self.removeVariable(Variable.RESIST);
                    self.removeEvent(EventList.RESIST_PRE_DAMAGED);
                    self.removeEvent(EventList.RESIST_END);

                    throw new EventRemoveException();
                } else {
                    self.setVariable(Variable.RESIST, resist - 1);
                }
            }
        });

        put(EventList.RESIST_END.getId(), new EndFightEvent() {
            @Override
            public void onEndFight(@NonNull Entity self) {
                self.removeVariable(Variable.RESIST);
                self.removeEvent(EventList.RESIST_PRE_DAMAGED);
                self.removeEvent(EventList.RESIST_TURN);

                throw new EventRemoveException();
            }
        });

        put(EventList.SILVER_SWORD_DAMAGE.getId(), new DamageEvent() {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                 @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                totalDra.multiple(0.2);

                self.removeEvent(EventList.SILVER_SWORD_END);
                throw new EventRemoveException();
            }
        });

        put(EventList.SILVER_SWORD_END.getId(), new EndFightEvent() {
            @Override
            public void onEndFight(@NonNull Entity self) {
                self.removeEvent(EventList.SILVER_SWORD_DAMAGE);
                throw new EventRemoveException();
            }
        });

        put(EventList.SILVER_SET_DAMAGE.getId(), new DamageEvent() {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                 @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                totalDra.multiple(0.5);

                self.removeEvent(EventList.SILVER_SET_END);
                throw new EventRemoveException();
            }
        });

        put(EventList.SILVER_SET_END.getId(), new EndFightEvent() {
            @Override
            public void onEndFight(@NonNull Entity self) {
                self.removeEvent(EventList.SILVER_SET_DAMAGE);
                throw new EventRemoveException();
            }
        });

        put(EventList.SILVER_CHESTPLATE_PRE_DAMAGED.getId(), new PreDamagedEvent() {
            @Override
            public void onPreDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int physicDmg,
                                     @NonNull Int magicDmg, @NonNull Int staticDmg, boolean canCrit) {
                if(magicDmg.get() != 0) {
                    self.damage(attacker, physicDmg.get() * 0.3, magicDmg.get() * 0.3,
                            0, true, false, false);
                    magicDmg.divide(2);
                }

                self.removeEvent(EventList.SILVER_CHESTPLATE_END);
                throw new EventRemoveException();
            }
        });

        put(EventList.SILVER_CHESTPLATE_END.getId(), new EndFightEvent() {
            @Override
            public void onEndFight(@NonNull Entity self) {
                self.removeEvent(EventList.SILVER_CHESTPLATE_PRE_DAMAGED);
                throw new EventRemoveException();
            }
        });

        put(EventList.ELEMENT_HEART_GEM_ATS_TURN.getId(), new TurnEvent() {
            @Override
            public void onTurn(@NonNull Entity self, @NonNull WrappedObject<Entity> attacker) {
                int ats = self.getVariable(Variable.ELEMENT_HEART_GEM_ATS);
                self.addBasicStat(StatType.ATS, ats);

                self.removeVariable(Variable.ELEMENT_HEART_GEM_ATS);
                self.removeEvent(EventList.ELEMENT_HEART_GEM_ATS_END);

                throw new EventRemoveException();
            }
        });

        put(EventList.ELEMENT_HEART_GEM_ATS_END.getId(), new EndFightEvent() {
            @Override
            public void onEndFight(@NonNull Entity self) {
                int ats = self.getVariable(Variable.ELEMENT_HEART_GEM_ATS);
                self.addBasicStat(StatType.ATS, ats);

                self.removeVariable(Variable.ELEMENT_HEART_GEM_ATS);
                self.removeEvent(EventList.ELEMENT_HEART_GEM_ATS_TURN);

                throw new EventRemoveException();
            }
        });

        put(EventList.ELEMENT_HEART_GEM_FIRE_TURN.getId(), new TurnEvent() {
            @Override
            public void onTurn(@NonNull Entity self, @NonNull WrappedObject<Entity> attacker) {
                int turn = self.getVariable(Variable.ELEMENT_HEART_GEM_FIRE);
                int damage = self.getVariable(Variable.ELEMENT_HEART_GEM_FIRE_DAMAGE);
                Entity entity = Objects.requireNonNull(self.getObjectVariable(Variable.ELEMENT_HEART_GEM_FIRE_ENTITY));

                entity.damage(self, 0, damage, 0, false, false, false);

                if(turn == 1) {
                    self.removeVariable(Variable.ELEMENT_HEART_GEM_FIRE);
                    self.removeVariable(Variable.ELEMENT_HEART_GEM_FIRE_DAMAGE);
                    self.removeVariable(Variable.ELEMENT_HEART_GEM_FIRE_ENTITY);

                    self.removeEvent(EventList.ELEMENT_HEART_GEM_FIRE_END);
                    throw new EventRemoveException();
                } else {
                    self.setVariable(Variable.ELEMENT_HEART_GEM_FIRE, turn - 1);
                }
            }
        });

        put(EventList.ELEMENT_HEART_GEM_FIRE_END.getId(), new EndFightEvent() {
            @Override
            public void onEndFight(@NonNull Entity self) {
                self.removeVariable(Variable.ELEMENT_HEART_GEM_FIRE);
                self.removeVariable(Variable.ELEMENT_HEART_GEM_FIRE_DAMAGE);
                self.removeVariable(Variable.ELEMENT_HEART_GEM_FIRE_ENTITY);

                self.removeEvent(EventList.ELEMENT_HEART_GEM_FIRE_TURN);
                throw new EventRemoveException();
            }
        });

        put(EventList.RUBY_EARRING_DAMAGE.getId(), new DamageEvent() {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                 @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                totalDra.multiple(3);

                self.removeEvent(EventList.RUBY_EARRING_END);
                throw new EventRemoveException();
            }
        });

        put(EventList.RUBY_EARRING_END.getId(), new EndFightEvent() {
            @Override
            public void onEndFight(@NonNull Entity self) {
                self.removeEvent(EventList.RUBY_EARRING_DAMAGE);
                throw new EventRemoveException();
            }
        });

        put(EventList.LYCANTHROPE_PAGE_2.getId(), new PreTurnEvent() {
            @Override
            public void onPreTurn(@NonNull Entity self) {
                int maxHp = self.getStat(StatType.MAXHP);
                int hp = self.getStat(StatType.HP);

                if(maxHp / 2 >= hp) {
                    self.addBasicStat(StatType.MAXHP, (int) (maxHp * 0.3));
                    self.addBasicStat(StatType.ATK, self.getStat(StatType.ATK) / 2);
                    self.addBasicStat(StatType.ATS, 300);

                    self.setBasicStat(StatType.HP, (int) (maxHp * 0.65));
                    self.setBasicStat(StatType.AGI, 200);
                    self.setBasicStat(StatType.DEF, 50);
                    self.setBasicStat(StatType.MDEF, 0);
                    self.setBasicStat(StatType.BRE, 200);
                    self.setBasicStat(StatType.DRA, 50);
                    self.setBasicStat(StatType.EVA, 0);
                    self.setBasicStat(StatType.ACC, 200);

                    long fightId = FightManager.getInstance().getFightId(self.getId());
                    Set<Player> playerSet = FightManager.getInstance().getPlayerSet(fightId);

                    Player.replyPlayers(playerSet, self.getFightName() + " (이/가) 분노합니다!");

                    throw new EventRemoveException();
                }
            }
        });

        put(EventList.WOLF_OF_MOON_PAGE_2.getId(), new PreTurnEvent() {
            @Override
            public void onPreTurn(@NonNull Entity self) {
                int maxHp = self.getStat(StatType.MAXHP);
                int hp = self.getStat(StatType.HP);

                if(maxHp * 0.7 >= hp) {
                    Boss boss = (Boss) self;

                    boss.addBasicStat(StatType.MAXHP, maxHp / 8);
                    boss.addBasicStat(StatType.ATK, -50);
                    boss.addBasicStat(StatType.DEF, 100);
                    boss.addBasicStat(StatType.EVA, 100);

                    boss.addEvent(EventList.WOLF_OF_MOON_PAGE_3);
                    boss.addSkill(SkillList.HOWLING_OF_WOLF.getId());
                    boss.setSkillPercent(SkillList.HOWLING_OF_WOLF.getId(), 0.2);

                    long fightId = FightManager.getInstance().getFightId(self.getId());
                    Set<Player> playerSet = FightManager.getInstance().getPlayerSet(fightId);

                    Player.replyPlayers(playerSet, self.getFightName() + " (이/가) 비틀거리기 시작합니다");

                    throw new EventRemoveException();
                }
            }
        });

        put(EventList.WOLF_OF_MOON_PAGE_3.getId(), new PreTurnEvent() {
            @Override
            public void onPreTurn(@NonNull Entity self) {
                int maxHp = self.getStat(StatType.MAXHP);
                int hp = self.getStat(StatType.HP);

                if(maxHp * 0.3 >= hp) {
                    Boss boss = (Boss) self;

                    boss.addBasicStat(StatType.MAXHP, maxHp / 5);
                    boss.addBasicStat(StatType.ATK, 150);
                    boss.addBasicStat(StatType.ATS, 100);
                    boss.addBasicStat(StatType.BRE, 100);
                    boss.addBasicStat(StatType.DRA, 25);

                    boss.setBasicStat(StatType.DEF, 0);
                    boss.setBasicStat(StatType.EVA, 0);

                    boss.addSkill(SkillList.CUTTING_MOONLIGHT.getId());
                    boss.setSkillPercent(SkillList.CUTTING_MOONLIGHT.getId(), 0.5);

                    long fightId = FightManager.getInstance().getFightId(self.getId());
                    Set<Player> playerSet = FightManager.getInstance().getPlayerSet(fightId);

                    for(Player player : playerSet) {
                        int ats = (int) (player.getStat(StatType.ATS) * 0.2);

                        player.setVariable(Variable.WOLF_OF_MOON_ATS, ats);
                        player.addBasicStat(StatType.ATS, -1 * ats);
                        player.addEvent(EventList.WOLF_OF_MOON_END);
                    }

                    Player.replyPlayers(playerSet, self.getFightName() + " (이/가) 분노의 포효를 하며 늑대인간으로 변했습니다!\n" +
                            "이번 전투동안 공격 속도가 20% 감소합니다");

                    throw new EventRemoveException();
                }
            }
        });

        put(EventList.WOLF_OF_MOON_END.getId(), new EndFightEvent() {
            @Override
            public void onEndFight(@NonNull Entity self) {
                int ats = self.getVariable(Variable.WOLF_OF_MOON_ATS);
                self.removeVariable(Variable.WOLF_OF_MOON_ATS);

                self.addBasicStat(StatType.ATS, ats);

                throw new EventRemoveException();
            }
        });
    }};

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T extends Event> T getEvent(long eventId) {
        return (T) Objects.requireNonNull(MAP.get(eventId));
    }

}
