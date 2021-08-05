package lkd.namsic.game.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

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

    final Map<StatType, Integer> limitStat = new HashMap<>();

    public Skill(@NonNull String name) {
        super(name);
        this.id.setId(Id.SKILL);
    }

}
