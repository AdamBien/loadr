package loadr;

import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class AppIT {

    @Test
    public void mainWithTwoArguments() {
        String args[] = {"http://localhost:4848", "./test-deployment/coffeebeans.war"};
        App.main(args);
    }

    @Test
    public void mainWithSingleArgument() {
        String args[] = {"./test-deployment/coffeebeans.war"};
        App.main(args);
    }

}
