package lkd.namsic.game.base;

public class LoNg {

    private Long value;

    public LoNg() {
        this.set(0L);
    }

    public LoNg(Long value) {
        this.set(value);
    }

    public Long get() {
        return this.value;
    }

    public void set(Long value) {
        this.value = value;
    }

    public void add(long value) {
        this.set(this.get() + value);
    }

    public void add(double value) {
        this.set((long) (this.get() + value));
    }

    public void multiple(long value) {
        this.set(this.get() * value);
    }

    public void multiple(double value) {
        this.set((long) (this.get() * value));
    }

    public void divide(long value) {
        this.set(this.get() / value);
    }

    public void divide(double value) {
        this.set((long) (this.get() / value));
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

}
