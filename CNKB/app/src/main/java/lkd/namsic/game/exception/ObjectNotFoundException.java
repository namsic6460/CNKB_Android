package lkd.namsic.game.exception;

import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(Id id, long objectId) {
        super(id.toString() + "-" + objectId);
    }

    public ObjectNotFoundException(String string) {
        super(string);
    }

    public ObjectNotFoundException(EquipType equipType) { super(equipType.toString()); }

}
