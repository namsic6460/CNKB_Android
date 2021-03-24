package lkd.namsic.Game.Exception;

import java.util.Collection;

import lkd.namsic.Game.Config;

public class CollectionAddFailedException extends RuntimeException {

    public CollectionAddFailedException(String message) {
        super(message);
    }

    public CollectionAddFailedException(String message, Collection<?> collection) {
        super(message + " - " + Config.collectionToString(collection));
    }

}
