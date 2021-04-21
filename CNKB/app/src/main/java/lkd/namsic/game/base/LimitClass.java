package lkd.namsic.game.base;

import androidx.annotation.NonNull;

import java.io.Serializable;

public interface LimitClass<T> extends Serializable {

    @NonNull
    T get();

    void set(@NonNull T t);

}
