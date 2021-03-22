package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.Game.Base.RangeIntegerMap;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Skill implements GameObject {

    @Setter
    @NonNull
    String name;

    @Setter
    boolean isPassive;

    @Nullable
    @Setter
    Use use;

    //Only use on getting
    RangeIntegerMap<StatType> limitStat = new RangeIntegerMap<>(
            new HashMap<StatType, Integer>(), new HashMap<StatType, Integer>()
    );

    public Skill(@NonNull String name, boolean isPassive, @Nullable Use use,
                 @NonNull Map<StatType, Integer> minLimitStat, @NonNull Map<StatType, Integer> maxLimitStat) {
        this.id.setId(Id.SKILL);

        this.name = name;
        this.isPassive = isPassive;
        this.use = use;
        this.limitStat.set(minLimitStat, maxLimitStat);
    }

}
