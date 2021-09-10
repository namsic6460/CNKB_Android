package lkd.namsic.game.object;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.object.BossList;
import lombok.Getter;

@Getter
public class Boss extends Monster {

    @NonNull
    final List<String> spawnMsg;

    public Boss(@NonNull BossList bossData, @NonNull String...spawnMsg) {
        super(bossData.getDisplayName(), bossData.getId());
        this.id.setId(Id.BOSS);

        this.location = null;
        this.spawnMsg = Arrays.asList(spawnMsg);
    }

    @Override
    public void onKill(@NonNull Entity entity) {}

}
