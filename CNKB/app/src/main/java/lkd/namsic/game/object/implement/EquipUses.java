package lkd.namsic.game.object.implement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.base.EquipUse;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Player;

public class EquipUses {

    public final static Map<Long, EquipUse> MAP = new HashMap<Long, EquipUse>() {{
        put(EquipList.MIX_SWORD.getId(), new EquipUse(0, 10) {
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

                Player.replyPlayers(playerSet, self.getName() + " 이 슬라임 바지 아이템을 사용하여 공격속도를 30 감소시켰습니다");
                return "모든 적의 공격속도를 30 감소시켰습니다";
            }
        });

        put(EquipList.TROLL_CLUB.getId(), new EquipUse(0, 1) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.setVariable(Variable.TROLL_CLUB, true);
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
    }};

}
