package lkd.namsic.game.object.implement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.base.EquipUse;
import lkd.namsic.game.base.SkillUse;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.EventList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Player;

public class EquipUses {

    public final static Map<Long, EquipUse> MAP = new HashMap<Long, EquipUse>() {{
        put(EquipList.MIX_SWORD_1.getId(), new EquipUse(0, 10) {
            @Override
            @NonNull
            public String use(@NonNull Entity self, @Nullable String other) {
                int lostHp = self.getStat(StatType.MAXHP) - self.getStat(StatType.HP);
                int heal = (int) (lostHp * 0.3);

                self.addBasicStat(StatType.HP, heal);
                return "마나 10을 사용하여 체력을 " + heal + "만큼 회복했습니다";
            }
        });

        put(EquipList.GHOST_SWORD_1.getId(), new EquipUse(0, 0.06, 0, 0, 0, 0) {
            @Override
            @NonNull
            public String use(@NonNull Entity self, @Nullable String other) {
                self.setVariable(Variable.GHOST_SWORD_1, true);
                return "최대 체력의 6%를 소모하여 다음 공격을 강화했습니다";
            }
        });

        put(EquipList.LOW_ALLOY_CHESTPLATE.getId(), new EquipUse(0, 4) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                if(Math.random() < 0.75) {
                    self.setVariable(Variable.IS_DEFENCING, true);
                    return "방어 태세로 전환했습니다";
                } else {
                    return "방어 태세로의 전환에 실패했습니다";
                }
            }
        });

        put(EquipList.LOW_ALLOY_LEGGINGS.getId(), new EquipUse(0, 5) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addBuff(System.currentTimeMillis() + 20000L, StatType.EVA, 15);
                return "20초간 회피 15의 버프를 획득하였습니다";
            }
        });

        put(EquipList.SLIME_CHESTPLATE.getId(), new EquipUse(0, 5) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.setVariable(Variable.SLIME_CHESTPLATE_USE, true);

                int saved = self.getVariable(Variable.SLIME_CHESTPLATE);
                return "다음 공격이 " + saved + " 만큼의 추가 데미지를 입힙니다";
            }
        });

        put(EquipList.SLIME_LEGGINGS.getId(), new EquipUse(0, 10) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                Long fightId = FightManager.getInstance().fightId.get(self.getId());
                if(fightId == null) {
                    throw new WeirdCommandException("전투중에만 사용이 가능합니다");
                }
                
                Set<Entity> entitySet = FightManager.getInstance().getEntitySet(fightId);
                Set<Player> playerSet = FightManager.getInstance().getPlayerSet(fightId);

                for(Entity entity : entitySet) {
                    entity.addBuff(System.currentTimeMillis() + 30000L, StatType.ATS, -20);
                }

                Player.replyPlayers(playerSet, self.getName() +
                        " 이 슬라임 바지 아이템을 사용하여 30초간 공격속도를 20 감소시켰습니다");
                return "모든 적의 공격속도를 20 감소시켰습니다";
            }
        });

        put(EquipList.TROLL_CLUB.getId(), new EquipUse(0, 2) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.setVariable(Variable.TROLL_CLUB_USE, true);
                return "다음 공격을 강화했습니다";
            }
        });
        
        put(EquipList.DEMON_STAFF.getId(), new EquipUse(0, 0.1, 0, 0, 0, 0) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addBuff(System.currentTimeMillis() + 60000, StatType.MATK, 8);
                self.addBuff(System.currentTimeMillis() + 60000, StatType.MBRE, 8);
                self.addBasicStat(StatType.MN, 4);
                return "힘이 빠져나가고 또 다른 힘이 들어왔습니다";
            }
        });

        put(EquipList.GHOST_SWORD_2.getId(), new EquipUse(0, 0.06, 0, 0, 0, 0) {
            @Override
            @NonNull
            public String use(@NonNull Entity self, @Nullable String other) {
                self.setVariable(Variable.GHOST_SWORD_2, true);
                return "최대 체력의 6%를 소모하여 다음 공격을 강화했습니다";
            }
        });

        put(EquipList.SILVER_CHESTPLATE.getId(), new EquipUse(0, 0) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addEvent(EventList.SILVER_CHESTPLATE_PRE_DAMAGED);
                self.addEvent(EventList.SILVER_CHESTPLATE_END);
                
                return "다음 피해에 은 갑옷의 효과가 발동됩니다";
            }
        });

        put(EquipList.SILVER_SWORD.getId(), new EquipUse(0, 5) {
            @Override
            public int getMinTargetCount() {
                return 1;
            }

            @Override
            public int getMaxTargetCount() {
                return 1;
            }

            @Nullable
            @Override
            public String checkOther(@NonNull Entity self, @NonNull String... other) {
                if(!Doing.fightList().contains(self.getDoing())) {
                    return "이 장비는 전투중에만 사용이 가능합니다";
                }
                
                return new SkillUse(0, 0) {
                    @Override
                    public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {}
                }.checkOther(self, other);
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                long fightId = FightManager.getInstance().getFightId(self.getId());
                Map<Integer, Entity> targetMap = FightManager.getInstance().getTargetMap(fightId);

                int targetIndex = Integer.parseInt(other);
                Entity target = targetMap.get(targetIndex);

                self.damage(target, 0, self.getStat(StatType.MATK), 0, true, true, true);

                target.addEvent(EventList.SILVER_SWORD_DAMAGE);
                target.addEvent(EventList.SILVER_SWORD_END);

                return "대상의 다음 회복량을 감소시켰습니다";
            }
        });

        put(EquipList.SILVER_CHESTPLATE.getId(), new EquipUse(0, 3) {
            @Override
            public int getMinTargetCount() {
                return 0;
            }

            @Override
            public int getMaxTargetCount() {
                return 0;
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addEvent(EventList.SILVER_CHESTPLATE_PRE_DAMAGED);
                self.addEvent(EventList.SILVER_CHESTPLATE_END);
                
                return "다음 마법 피해를 포함한 공격에 반사 및 데미지 감소 효과가 부여되었습니다";
            }
        });

        put(EquipList.MIX_SWORD_2.getId(), new EquipUse(0, 10) {
            @Override
            @NonNull
            public String use(@NonNull Entity self, @Nullable String other) {
                int lostHp = self.getStat(StatType.MAXHP) - self.getStat(StatType.HP);
                int heal = lostHp / 2;

                self.addBasicStat(StatType.HP, heal);
                return "마나 10을 사용하여 체력을 " + heal + "만큼 회복했습니다";
            }
        });

        put(EquipList.LOW_ALLOY_CHESTPLATE.getId(), new EquipUse(0, 5) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                if(Math.random() < 0.85) {
                    self.setVariable(Variable.IS_DEFENCING, true);
                    return "방어 태세로 전환했습니다";
                } else {
                    return "방어 태세로의 전환에 실패했습니다";
                }
            }
        });

        put(EquipList.LOW_ALLOY_LEGGINGS.getId(), new EquipUse(0, 5) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addBuff(System.currentTimeMillis() + 30000L, StatType.EVA, 60);
                return "20초간 회피 15의 버프를 획득하였습니다";
            }
        });

        put(EquipList.HARDENED_SLIME_CHESTPLATE.getId(), new EquipUse(0, 5) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.setVariable(Variable.HARDENED_SLIME_CHESTPLATE_USE, true);

                int saved = self.getVariable(Variable.HARDENED_SLIME_CHESTPLATE);
                return "다음 공격이 " + saved + " 만큼의 추가 데미지를 입힙니다";
            }
        });

        put(EquipList.HARDENED_SLIME_LEGGINGS.getId(), new EquipUse(0, 10) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                Long fightId = FightManager.getInstance().fightId.get(self.getId());
                if(fightId == null) {
                    throw new WeirdCommandException("전투중에만 사용이 가능합니다");
                }

                Set<Entity> entitySet = FightManager.getInstance().getEntitySet(fightId);
                Set<Player> playerSet = FightManager.getInstance().getPlayerSet(fightId);

                for(Entity entity : entitySet) {
                    entity.addBuff(System.currentTimeMillis() + 30000L, StatType.ATS, -60);
                }

                Player.replyPlayers(playerSet, self.getName() +
                        " 이 굳은 슬라임 바지 아이템을 사용하여 30초간 공격속도를 60 감소시켰습니다");
                return "모든 적의 공격속도를 60 감소시켰습니다";
            }
        });

        put(EquipList.RUBY_EARRING.getId(), new EquipUse(0, 10) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addEvent(EventList.RUBY_EARRING_DAMAGE);
                self.addEvent(EventList.RUBY_EARRING_END);
                
                return "다음 공격의 회복량이 3배로 적용됩니다";
            }
        });

        put(EquipList.LYCANTHROPE_CHESTPLATE.getId(), new EquipUse(0, 3) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                double percent = ((double) self.getStat(StatType.HP)) / self.getStat(StatType.MAXHP);
                int stat = (int) ((1 - percent) * 100);

                long time = System.currentTimeMillis() + 60000;
                self.addBuff(time, StatType.ATK, stat);
                self.addBuff(time, StatType.MATK, stat);

                return "공격력과 마법 공격력 버프를 " + stat + " 만큼 받았습니다";
            }
        });
    }};

}
