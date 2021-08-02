package lkd.namsic.game.object.interfaces;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.EquipUse;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.GameObject;
import lkd.namsic.game.object.Player;

public class EquipUses {

    public final static Map<Long, EquipUse> EQUIP_USE_MAP = new HashMap<Long, EquipUse>() {{
        put(EquipList.MIX_SWORD.getId(), new EquipUse(0, 10) {
            @Override
            @NonNull
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                int lostHp = self.getStat(StatType.MAXHP) - self.getStat(StatType.HP);
                int heal = (int) (lostHp * 0.1);

                self.addBasicStat(StatType.HP, heal);
                return "마나 10을 사용하여 체력을 " + lostHp + "만큼 회복했습니다";
            }
        });

        put(EquipList.GHOST_SWORD_1.getId(), new EquipUse(0, 0.06, 0, 0, 0, 0) {
            @Override
            @NonNull
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                self.setVariable(Variable.GHOST_SWORD_1, true);
                return "최대 체력의 6%를 소모하여 다음 공격을 강화했습니다";
            }
        });

        put(EquipList.LOW_ALLOY_CHESTPLATE.getId(), new EquipUse(0, 4) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
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
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                self.addBuff(System.currentTimeMillis() + 20000L, StatType.EVA, 15);
                return "20초간 회피 15의 버프를 획득하였습니다";
            }
        });

        put(EquipList.SLIME_CHESTPLATE.getId(), new EquipUse(0, 5) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                self.setVariable(Variable.SLIME_CHESTPLATE_USE, true);

                int saved = self.getVariable(Variable.SLIME_CHESTPLATE);
                return "다음 공격이 " + saved + " 만큼의 추가 데미지를 입힙니다";
            }
        });

        put(EquipList.SLIME_LEGGINGS.getId(), new EquipUse(0, 10) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                Id id;
                Entity entity;
                Set<Player> playerSet = new HashSet<>();
                for(Map.Entry<Id, ConcurrentHashSet<Long>> entry : self.getEnemy().entrySet()) {
                    id = entry.getKey();

                    for(long objectId : entry.getValue()) {
                        entity = Config.getData(id, objectId);

                        if(id.equals(Id.PLAYER)) {
                            playerSet.add((Player) entity);
                        }

                        entity.addBuff(System.currentTimeMillis() + 30000L, StatType.ATS, -25);
                    }
                }

                Player.replyPlayers(playerSet, self.getName() + " 이 슬라임 바지 아이템을 사용하여 모든 적의 공격속도를 25 감소시켰습니다");
                return "모든 적의 공격속도를 25 감소시켰습니다";
            }
        });

        put(EquipList.TROLL_CLUB.getId(), new EquipUse(0, 1) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                self.setVariable(Variable.TROLL_CLUB, true);
                return "다음 공격을 강화했습니다";
            }
        });

    }};

}
