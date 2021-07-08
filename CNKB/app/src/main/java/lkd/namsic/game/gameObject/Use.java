package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

@FunctionalInterface
public interface Use extends Serializable {

    @Nullable
    String use(Entity self, @NonNull List<GameObject> other);

}
