package lkd.namsic.Game.Event;

public interface DeathEvent {

    boolean onDeath(int beforeDeathHp, int afterDeathHp);

}
