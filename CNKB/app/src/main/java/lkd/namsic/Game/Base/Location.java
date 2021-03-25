package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;

import java.io.Serializable;

import lkd.namsic.Game.Config;
import lombok.Getter;

@Getter
public class Location implements Serializable {

    private final LimitInteger x = new LimitInteger(Config.MIN_MAP_X, Config.MIN_MAP_X, Config.MAX_MAP_X);
    private final LimitInteger y = new LimitInteger(Config.MIN_MAP_Y, Config.MIN_MAP_Y, Config.MAX_MAP_Y);
    private final LimitInteger fieldX = new LimitInteger(Config.MIN_FIELD_X, Config.MIN_FIELD_X, Config.MAX_FIELD_X);
    private final LimitInteger fieldY = new LimitInteger(Config.MIN_FIELD_Y, Config.MIN_FIELD_Y, Config.MAX_FIELD_Y);

    public Location() {}

    public Location(int x, int y, int fieldX, int fieldY) {
        this.x.set(x);
        this.y.set(y);
        this.fieldX.set(fieldX);
        this.fieldY.set(fieldY);
    }

    @NonNull
    @Override
    public String toString() {
        return "x: " + x.get() + ", y: " + y.get() + ", fieldX: " + fieldX.get() + ", fieldY: " + fieldY.get();
    }

}
