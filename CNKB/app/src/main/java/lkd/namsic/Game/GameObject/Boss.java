package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;

import lkd.namsic.Game.Enum.Id;
import lombok.ToString;

@ToString
public class Boss extends AiEntity {

    public Boss(@NonNull String name) {
        super(name);
        this.id.setId(Id.BOSS);
    }

}
