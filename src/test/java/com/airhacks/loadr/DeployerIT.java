package com.airhacks.loadr;

import java.util.Set;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class DeployerIT {

    private static final String ARCHIVE = "/Users/abien/work/workspaces/fishloader/FishLoader/test-deployment/coffeebeans.war";
    private static final String EXISTING_APP_NAME = "coffeebeans";
    private static final String SERVER_LOCATION = "http://localhost:4848";
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
        deploy();
        Set<Application> applications = this.cut.applications();
        org.junit.Assert.assertNotNull(applications);
        boolean found = false;
        for (Application application : applications) {
            System.out.println(application);
            if (application.getName().equalsIgnoreCase(EXISTING_APP_NAME)) {
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void applicationsWithEmptyServer() {
        this.cut.undeployAll();
        Set<Application> applications = this.cut.applications();
        System.out.println("--- " + applications);
        assertTrue(applications.isEmpty());
    }

    @Test
    public void undeployNotExisting() {
        boolean success = this.cut.undeploy("doesNotExistApp");
        assertFalse(success);
    }

    @Test
    public void undeployExisting() {
        deploy();
        boolean success = this.cut.undeploy(EXISTING_APP_NAME);
        assertTrue(success);
    }

}
