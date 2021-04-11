package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;

import lkd.namsic.Game.Base.IdClass;
import lombok.Getter;

public abstract class GameObject implements Cloneable {

    @Getter
    IdClass id = new IdClass();

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    public GameObject newObject() {
        try {
            return (GameObject) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
