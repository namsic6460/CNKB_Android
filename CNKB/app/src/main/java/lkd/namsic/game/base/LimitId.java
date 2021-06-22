package lkd.namsic.game.base;

import androidx.annotation.NonNull;

import java.util.Objects;

import lkd.namsic.game.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;

public class LimitId implements LimitClass<Long> {

    @Getter
    @NonNull
    private final Id id;

    private long value;

    public LimitId(long value, @NonNull Id id) {
        this.id = id;
        this.set(value);
    }

    @NonNull
    @Override
    public Long get() {
        return this.value;
    }

    @Override
    public void set(@NonNull Long setValue) {
        long maxValue = Objects.requireNonNull(Config.ID_COUNT.get(id));

        if(setValue < 0 || value >= maxValue) {
            throw new NumberRangeException(setValue, this);
        }

        this.value = setValue;
    }

    @NonNull
    @Override
    public String toString() {
        return "Value: " + this.value + "L, Id: " + this.id;
    }
}
