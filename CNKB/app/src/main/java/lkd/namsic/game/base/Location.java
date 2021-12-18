package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.exception.NumberRangeException;
import lombok.Getter;

@Getter
public class Location {
    
    public static Location parseLocation(@NonNull String string) {
        String[] split = string.split("-");
        
        return new Location(Integer.parseInt(split[0]), Integer.parseInt(split[1]),
            Integer.parseInt(split[2]), Integer.parseInt(split[3]));
    }
    
    int x = 0;
    int y = 0;
    int fieldX = 1;
    int fieldY = 1;
    
    public Location() {
    }
    
    public Location(int x, int y) {
        this(x, y, false);
    }
    
    public Location(int x, int y, boolean ignore) {
        if(ignore) {
            this.x = x;
            this.y = y;
        } else {
            this.setMap(x, y);
        }
    }
    
    public Location(int x, int y, int fieldX, int fieldY) {
        this(x, y);
        this.setField(fieldX, fieldY);
    }
    
    public Location(@NonNull Location location) {
        this(location.x, location.y, location.fieldX, location.fieldY);
    }
    
    public void setMap(int x, int y) {
        if(x < 0 || x > Config.MAX_MAP_X) {
            throw new NumberRangeException(x, 0, Config.MAX_MAP_X);
        } else if(y < 0 || y > Config.MAX_MAP_Y) {
            throw new NumberRangeException(y, 0, Config.MAX_MAP_Y);
        }
        
        this.x = x;
        this.y = y;
    }
    
    public void setField(int fieldX, int fieldY) {
        if(fieldX < 1 || fieldX > Config.MAX_FIELD_X) {
            throw new NumberRangeException(fieldX, 0, Config.MAX_FIELD_X);
        } else if(fieldY < 1 || fieldY > Config.MAX_FIELD_Y) {
            throw new NumberRangeException(fieldY, 0, Config.MAX_FIELD_Y);
        }
        
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
    public String toMapString() {
        return x + "-" + y;
    }
    
    @NonNull
    public String toFieldString() {
        return fieldX + "-" + fieldY;
    }
    
}
