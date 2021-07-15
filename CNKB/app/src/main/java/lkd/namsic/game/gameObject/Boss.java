package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import lkd.namsic.game.enums.Id;

public class Boss extends Monster {

    public Boss(@NonNull String name) {
        super(name);
        this.id.setId(Id.BOSS);
    }

}
