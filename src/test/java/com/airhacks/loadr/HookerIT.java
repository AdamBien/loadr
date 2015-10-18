package com.airhacks.loadr;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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
    private HttpServer server;
    private String result;

    public void startServer() throws IOException {
        String payload = "+";
        this.server = HttpServer.create(new InetSocketAddress(4221), 0);
        HttpContext context = server.createContext("/test");
        context.setHandler((he) -> {
            he.sendResponseHeaders(200, payload.getBytes().length);
            final OutputStream output = he.getResponseBody();
            InputStream input = he.getRequestBody();
            this.result = read(input);
            System.out.println("result = " + result);
            output.write(payload.getBytes());
            output.flush();
            he.close();
        });
        server.start();
    }

    public static String read(InputStream input) throws IOException {
        try (final InputStreamReader inputStreamReader = new InputStreamReader(input)) {
            try (BufferedReader br = new BufferedReader(inputStreamReader)) {
                return br.lines().collect(Collectors.joining("\n"));
            }
        }
    }

    @Before
    public void initialize() throws IOException {
        this.startServer();
        this.cut = new Hooker("http://localhost:4221/test");
    }

    @Test
    public void performGET() {
        String result = this.cut.invokeGET();
        assertThat(result, not(is("-")));
        System.out.println("result = " + result);
    }

    @Test
    public void performPOST() {
        String response = this.cut.invokePOST();
        assertThat(response, not(is("-")));
        System.out.println("result = " + response);
    }

    @After
    public void stopServer() {
        this.server.stop(0);
    }

}
