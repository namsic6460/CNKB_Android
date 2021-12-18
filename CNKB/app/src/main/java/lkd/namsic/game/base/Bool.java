package lkd.namsic.game.base;

public class Bool {
    
    private Boolean value;
    
    public Bool() {
        this.set(false);
    }
    
    public Bool(Boolean value) {
        this.set(value);
    }
    
    public Boolean get() {
        return this.value;
    }
    
    public void set(Boolean value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return this.value.toString();
    }
    
}
