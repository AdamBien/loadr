package loadr;

import com.airhacks.loadr.Application;
import com.airhacks.loadr.Deployer;
import com.airhacks.loadr.Hooker;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author airhacks.com
 */
public class App {

    static final String DEPLOY_ARGUMENT = "-d";
    static final String UNDEPLOY_ARGUMENT = "-u";
    static final String LIST_ARGUMENT = "-l";
    static final String HOOK_ARGUMENT = "-h";

    enum Action {
        DEPLOY, UNDEPLOY, LIST, USAGE;
    }

    public static void main(String[] args) {
        String server = "http://localhost:4848";
        String archive = null;
        if (args == null || args.length <= 1) {
            usage();
            return;
        }
        Map<String, String> arguments = arrayToMap(args);
        Action action = argumentsToAction(arguments);
        switch (action) {
            case LIST:
                list(server);
                break;
            case DEPLOY:
                deploy(server, archive);
                break;
            case UNDEPLOY:
                undeploy(server, archive);
                break;
            case USAGE:
                usage();
        }
        String uri = arguments.get(HOOK_ARGUMENT);
        if (uri != null) {
            performHook(uri);
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

    static Action argumentsToAction(Map<String, String> arguments) {
        if (arguments.containsKey(DEPLOY_ARGUMENT)) {
            return Action.DEPLOY;
        }
        if (arguments.containsKey(UNDEPLOY_ARGUMENT)) {
            return Action.UNDEPLOY;
        }
        if (arguments.containsKey(LIST_ARGUMENT)) {
            return Action.LIST;
        }
        return Action.USAGE;
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

    static void performHook(String uri) {
        Hooker hooker = new Hooker(uri);
        String result = hooker.invoke();
        System.out.println("------------");
        System.out.println(result);
        System.out.println("------------");
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
        System.out.println("-h: perform a HTTP GET request to the URI");
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
