package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lombok.Getter;

@Getter
public class Location {

    int x = 0;
    int y = 0;
    int fieldX = 1;
    int fieldY = 1;

    public Location() {}

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location(int x, int y, int fieldX, int fieldY) {
        this(x, y);
        this.fieldX = fieldX;
        this.fieldY = fieldY;
    }

    public Location(@NonNull Location location) {
        this(location.x, location.y, location.fieldX, location.fieldY);
    }

    public void setMap(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setField(int fieldX, int fieldY) {
        this.fieldX = fieldX;
        this.fieldY = fieldY;
    }

    public void set(int x, int y, int fieldX, int fieldY) {
        this.setMap(x, y);
        this.setField(fieldX, fieldY);
    }

    public void set(Location location) {
        this.set(location.getX(), location.getY(), location.getFieldX(), location.getFieldY());
    }

    public boolean equalsMap(@NonNull Location location) {
        return this.x == location.x && this.y == location.y;
    }

    public boolean equalsField(@NonNull Location location) {
        return this.fieldX == location.fieldX && this.fieldY == location.fieldY;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null) {
            return false;
        }

        if(obj instanceof Location) {
            Location location = (Location) obj;
            return this.equalsMap(location) && this.equalsField(location);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return this.toMapString() + "-" + this.toFieldString();
    }

    @NonNull
    public String toMapString() { return x + "-" + y; }

    @NonNull
    public String toFieldString() { return fieldX + "-" + fieldY; }

}
