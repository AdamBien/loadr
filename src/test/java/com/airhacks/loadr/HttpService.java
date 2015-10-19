package com.airhacks.loadr;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author airhacks.com
 */
public class HttpService {

    public static HttpServer startServer(String path, Consumer<String> consumer) throws IOException {
        String payload = "+";
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(4221), 0);
        HttpContext context = httpServer.createContext(path);
        context.setHandler((he) -> {
            he.sendResponseHeaders(200, payload.getBytes().length);
            final OutputStream output = he.getResponseBody();
            InputStream input = he.getRequestBody();
            consumer.accept(read(input));
            output.write(payload.getBytes());
            output.flush();
            he.close();
        });
        httpServer.start();
        return httpServer;
    }

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

}
