package lkd.namsic.game.object.implement;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lkd.namsic.game.base.Bool;
import lkd.namsic.game.base.Int;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EventList;
import lkd.namsic.game.event.DamagedEvent;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.exception.EventRemoveException;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Player;

public class EntityEvents {

    private final static Map<Long, Event> EVENT_MAP = new HashMap<Long, Event>() {{
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
                    self.addBasicStat(StatType.BRE, 10);
                } else {
                    self.setVariable(Variable.ENT_WAIT_TURN, waitTurn - 1);

                    if(attacker.getId().getId().equals(Id.PLAYER)) {
                        ((Player) attacker).replyPlayer("마수의 기운이 깨어나고 있습니다");
                    }

                    throw new EventRemoveException();
                }
            }
        });
    }};

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T extends Event> T getEvent(long eventId) {
        return (T) Objects.requireNonNull(EVENT_MAP.get(eventId));
    }

}
