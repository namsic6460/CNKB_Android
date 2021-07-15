package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Map;

public abstract class Event implements Serializable {

    public Event(int activeCount) {
        this(activeCount, null);
    }

    public Event(int activeCount, @Nullable Map<String, Object> variable) {
        this.activeCount = activeCount;
        this.variable = variable;
    }

    public int activeCount;

    @Nullable
    public final Map<String, Object> variable;

    @NonNull
    public abstract String getClassName();

}
