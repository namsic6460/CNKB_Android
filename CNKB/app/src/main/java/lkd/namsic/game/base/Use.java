package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

import lkd.namsic.game.gameObject.Entity;
import lkd.namsic.game.gameObject.GameObject;

@FunctionalInterface
public interface Use extends Serializable {

    @Nullable
    String use(@NonNull Entity self, @NonNull List<GameObject> other);

}
