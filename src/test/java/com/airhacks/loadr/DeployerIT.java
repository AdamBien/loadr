package com.airhacks.loadr;

import java.util.Set;
import static junit.framework.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class DeployerIT {

    private String SERVER_LOCATION = "http://localhost:4848";
    private static final String ARCHIVE = "/Users/abien/work/workspaces/fishloader/FishLoader/test-deployment/coffeebeans.war";
    Deployer cut;

    @Before
    public void initialize() {
        this.cut = new Deployer(SERVER_LOCATION);
    }

    @Test
    public void deploy() {
        boolean success = this.cut.deploy(ARCHIVE);
        assertTrue(success);
        success = this.cut.deploy(ARCHIVE);
        assertTrue(success);
    }

    @Test
    public void applications() {
        Set<Application> applications = this.cut.applications();
        org.junit.Assert.assertNotNull(applications);
        boolean found = false;
        for (Application application : applications) {
            System.out.println(application);
            if (application.getName().equalsIgnoreCase("coffeebeans")) {
                found = true;
            }
        }
        assertTrue(found);
    }

}
