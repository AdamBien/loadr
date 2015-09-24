package com.airhacks.loadr;

import java.util.Set;

/**
 *
 * @author airhacks.com
 */
public class Deploy {

    public static void main(String[] args) {
        String server = "http://localhost:4848";
        String archive = null;
        if (args != null && args.length == 2) {
            server = args[0];
            archive = args[1];
        }
        if (args != null && args.length == 1) {
            archive = args[0];
        }
        Deployer deployer = new Deployer(server);
        deployer.deploy(archive);
        Set<Application> applications = deployer.applications();
        for (Application application : applications) {
            System.out.println("- " + application.getName());
        }
        String appName = extractApplicationName(archive);
        System.out.println("To undeploy use: com.airhacks.loadr.Undeploy " + server + " " + appName);
    }

    public static String extractApplicationName(String archive) {
        int dotIndex = archive.lastIndexOf(".");
        String archiveWithoutEnding = archive;
        if (dotIndex != -1) {
            archiveWithoutEnding = archive.substring(0, dotIndex);
        }

        int slashIndex = archiveWithoutEnding.lastIndexOf("/");
        return archiveWithoutEnding.substring(slashIndex + 1);
    }
}
