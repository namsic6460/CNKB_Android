package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Entity;

public abstract class Use extends BasicUse {

    @NonNull
    public abstract String use(@NonNull Entity self, @Nullable String other);

}
