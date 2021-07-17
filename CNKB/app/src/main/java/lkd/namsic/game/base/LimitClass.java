package lkd.namsic.game.base;

import androidx.annotation.NonNull;

public interface LimitClass<T> {

    @NonNull
    T get();

    void set(@NonNull T t);

}
