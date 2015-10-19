package com.airhacks.loadr;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class HookerIT {

    Hooker cut;
    private String result;
    private HttpServer server;

    @Before
    public void initialize() throws IOException {
        this.server = HttpService.startServer("/test", () -> "+", this::setResult);
        this.cut = new Hooker("http://localhost:4221/test");
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Test
    public void performGET() {
        String response = this.cut.invokeGET();
        assertThat(response, is("+"));
    }

    @Test
    public void performPOST() {
        String response = this.cut.invokePOST();
        assertThat(response, is("+"));
        System.out.println("result = " + response);
    }

    @After
    public void stopServer() {
        this.server.stop(0);
    }

}
