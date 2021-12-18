package lkd.namsic.game.base;

import androidx.annotation.NonNull;

public class WrappedObject<T> {
    
    T object;
    
    public WrappedObject(@NonNull T object) {
        this.object = object;
    }
    
    @NonNull
    public T get() {
        return object;
    }
    
    public void set(@NonNull T object) {
        this.object = object;
    }
    
}
