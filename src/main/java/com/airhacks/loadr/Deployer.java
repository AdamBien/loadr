package com.airhacks.loadr;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

public class Deployer {

    public static final String MANAGEMENT_URI = "/management/domain/applications/application";
    private final Client client;
    private final WebTarget applicationTarget;

    public Deployer(String serverLocation) {
        this.client = ClientBuilder.newClient().register(MultiPartFeature.class);
        this.applicationTarget = client.target(serverLocation + MANAGEMENT_URI);
    }

    public boolean deploy(String archiveFile) {
        FileDataBodyPart archive = new FileDataBodyPart("id", new File(archiveFile));
        MultiPart form = new FormDataMultiPart().
                field("force", "true").bodyPart(archive);
        Response response = applicationTarget.request("application/json").
                header("X-Requested-By", "loadr").
                post(Entity.entity(form, form.getMediaType()));
        return (response.getStatus() == 200);
    }

    public boolean isDeployed(String appName) {
        Response response = this.applicationTarget.path(appName).request().get();
        return response.getStatus() == 200;
    }

    public boolean undeploy(String applicationName) {
        Response response = this.applicationTarget.path(applicationName).
                request(MediaType.APPLICATION_JSON).
                header("X-Requested-By", "loadr").
                delete();
        return response.getStatus() == 200;
    }

    public void undeployAll() {
        Set<Application> applications = applications();
        for (Application application : applications) {
            undeploy(application.getName());
        }
    }

    public Set<Application> applications() {
        Set<Application> retVal = new HashSet<>();
        JsonObject answer = this.applicationTarget.
                request().
                accept(MediaType.APPLICATION_JSON).
                get(JsonObject.class);
        JsonObject props = answer.getJsonObject("extraProperties");
        JsonObject kids = props.getJsonObject("childResources");
        Set<Map.Entry<String, JsonValue>> entrySet = kids.entrySet();
        for (Map.Entry<String, JsonValue> application : entrySet) {
            retVal.add(new Application(application.getKey(),
                    application.getValue().toString()));
        }
        return retVal;

    }

}
