package lkd.namsic.Game.GameObject;

import java.util.List;

@FunctionalInterface
public interface Use {

    void use(Entity user, List<Entity> other);

}
