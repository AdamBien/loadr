loadr
=====

##GlassFish Embeddable Deployment Utility

The core functionality of loadr is implemented within the `com.airhacks.loadr.Deployer`class. 
Deployer conveniently wraps the GlassFish v3 and v4 management API:


```java

public class WorkflowIT {

    private static final String ARCHIVE = "./test-deployment/coffeebeans.war";
    private static final String APP_NAME = "coffeebeans";
    private static final String SERVER_LOCATION = "http://localhost:4848";
    private Deployer cut;

    @Before
    public void initialize() {
        this.cut = new Deployer(SERVER_LOCATION);
    }

    @Test
    public void deployCheckListUndeploy() {
        boolean deployed = this.cut.deploy(ARCHIVE);
        assertTrue(deployed);
        deployed = this.cut.isDeployed(APP_NAME);
        assertTrue(deployed);
        Set<Application> applications = this.cut.applications();
        assertNotNull(applications);
        boolean found = false;
        for (Application application : applications) {
            if (APP_NAME.equals(application.getName())) {
                found = true;
            }
        }
        assertTrue(found);
        boolean success = this.cut.undeploy(APP_NAME);
        assertTrue(success);

        deployed = this.cut.isDeployed(APP_NAME);
        assertFalse(deployed);

        success = this.cut.undeploy("NOT-EXISTING");
        assertFalse(success);

        this.cut.undeployAll();
        deployed = this.cut.isDeployed(APP_NAME);
        assertFalse(deployed);
    }
}

```

