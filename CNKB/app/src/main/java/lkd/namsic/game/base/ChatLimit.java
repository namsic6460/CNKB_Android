package lkd.namsic.game.base;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import lkd.namsic.game.Config;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.gameObject.Player;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ChatLimit {

    final RangeInteger limitLv = new RangeInteger(Config.MIN_LV, Config.MAX_LV);
    final RangeIntegerMap<Long> limitCloseRate = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>(), Long.class
    );
    final RangeIntegerMap<StatType> limitStat = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>(), StatType.class
    );
    final RangeIntegerMap<Long> limitQuest = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>(), Long.class
    );

    final Set<Long> runningQuest = new HashSet<>();
    final Set<Long> notRunningQuest = new HashSet<>();

    RangeInteger limitHour = new RangeInteger(0, 23);

    public boolean isAvailable(Player player) {
        boolean flag = this.getLimitLv().isInRange(player.getLv().get()) &&
                this.getLimitCloseRate().isInRange(player.getCloseRate()) &&
                player.compareStat(this.limitStat.getMin(), true) &&
                player.compareStat(this.limitStat.getMax(), false) &&
                this.limitQuest.isInRange(player.getClearedQuest());

        if(flag) {
            Set<Long> questSet = player.getQuest().keySet();

            for(long questId : this.runningQuest) {
                if(!questSet.contains(questId)) {
                    return false;
                }
            }

            for(long questId : this.notRunningQuest) {
                if(questSet.contains(questId)) {
                    return false;
                }
            }

            LocalDateTime time = LocalDateTime.now();
            flag = this.limitHour.isInRange(time.getHour());
        }

        return flag;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof ChatLimit) {
            ChatLimit chatLimit = (ChatLimit) obj;

            return this.limitLv.equals(chatLimit.getLimitLv()) &&
                    this.limitCloseRate.equals(chatLimit.getLimitCloseRate()) &&
                    this.limitStat.equals(chatLimit.getLimitStat()) &&
                    this.limitQuest.equals(chatLimit.getLimitQuest());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
