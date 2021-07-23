package lkd.namsic.game.exception;

import lkd.namsic.game.base.Location;

public class WeirdDataException extends RuntimeException {

    public WeirdDataException(Location rightLocation, Location weirdLocation) {
        super(rightLocation.toString() + " - " + weirdLocation.toString());
    }

}
