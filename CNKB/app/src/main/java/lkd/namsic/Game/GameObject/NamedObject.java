package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;

import lombok.Getter;
import lombok.Setter;

public class NamedObject extends GameObject {

    @Getter
    @Setter
    @NonNull
    String name;

    public NamedObject(@NonNull String name) {
        this.name = name;
    }

}
