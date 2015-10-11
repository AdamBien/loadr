package loadr;

import com.airhacks.loadr.Application;
import com.airhacks.loadr.Deployer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 * @author airhacks.com
 */
public class App {

    public static void main(String[] args) {
        String server = "http://localhost:4848";
        String archive = null;
        if (args == null || args.length <= 1) {
            usage();
            return;
        }

        String action = null;
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

    static Map<String, String> arrayToMap(String args[]) {
        Map<String, String> arguments = new HashMap<>();
        for (int i = 0; i < args.length - 1; i++) {
            if (i % 2 == 0) {
                arguments.put(args[i], args[i + 1]);
            }
        }
        return arguments;
    }

    static void perform(BiFunction<String, String, Boolean> task, String server, String archive, Function<Void, Void> notification) {
        Boolean result = task.apply(server, archive);
        if (result) {
            notification.apply(null);
        }
    }

    static boolean deploy(String server, String archive) {
        Deployer deployer = new Deployer(server);
        boolean success = deployer.deploy(archive);
        Set<Application> applications = deployer.applications();
        list(applications);
        String appName = extractApplicationName(archive);
        System.out.println("To undeploy use: java -jar loadr.jar -u " + server + " " + appName);
        return success;
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
        System.out.println("-l: list deployed applications");
        System.out.println("-d: deploy an application");
        System.out.println("-u: undeploy an application");
    }

    static boolean undeploy(String server, String archive) {
        Deployer deployer = new Deployer(server);
        boolean success = deployer.undeploy(extractApplicationName(archive));
        list(deployer.applications());
        return success;
    }

    static void list(String server) {
        Deployer deployer = new Deployer(server);
        Set<Application> applications = deployer.applications();
        list(applications);
    }

    static void list(Set<Application> applications) {

        System.out.println("------------------");
        if (applications.isEmpty()) {
            System.out.println("no deployed applications");
            return;
        }
        for (Application application : applications) {
            System.out.println(application.getName() + " -> " + application.getUri());
        }
        System.out.println("------------------");
    }

}
