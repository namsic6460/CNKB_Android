package lkd.namsic.Game.Exception;

import lkd.namsic.Game.Base.Location;

public class WeirdDataException extends RuntimeException {

    public WeirdDataException(Location rightLocation, Location weirdLocation) {
        super(rightLocation.toString() + " - " + weirdLocation.toString());
    }

}
