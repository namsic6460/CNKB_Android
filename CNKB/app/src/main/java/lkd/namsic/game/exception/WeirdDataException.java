package lkd.namsic.game.exception;

import lkd.namsic.game.base.Location;
import lkd.namsic.game.enums.Id;

public class WeirdDataException extends RuntimeException {

    public WeirdDataException(Location rightLocation, Location weirdLocation) {
        super(rightLocation.toString() + " - " + weirdLocation.toString());
    }

    public WeirdDataException(Id id, long objectId) {
        super(id.toString() + "-" + objectId);
    }

}
