package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MonsterType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.UnhandledEnumException;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Monster extends AiEntity {

    @Setter
    @NonNull
    MonsterType type = MonsterType.BAD;

    @Setter
    boolean enableRandomLv = true;

    final Set<Long> angryPlayers = new ConcurrentHashSet<>();

    public Monster(@NonNull String name) {
        super(name);
        this.id.setId(Id.MONSTER);
    }

    public void randomLevel() {
        if(!enableRandomLv) {
            return;
        }

        int prevLv = this.lv.get();
        int newLv = prevLv + ((int) Math.round(Math.random() * prevLv) - prevLv / 2);
        this.lv.set(newLv);

        double statIncrease = 0.2 * (newLv - prevLv);
        for(StatType statType : StatType.values()) {
            try {
                Config.checkStatType(statType);
            } catch (UnhandledEnumException ignore) {}

            this.addBasicStat(statType, Math.max((int) (this.getBasicStat(statType) * statIncrease), 0));
        }

        this.setBasicStat(StatType.HP, this.getStat(StatType.MAXHP));
        this.setBasicStat(StatType.MN, this.getStat(StatType.MAXMN));
    }

    public Object[] onTurn(Set<Player> players) {
        Object[] output = new Object[2];

        Object[] playerArray;
        Random random = new Random();
        if(type.equals(MonsterType.BAD)) {
            playerArray = players.toArray();
            output[0] = playerArray[random.nextInt(playerArray.length)];
        } else if(type.equals(MonsterType.MIDDLE)) {
            Set<Player> retainSet = new HashSet<>();
            for(Player player : players) {
                if(this.angryPlayers.contains(player.getId().getObjectId())) {
                    retainSet.add(player);
                }
            }

            playerArray = retainSet.toArray();
            if(playerArray.length != 0) {
                output[0] = playerArray[random.nextInt(playerArray.length)];
            } else {
                output[0] = null;
            }
        } else {
            return new Object[] { null, 0 };
        }

        //TODO: 스킬 id
        output[1] = 0;

        return output;
    }

}
