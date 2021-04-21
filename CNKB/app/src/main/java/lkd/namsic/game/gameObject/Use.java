package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.List;

@FunctionalInterface
public interface Use {

    void use(Entity user, @NonNull List<GameObject> other);

}
