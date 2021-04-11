package lkd.namsic.Game.Exception;

import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Enum.Id;

public class WeirdDataException extends RuntimeException {

    public WeirdDataException(Location rightLocation, Location weirdLocation) {
        super(rightLocation.toString() + " - " + weirdLocation.toString());
    }

    public WeirdDataException(Id id, long objectId) {
        super(id.toString() + "-" + objectId);
    }

}
