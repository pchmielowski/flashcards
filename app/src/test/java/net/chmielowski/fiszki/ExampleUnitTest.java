package net.chmielowski.fiszki;

import com.annimon.stream.Stream;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        Stream.of(DictionaryUtils.getLesson(DictionaryUtils.Lang.GREEK, 3, realm)).forEach(System.out::println);
    }
}