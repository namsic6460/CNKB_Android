package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import lkd.namsic.game.base.IdClass;
import lombok.Getter;

public abstract class GameObject implements Cloneable {

    @Getter
    final
    IdClass id = new IdClass();

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T extends GameObject> T newObject() {
        try {
            return (T) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
