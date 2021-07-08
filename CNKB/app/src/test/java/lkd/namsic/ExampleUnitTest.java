package lkd.namsic;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.base.Location;

public class ExampleUnitTest {

    @Test
    public void evalTest() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        System.out.println("n eval Player.getMethod(\"setBuffStat\", StatType, int).invoke(self,StatType.getDeclaredMethod(\"valueOf\", String).invoke(null, \"ATK\"), toInt(\"0\")))"
                .replaceAll("toInt\\(",
                "Double.getMethod(\"intValue\").invoke(Double.getDeclaredMethod(\"valueOf\", String).invoke(null, "));
    }

    @Test
    public void test() {
        Location loc1 = new Location(1, 2, 3, 4);
        Location loc2 = new Location(1, 2, 4, 4);
        Location loc3 = new Location(1, 2, 3, 4);

        System.out.println(loc1.equalsMap(loc2));
        System.out.println(loc1.equalsField(loc2));
        System.out.println(loc1.equals(loc2));
        System.out.println(loc1.equals(loc3));

        Map<Location, Integer> map = new ConcurrentHashMap<>();
        map.put(loc1, 1);
        System.out.println(map.get(loc3));

        System.out.println("1234".hashCode() + " " + "4321".hashCode() + " " + "1234".hashCode());
    }

}