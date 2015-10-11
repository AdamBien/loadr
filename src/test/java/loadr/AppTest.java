package loadr;

import java.io.PrintStream;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

/**
 *
 * @author airhacks.com
 */
public class AppTest {

    @Test
    public void extractApplicationName() {
        String expected = "duke";
        String actual = App.extractApplicationName(" ./test-deployment/" + expected + ".war");
        assertThat(actual, is(expected));
    }

    @Test
    public void extractApplicationNameWithoutEnding() {
        String expected = "duke";
        String actual = App.extractApplicationName(expected);
        assertThat(actual, is(expected));
    }

    @Test
    public void archiveSingleArgument() {
        PrintStream stream = Mockito.mock(PrintStream.class);
        System.setOut(stream);
        App.main(new String[]{"something.war"});
        verify(stream).println(Matchers.argThat(startsWith("loadr")));
    }

    @Test
    public void arrayToMapEvenArgumentsNumber() {
        String[] args = {"-d", "war.war", "-h", "http://airhacks.com", "-l", "list"};
        Map<String, String> argumentMap = App.arrayToMap(args);
        assertNotNull(argumentMap);
        System.out.println("argumentMap = " + argumentMap);
        assertThat(argumentMap.get("-d"), is("war.war"));
        assertThat(argumentMap.get("-h"), is("http://airhacks.com"));
        assertThat(argumentMap.get("-l"), is("list"));
        assertThat(argumentMap.size(), is(args.length));
    }
}
