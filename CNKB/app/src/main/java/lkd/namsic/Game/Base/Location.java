package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;

import lkd.namsic.Game.Config;
import lombok.Getter;

@Getter
public class Location {

    private final LimitInteger x = new LimitInteger(0, Config.MIN_MAP_X, true, Config.MAX_MAP_X, true);
    private final LimitInteger y = new LimitInteger(0, Config.MIN_MAP_Y, true, Config.MAX_MAP_Y, true);

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
