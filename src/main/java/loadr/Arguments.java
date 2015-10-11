package loadr;

/**
 *
 * @author airhacks.com
 */
enum Arguments {

    DEPLOY("-d"),
    UNDEPLOY("-u"),
    LIST("-l"),
    HOOK("-h"),
    SERVER("-s"),
    USAGE("-u");

    private final String name;

    Arguments(String name) {
        this.name = name;
    }

    public String argumentName() {
        return name;
    }
}
