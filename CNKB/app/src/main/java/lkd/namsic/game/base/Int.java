package lkd.namsic.game.base;

public class Int {
    
    private Integer value;
    
    public Int() {
        this.set(0);
    }
    
    public Int(Integer value) {
        this.set(value);
    }
    
    public Integer get() {
        return this.value;
    }
    
    public void set(Integer value) {
        this.value = value;
    }
    
    public void add(int value) {
        this.set(this.get() + value);
    }
    
    public void add(double value) {
        this.set((int) (this.get() + value));
    }
    
    public void multiple(int value) {
        this.set(this.get() * value);
    }
    
    public void multiple(double value) {
        this.set((int) (this.get() * value));
    }
    
    public void divide(int value) {
        this.set(this.get() / value);
    }
    
    public void divide(double value) {
        this.set((int) (this.get() / value));
    }
    
    @Override
    public String toString() {
        return this.value.toString();
    }
    
}
