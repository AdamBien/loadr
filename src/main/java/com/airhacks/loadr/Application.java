package com.airhacks.loadr;

/**
 *
 * @author adam-bien.com
 */
public class Application {

    private String name;
    private String uri;

    public Application(String name, String uri) {
        this.uri = uri;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "Application{" + "name=" + name + ", uri=" + uri + '}';
    }
}
