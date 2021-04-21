package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import lkd.namsic.game.enums.Id;
import lombok.ToString;

@ToString
public class Boss extends AiEntity {

    public Boss(@NonNull String name) {
        super(name);
        this.id.setId(Id.BOSS);
    }

}
