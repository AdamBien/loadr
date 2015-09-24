package com.airhacks.loadr;

import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class DeployIT {

    @Test
    public void mainWithTwoArguments() {
        String args[] = {"http://localhost:4848", "./test-deployment/coffeebeans.war"};
        Deploy.main(args);
    }

    @Test
    public void mainWithSingleArgument() {
        String args[] = {"./test-deployment/coffeebeans.war"};
        Deploy.main(args);
    }

}
