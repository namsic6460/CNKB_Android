package lkd.namsic.game.object;

import androidx.annotation.NonNull;

import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.object.BossList;

public class Boss extends Monster {

    public Boss(@NonNull BossList bossData) {
        super(bossData.getDisplayName(), bossData.getId());
        this.id.setId(Id.BOSS);
    }

}
