package lkd.namsic.game.object.implement;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lkd.namsic.game.base.Bool;
import lkd.namsic.game.base.IdClass;
import lkd.namsic.game.base.Int;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EventList;
import lkd.namsic.game.event.DamagedEvent;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.event.PreDamageEvent;
import lkd.namsic.game.event.StartFightEvent;
import lkd.namsic.game.exception.EventRemoveException;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

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
                    Logger.i("NamsicDebug", ((int) (physicDmg.get() * 0.05)) + "");
                    magicDmg.add((int) (physicDmg.get() * 0.05));
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
