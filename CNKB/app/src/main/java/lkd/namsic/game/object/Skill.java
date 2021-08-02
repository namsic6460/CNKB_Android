package lkd.namsic.game.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

import lkd.namsic.game.base.RangeIntegerMap;
import lkd.namsic.game.base.Use;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Skill extends NamedObject {

    @Setter
    boolean isPassive = false;

    @Nullable
    @Setter
    Use use = null;

    boolean isWaitSkill = false;

    final RangeIntegerMap<StatType> limitStat = new RangeIntegerMap<>(
            new HashMap<>(), new HashMap<>(), StatType.class
    );

    public Skill(@NonNull String name) {
        super(name);
        this.id.setId(Id.SKILL);
    }

}
