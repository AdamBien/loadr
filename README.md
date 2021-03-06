loadr
=====

[![Loadr In Action](https://i.ytimg.com/vi/A3-13avaG0M/mqdefault.jpg)](https://www.youtube.com/embed/A3-13avaG0M?rel=0)

## Using WebHooks For Command Chaining

[![Using WebHooks](https://i.ytimg.com/vi/leznAGoCeNo/mqdefault.jpg)](https://www.youtube.com/embed/leznAGoCeNo?rel=0)

## Command Line Interface


### List applications

```
java -jar loadr.jar -l
```

or

```
java -jar loadr.jar -s http://[admin-server]:[port] loadr.jar -l
```

Output:

```
------------------
no deployed applications
```


### Deployment

```
java -jar loadr.jar -d ../test-deployment/coffeebeans.war
```

Output:

```
------------------
coffeebeans -> "http://localhost:4848/management/domain/applications/application/coffeebeans"
------------------
To undeploy use: java -jar loadr.jar -u http://localhost:4848 coffeebeans
```

### Undeployment

```
java -jar loadr.jar -u coffeebeans
```

Output:

```
------------------
no deployed applications
```

### Deployment to a configurable server

```
java -jar loadr.jar -s http://localhost:4242 -d ../test-deployment/coffeebeans.war
```

### Deployment with callback

```
java -jar loadr.jar -d ../test-deployment/coffeebeans.war -h http://ci-host/?action=perform-system-tests
```

### Deployment with POST callback

```
java -jar loadr.jar -d ../test-deployment/coffeebeans.war -h http://ci-host/?action=perform-system-tests -post
```


##Payara (GlassFish) Embeddable Deployment Utility

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

