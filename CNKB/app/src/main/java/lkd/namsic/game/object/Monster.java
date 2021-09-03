package lkd.namsic.game.object;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.SkillUse;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.FightWaitType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MonsterType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.MonsterList;
import lkd.namsic.game.exception.InvalidNumberException;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.manager.FightManager;
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

    final Map<Long, Double> skillPercent = new HashMap<>();

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

        this.location = null;
    }

    public void randomLevel() {
        if(!enableRandomLv) {
            return;
        }

        int prevLv = this.lv;
        int newLv = Math.max(prevLv + (new Random().nextInt(17) - 8), 1);
        this.lv = newLv;

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

    public void setSkillPercent(long skillId, double percent) {
        if(percent < 0 || percent > 1) {
            throw new NumberRangeException(percent, 0, 1);
        }

        if(!this.skill.contains(skillId)) {
            throw new InvalidNumberException(skillId);
        }

        this.skillPercent.put(skillId, percent);
    }

    public double getSkillPercent(long skillId) {
        return this.skillPercent.getOrDefault(skillId, 0D);
    }

    public Object[] onTurn(Set<Player> players, Set<Player> exceptSet) {
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

        if(output[0].equals(FightWaitType.ATTACK)) {
            for(long skillId : this.skill) {
                if(this.getSkillPercent(skillId) >= random.nextDouble()) {
                    Skill skill = Config.getData(Id.SKILL, skillId);
                    SkillUse use = Objects.requireNonNull(skill.getSkillUse());

                    long fightId = FightManager.getInstance().getFightId(this.id);
                    Map<Integer, Entity> targetMap = FightManager.getInstance().getTargetMap(fightId);

                    int playerCount = targetMap.size();
                    if(playerCount < use.getMinTargetCount()) {
                        continue;
                    }

                    StringBuilder otherBuilder;

                    int useMaxTargetCount = use.getMaxTargetCount();
                    if(useMaxTargetCount > 0) {
                        otherBuilder = new StringBuilder();

                        int count = 1;
                        int otherCount = random.nextInt(Math.min(useMaxTargetCount, playerCount)) + 1;

                        Entity value;
                        for (Map.Entry<Integer, Entity> entry : targetMap.entrySet()) {
                            value = entry.getValue();

                            if (value.getId().getId().equals(Id.PLAYER)) {
                                exceptSet.add((Player) entry.getValue());
                            }

                            otherBuilder.append(entry.getKey())
                                    .append(",");

                            if (count++ == otherCount) {
                                break;
                            }
                        }
                    } else {
                        otherBuilder = new StringBuilder(",");
                    }

                    String other = otherBuilder.toString();
                    other = other.substring(0, other.length() - 1);

                    this.setVariable(Variable.FIGHT_TARGET_MAX_INDEX, playerCount);

                    try {
                        use.checkUse(this, other);
                    } catch (WeirdCommandException e) {
                        continue;
                    }

                    output[0] = FightWaitType.SKILL;
                    this.setVariable(Variable.FIGHT_OBJECT_ID, skillId);
                    this.setVariable(Variable.FIGHT_OTHER, other);

                    break;
                }
            }
        }

        return output;
    }

    @NonNull
    @Override
    public String getName() {
        return "[" + this.type.getDisplayName() + "] " + super.getName();
    }

}
