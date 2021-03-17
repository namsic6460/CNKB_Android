package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.io.Serializable;

public interface GameClass extends Serializable {

    long id = 0;

    @NonNull
    String getPath();

}
