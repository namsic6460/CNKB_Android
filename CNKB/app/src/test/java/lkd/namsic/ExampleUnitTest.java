package lkd.namsic;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

public class ExampleUnitTest {

    @Test
    public void evalTest() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    }

    @Test
    public void test() {
        String command = "ab?cd";
        System.out.println(Pattern.quote("?"));
        System.out.println(command.replace("?", ""));
    }

}