package loadr;

import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class AppIT {

    @Test
    public void mainWithTwoArguments() {
        String args[] = {"-s", "http://localhost:4848", "-d", "./test-deployment/coffeebeans.war"};
        App.main(args);
    }

    @Test
    public void deployToDefaultServer() {
        String args[] = {"-d", "./test-deployment/coffeebeans.war"};
        App.main(args);
    }

}
