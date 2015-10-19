package loadr;

import com.airhacks.loadr.HttpService;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.PrintStream;
import static org.hamcrest.CoreMatchers.startsWith;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

/**
 *
 * @author airhacks.com
 */
public class AppIT {

    private String result;
    private HttpServer server;

    @Before
    public void initHttpServer() throws IOException {
        this.server = HttpService.startServer("/test", () -> "+", this::setResult);

    }

    public void setResult(String result) {
        this.result = result;
    }

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
    public void deployToDefaultServerWithGetHook() throws IOException {
        String args[] = {"-d", "./test-deployment/coffeebeans.war", "-h", "http://localhost:4221/test"};
        App.main(args);
        assertTrue(result.isEmpty());
    }

    @Test
    public void deployToDefaultServerWithPostHook() throws IOException {
        String args[] = {"-d", "./test-deployment/coffeebeans.war", "-h", "http://localhost:4221/test", "-post"};
        App.main(args);
        assertTrue(result.isEmpty());
    }

    @Test
    public void invokeWithoutArguments() {
        PrintStream stream = Mockito.mock(PrintStream.class);
        System.setOut(stream);
        String args[] = {""};
        App.main(args);
        verify(stream).println(Matchers.argThat(startsWith("loadr")));
    }

    @After
    public void stopServer() {
        this.server.stop(0);
        this.result = "-not used-";
    }

}
