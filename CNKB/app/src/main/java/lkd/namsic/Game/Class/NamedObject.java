package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import lombok.Getter;
import lombok.Setter;

public class NamedObject implements GameObject {

    @Getter
    @Setter
    @NonNull
    String name;

    public NamedObject(@NonNull String name) {
        this.name = name;
    }

}
