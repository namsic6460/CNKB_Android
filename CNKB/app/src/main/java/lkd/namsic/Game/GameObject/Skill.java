package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

import lkd.namsic.Game.Base.RangeIntegerMap;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Skill extends NamedObject {

    @Setter
    boolean isPassive = false;

    @Nullable
    @Setter
    Use use = null;

    //Only use on getting
    RangeIntegerMap<StatType> limitStat = new RangeIntegerMap<>(new HashMap<>(), new HashMap<>());

    public Skill(@NonNull String name) {
        super(name);
        this.id.setId(Id.SKILL);
    }

}
