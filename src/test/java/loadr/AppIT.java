package loadr;

import java.io.PrintStream;
import static org.hamcrest.CoreMatchers.startsWith;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

/**
 *
 * @author airhacks.com
 */
public class AppIT {

    @Test
    public void deployWithConfiguredServer() {
        String args[] = {"-s", "http://localhost:4848", "-d", "./test-deployment/coffeebeans.war"};
        App.main(args);
    }

    @Test
    public void listAppsFromConfiguredServer() {
        String args[] = {"-s", "http://localhost:4848", "-l"};
        App.main(args);
    }

    @Test
    public void deployToDefaultServer() {
        String args[] = {"-d", "./test-deployment/coffeebeans.war"};
        App.main(args);
    }

    @Test
    public void invokeWithoutArguments() {
        PrintStream stream = Mockito.mock(PrintStream.class);
        System.setOut(stream);
        String args[] = {""};
        App.main(args);
        verify(stream).println(Matchers.argThat(startsWith("loadr")));
    }

}
