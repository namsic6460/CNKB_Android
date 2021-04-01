package lkd.namsic.Game.Exception;

import lkd.namsic.Game.Enum.Id;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(Id id, long objectId) {
        super(id.toString() + "-" + objectId);
    }

    public ObjectNotFoundException(String string) {
        super(string);
    }

}
