package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;

import java.util.List;

@FunctionalInterface
public interface Use {

    void use(Entity user, @NonNull List<Entity> other);

}
