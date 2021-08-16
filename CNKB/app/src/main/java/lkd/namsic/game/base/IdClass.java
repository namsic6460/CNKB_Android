package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import lkd.namsic.game.enums.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IdClass {

    Id id = null;
    long objectId = 0L;

    public IdClass() {}

    public IdClass(@NonNull Id id, long objectId) {
        this.id = id;
        this.objectId = objectId;
    }

    @Override
    public int hashCode() {
        return (id.hashCode() + "_" + objectId).hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof IdClass) {
            IdClass idClass = (IdClass) obj;
            return Objects.equals(idClass.id, this.id) && idClass.objectId == this.objectId;
        } else {
            return false;
        }
    }

}
