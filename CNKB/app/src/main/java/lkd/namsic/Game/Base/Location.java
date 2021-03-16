package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;

import lkd.namsic.Game.Config;
import lombok.Getter;

@Getter
public class Location {

    private final LimitInteger x = new LimitInteger(0, Config.MIN_MAP_X, Config.MAX_MAP_X);
    private final LimitInteger y = new LimitInteger(0, Config.MIN_MAP_Y, Config.MAX_MAP_Y);

    public Location() {}

    public Location(int x, int y) {
        this.x.set(x);
        this.y.set(y);
    }

    @NonNull
    @Override
    public String toString() {
        return "x: " + x.get() + ", y: " + y.get();
    }

}
