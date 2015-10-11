package loadr;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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
        String[] args = {"-d", "war.war", "-h", "http://airhacks.com", "-l", "list", "-s http://localhost:4949"};
        Map<String, String> argumentMap = App.arrayToMap(args);
        assertNotNull(argumentMap);
        System.out.println("argumentMap = " + argumentMap);
        assertThat(argumentMap.get("-d"), is("war.war"));
        assertThat(argumentMap.get("-h"), is("http://airhacks.com"));
        assertThat(argumentMap.get("-l"), is("list"));
    }

    @Test
    public void extractListArgument() {
        String LIST_ARGUMENT = "-l";
        String[] args = {LIST_ARGUMENT};

        Map<String, String> argumentMap = App.arrayToMap(args);
        assertThat(argumentMap.size(), is(1));
        assertTrue(argumentMap.containsKey("-l"));
        assertTrue(argumentMap.get("-l").isEmpty());
    }

    @Test
    public void extractServerURI() {
        final String expected = "http://localhost:4949";
        Map<String, String> map = new HashMap<>();
        map.put("-s", expected);

        String serverURI = App.extractServerURI(map);
        assertThat(serverURI, is(expected));

        map.clear();

        serverURI = App.extractServerURI(map);
        assertThat(serverURI, is(App.DEFAULT_SERVER_URI));
    }

    @Test
    public void mapToAction() {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("-d", "archive");
        Arguments action = App.argumentsToAction(arguments);
        assertThat(action, is(Arguments.DEPLOY));

        arguments.clear();
        arguments.put("-u", "archive");
        action = App.argumentsToAction(arguments);
        assertThat(action, is(Arguments.UNDEPLOY));

        arguments.clear();
        arguments.put("-l", null);
        action = App.argumentsToAction(arguments);
        assertThat(action, is(Arguments.LIST));

        arguments.clear();
        arguments.put("-UNKNOWN-", null);
        action = App.argumentsToAction(arguments);
        assertThat(action, is(Arguments.USAGE));
    }

}
