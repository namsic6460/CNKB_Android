package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.object.Player;
import lombok.Getter;

@Getter
public class ChatLimit {

    final RangeInteger limitLv = new RangeInteger(1, Config.MAX_LV);
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

    final Map<Long, Integer> clearedQuest = new HashMap<>();
    final Set<Long> notClearedQuest = new HashSet<>();

    final RangeInteger limitHour1 = new RangeInteger(0, 23);
    final RangeInteger limitHour2 = new RangeInteger(24, 24);

    public boolean isAvailable(@NonNull Player player) {
        boolean flag = this.getLimitLv().isInRange(player.getLv()) &&
                this.limitCloseRate.isInRange(player.getCloseRate()) &&
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

            questSet = player.getClearedQuest().keySet();
            for(long questId : this.notClearedQuest) {
                if(questSet.contains(questId)) {
                    return false;
                }
            }

            LocalDateTime time = LocalDateTime.now();
            flag = (this.limitHour1.isInRange(time.getHour()) || this.limitHour2.isInRange(time.getHour())) &&
                    Config.compareMap(player.getClearedQuest(), this.clearedQuest,true, false, 0);
        }

        return flag;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof ChatLimit) {
            ChatLimit chatLimit = (ChatLimit) obj;

            return this.limitLv.equals(chatLimit.limitLv) &&
                    this.limitCloseRate.equals(chatLimit.limitCloseRate) &&
                    this.limitStat.equals(chatLimit.limitStat) &&
                    this.limitQuest.equals(chatLimit.limitQuest) &&
                    this.runningQuest.equals(chatLimit.runningQuest) &&
                    this.notRunningQuest.equals(chatLimit.notRunningQuest) &&
                    this.clearedQuest.equals(chatLimit.clearedQuest) &&
                    this.notClearedQuest.equals(chatLimit.notClearedQuest) &&
                    this.limitHour1.equals(chatLimit.limitHour1) &&
                    this.limitHour2.equals(chatLimit.limitHour2);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return ("" + this.limitLv.hashCode() + this.limitCloseRate.hashCode() +
                this.limitStat.hashCode() + this.limitQuest.hashCode() +
                this.runningQuest.hashCode() + this.notRunningQuest.hashCode() +
                this.clearedQuest.hashCode() + this.notClearedQuest.hashCode() +
                this.limitHour1.hashCode() + this.limitHour2.hashCode()).hashCode();
    }

}
