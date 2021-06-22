package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.List;

@FunctionalInterface
public interface Use {

    void use(Entity self, @NonNull List<GameObject> other);

}
