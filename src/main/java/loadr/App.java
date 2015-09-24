package loadr;

import com.airhacks.loadr.Application;
import com.airhacks.loadr.Deployer;
import java.util.Set;

/**
 *
 * @author airhacks.com
 */
public class App {

    public static void main(String[] args) {
        String server = "http://localhost:4848";
        String archive = null;
        if (args == null || args.length < 1) {
            usage();
            return;
        }
        String action = args[0];

        if (args.length == 3) {
            server = args[1];
            archive = args[2];
        }
        if (args.length == 2) {
            archive = args[1];
        }
        if (args.length == 1) {
            server = args[1];
        }

        switch (action) {
            case "-l":
                list(server);
                break;
            case "-d":
                deploy(server, archive);
                break;
            case "-u":
                undeploy(server, archive);
                break;
        }
    }

    static void deploy(String server, String archive) {
        Deployer deployer = new Deployer(server);
        deployer.deploy(archive);
        Set<Application> applications = deployer.applications();
        list(applications);
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

    static void usage() {
        System.out.println("loadr.App [-d|-u|-l] http://[ADMIN_SERVER_HOST] PATH_TO_WAR");
    }

    static void undeploy(String server, String archive) {
        Deployer deployer = new Deployer(server);
        deployer.undeploy(extractApplicationName(archive));
        list(deployer.applications());

    }

    static void list(String server) {
        Deployer deployer = new Deployer(server);
        Set<Application> applications = deployer.applications();
        list(applications);
    }

    static void list(Set<Application> applications) {

        System.out.println("------------------");
        for (Application application : applications) {
            System.out.println(application.getName() + " -> " + application.getUri());
        }
        System.out.println("------------------");
    }

}
