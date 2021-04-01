package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Base.ConcurrentArrayList;
import lkd.namsic.Game.Base.ConcurrentHashSet;
import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Enum.Doing;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Event.Event;
import lombok.ToString;

@ToString
public class Monster extends AiEntity {

    public Monster(@NonNull String name) {
        super(name);

        this.id.setId(Id.MONSTER);
    }

}
