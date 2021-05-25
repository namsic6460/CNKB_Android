package lkd.namsic;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() {
        String value = "abc*-/+=%52#$<script>으악 ㅁㄴㅇㄹ 와\\ 샌즈\nanc</scr\r\nipt>시작      끝";
        String regex = "[^A-Za-z-_0-9ㄱ-ㅎㅏ-ㅣ가-힣\\s]|[\r]|[\n]";

        String newValue = value.replaceAll(regex, "-")
                .replaceAll("[ ]{2,}", "- ")
                .trim();

        System.out.println("Equal: " + value.equals(newValue));
        System.out.println(value);
        System.out.println(newValue);
    }
}