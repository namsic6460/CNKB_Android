package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Entity;
import lombok.Getter;

@Getter
public abstract class SkillUse extends LimitUse {

    public SkillUse(int useHp, int useMn) {
        this(useHp, 0, 0, useMn, 0, 0);
    }

    public SkillUse(int useHp, double useHpTotal, double useHpCurrent, int useMn, double useMnTotal, double useMnCurrent) {
        super(useHp, useHpTotal, useHpCurrent, useMn, useMnTotal, useMnCurrent);
    }

    public int getWaitTurn() {
        return 0;
    }

    @Override
    public int getMinTargetCount() {
        return 1;
    }

    @Override
    public int getMaxTargetCount() {
        return 1;
    }

    @NonNull
    @Override
    public String getUseName() {
        return "스킬을";
    }

    @Nullable
    @Override
    public String checkOther(@NonNull Entity self, @NonNull String...other) {
        try {
            int maxIndex = self.getVariable(Variable.FIGHT_TARGET_MAX_INDEX);
            int index;
            for (String string : other) {
                index = Integer.parseInt(string);

                if(index < 1 || index > maxIndex) {
                    return "대상의 번호를 정확하게 입력해주세요";
                }
            }
        } catch (NumberFormatException e) {
            return "대상은 숫자여야합니다";
        }
        
        return null;
    }

    @NonNull
    @Override
    public String use(@NonNull Entity self, @Nullable String other) {
        List<Entity> targetList = new ArrayList<>();

        long fightId = FightManager.getInstance().getFightId(self.getId());
        Map<Integer, Entity> targetMap = FightManager.getInstance().getTargetMap(fightId);

        StringBuilder builder = new StringBuilder();
        String output;

        if(other != null) {
            int index;
            Entity target;

            String[] split = other.split(",");
            for (String string : split) {
                index = Integer.parseInt(string);
                target = Objects.requireNonNull(targetMap.get(index));

                targetList.add(target);
                builder.append(target.getFightName())
                        .append(", ");
            }

            if(split.length == 1) {
                output = targetList.get(0).getFightName();
            } else {
                output = targetList.toString();
            }
        } else {
            output = self.getName();
        }

        if(this.getWaitTurn() == 0) {
            this.useSkill(self, targetList);
        } else {
            FightManager.getInstance().castingMap.put(self, targetList);

            List<Entity> castedList;
            for(Entity target : targetList) {
                castedList = FightManager.getInstance().castedMap.get(target);

                if(castedList == null) {
                    castedList = new ArrayList<>();
                    castedList.add(self);
                    FightManager.getInstance().castedMap.put(target, castedList);
                } else {
                    castedList.add(self);
                }
            }
        }

        return output;
    }

    public abstract void useSkill(@NonNull Entity self, @NonNull List<Entity> targets);

}
