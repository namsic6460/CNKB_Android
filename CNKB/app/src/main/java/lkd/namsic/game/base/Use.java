package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Entity;

@FunctionalInterface
public interface Use {

    @NonNull
    String use(@NonNull Entity self, @Nullable String other);

    static void checkOther(@Nullable String other) {
        if(other == null) {
            throw new WeirdCommandException("사용 대상이 필요합니다");
        }
    }

}
