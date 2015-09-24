package com.airhacks.loadr;

import java.util.Set;

/**
 *
 * @author airhacks.com
 */
public class Deploy {

    public static void main(String[] args) {
        String server = "http://localhost:8080";
        String archive = null;
        if (args != null && args.length == 2) {
            server = args[0];
            archive = args[1];
        }
        Deployer deployer = new Deployer(server);
        deployer.deploy(archive);
        Set<Application> applications = deployer.applications();
        for (Application application : applications) {
            System.out.println("- " + application);
        }
        String appName = extractApplicationName(archive);
        System.out.println("To undeploy use: com.airhacks.loadr.Undeploy " + server + " " + appName);
    }

    public static String extractApplicationName(String archive) {
        int lastIndexOf = archive.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return archive;
        }
        return archive.substring(0, lastIndexOf);
    }
}
