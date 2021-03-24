package lkd.namsic.Game.Exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String path) {
        super(path);
    }

}
