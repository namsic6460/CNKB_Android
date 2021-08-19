package lkd.namsic.game.object;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.enums.FightWaitType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MonsterType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.MonsterList;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Monster extends AiEntity {

    final long originalId;

    @Setter
    @NonNull
    MonsterType type = MonsterType.BAD;

    @Setter
    boolean enableRandomLv = true;

    final Set<Long> angryPlayers = new ConcurrentHashSet<>();

    protected Monster(@NonNull String name, long originalId) {
        super(name);

        this.originalId = originalId;
        this.id.setObjectId(originalId);
    }

    public Monster(@NonNull MonsterList monsterData) {
        super(monsterData.getDisplayName());
        this.id.setId(Id.MONSTER);

        this.id.setObjectId(monsterData.getId());
        this.originalId = monsterData.getId();
    }

    public void randomLevel() {
        if(!enableRandomLv) {
            return;
        }

        int prevLv = this.lv.get();
        int newLv = Math.max(prevLv + (new Random().nextInt(17) - 8), 1);
        this.lv.set(newLv);

        double statIncrease = 0.1 * (newLv - prevLv);
        if(statIncrease < 0) {
            statIncrease /= 2;
        }

        for(StatType statType : StatType.values()) {
            this.addBasicStat(statType, (int) (this.getBasicStat(statType) * statIncrease));
        }

        this.setBasicStat(StatType.HP, this.getStat(StatType.MAXHP));
        this.setBasicStat(StatType.MN, this.getStat(StatType.MAXMN));
    }

    public Object[] onTurn(Set<Player> players) {
        Object[] output = new Object[2];

        Object[] playerArray;
        Random random = new Random();
        if(type.equals(MonsterType.BAD)) {
            output[0] = FightWaitType.ATTACK;

            playerArray = players.toArray();
            output[1] = playerArray[random.nextInt(playerArray.length)];
        } else if(type.equals(MonsterType.MIDDLE)) {
            Set<Player> duplicateSet = new HashSet<>();
            for(Player player : players) {
                if(this.angryPlayers.contains(player.getId().getObjectId())) {
                    duplicateSet.add(player);
                }
            }

            playerArray = duplicateSet.toArray();
            if(playerArray.length != 0) {
                output[0] = FightWaitType.ATTACK;
                output[1] = playerArray[random.nextInt(playerArray.length)];
            } else {
                output[0] = FightWaitType.WAIT;
            }
        } else {
            output[0] = FightWaitType.WAIT;
        }

        return output;
    }

    @NonNull
    @Override
    public String getName() {
        return "[" + this.type.getDisplayName() + "] " + super.getName();
    }

}
