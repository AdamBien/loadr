package loadr;

import com.airhacks.loadr.Application;
import com.airhacks.loadr.Deployer;
import com.airhacks.loadr.Hooker;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author airhacks.com
 */
public class App {

    static final String DEFAULT_SERVER_URI = "http://localhost:4848";

    public static void main(String[] args) {
        String archive;
        if (args == null || args.length < 1) {
            usage();
            return;
        }
        Map<String, String> arguments = arrayToMap(args);
        String server = extractServerURI(arguments);

        Arguments action = argumentsToAction(arguments);

        switch (action) {
            case LIST:
                list(server);
                break;
            case DEPLOY:
                archive = arguments.get(Arguments.DEPLOY.argumentName());
                deploy(server, archive);
                break;
            case UNDEPLOY:
                archive = arguments.get(Arguments.UNDEPLOY.argumentName());
                undeploy(server, archive);
                break;
            case USAGE:
                usage();
        }
        String uri = arguments.get(Arguments.HOOK.argumentName());
        boolean isPost = arguments.containsKey(Arguments.POST.argumentName());
        if (uri != null) {
            if (isPost) {
                performPostHook(uri);
            } else {
                performGetHook(uri);
            }
        }
    }

    static Map<String, String> arrayToMap(String args[]) {
        Map<String, String> arguments = new HashMap<>();

        Optional<String> list = Arrays.stream(args).
                filter(a -> a.equals(Arguments.LIST.argumentName())).
                findFirst();
        if (list.isPresent()) {
            arguments.put(list.get(), "");
        }
        Optional<String> post = Arrays.stream(args).
                filter(a -> a.equals(Arguments.POST.argumentName())).
                findFirst();

        if (post.isPresent()) {
            arguments.put(post.get(), "");
        }

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].startsWith("-")) {
                arguments.put(args[i], args[i + 1]);
            }
        }
        return arguments;
    }

    static Arguments argumentsToAction(Map<String, String> arguments) {
        if (arguments.containsKey(Arguments.DEPLOY.argumentName())) {
            return Arguments.DEPLOY;
        }
        if (arguments.containsKey(Arguments.UNDEPLOY.argumentName())) {
            return Arguments.UNDEPLOY;
        }
        if (arguments.containsKey(Arguments.LIST.argumentName())) {
            return Arguments.LIST;
        }
        return Arguments.USAGE;
    }

    static boolean deploy(String server, String archive) {
        Deployer deployer = new Deployer(server);
        boolean success = deployer.deploy(archive);
        Set<Application> applications = deployer.applications();
        list(applications);
        String appName = extractApplicationName(archive);
        System.out.println("To undeploy use: java -jar loadr.jar -s " + server + " -u " + appName);
        return success;
    }

    static void performPostHook(String uri) {
        Hooker hooker = new Hooker(uri);
        String result = hooker.invokePOST();
        System.out.println("------------");
        System.out.println(result);
        System.out.println("------------");
    }

    static void performGetHook(String uri) {
        Hooker hooker = new Hooker(uri);
        String result = hooker.invokeGET();
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
        System.out.println("loadr.App -s [SERVER_NAME] [-d|-u|-l] PATH_TO_WAR [-h HTTP_GET_URI]");
        System.out.println("-s: sever uri, default is localhost:4848");
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

    static String extractServerURI(Map<String, String> arguments) {
        return arguments.getOrDefault(Arguments.SERVER.argumentName(), DEFAULT_SERVER_URI);
    }

}
