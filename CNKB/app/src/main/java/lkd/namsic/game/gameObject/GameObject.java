package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.game.base.IdClass;
import lombok.Getter;

public abstract class GameObject implements Cloneable {

    @Getter
    final IdClass id = new IdClass();

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if(super.equals(obj)) {
            return true;
        } else if(obj instanceof GameObject) {
            GameObject gameObject = (GameObject) obj;
            return this.id.equals(gameObject.id);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
