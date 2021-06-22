package lkd.namsic;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;

public class ExampleUnitTest {

    @Test
    public void evalTest() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Config.class.getDeclaredMethod("getData", Id.class, long.class).invoke(null, Id.PLAYER, 1);
    }

    @Test
    public void test() {
        String[] split = "15-24-34-21".split("-");
        for(int i = 0; i < 4; i++) {
            System.out.println(split[i]);
        }
    }

}