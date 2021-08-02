package lkd.namsic.game.base;

import androidx.annotation.NonNull;

import java.util.List;

import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.GameObject;

@FunctionalInterface
public interface Use {

    @NonNull
    String use(@NonNull Entity self, @NonNull List<GameObject> other);

}
