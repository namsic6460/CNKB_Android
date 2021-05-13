package lkd.namsic.game.base;

import java.util.HashMap;

import lkd.namsic.game.Config;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.gameObject.Player;
import lombok.Getter;

@Getter
public class ChatLimit {

    final RangeInteger limitLv = new RangeInteger(Config.MIN_LV, Config.MAX_LV);
    final RangeIntegerMap<Long> limitCloseRate = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>()
            new HashMap<>(), new HashMap<>(), Long.class
    );
    final RangeIntegerMap<StatType> limitStat = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>()
            new HashMap<>(), new HashMap<>(), StatType.class
    );
    final RangeIntegerMap<Long> limitQuest = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>()
            new HashMap<>(), new HashMap<>(), Long.class
    );

    public boolean isAvailable(Player player) {
        return this.getLimitLv().isInRange(player.getLv().get()) &&
                this.getLimitCloseRate().isInRange(player.getCloseRate()) &&
                player.compareStat(this.limitStat.getMin(), true) &&
                player.compareStat(this.limitStat.getMax(), false) &&
                this.limitQuest.isInRange(player.getClearedQuest());
    }

}
