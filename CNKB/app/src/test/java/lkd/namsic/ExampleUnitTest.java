package lkd.namsic;

import org.junit.Test;

import lkd.namsic.game.base.Location;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Location location1 = new Location(0, 0);
        Location location2 = new Location(0, 0);
        Location location3 = new Location(0, 1);

        System.out.println(location1.equalsMap(location2));
        System.out.println(location1.equalsMap(location3));
    }
}