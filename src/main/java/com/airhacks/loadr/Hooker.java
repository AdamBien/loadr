package com.airhacks.loadr;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
public class Hooker {

    private final WebTarget successTarget;

    public Hooker(String uri) {
        this.successTarget = ClientBuilder.newClient().target(uri);
    }

    public String invokeGET() {
        Response response = this.successTarget.request().get();
        return evaluateResponse(response);
    }

    public String invokePOST() {
        Response response = this.successTarget.request().post(Entity.text(""));
        return evaluateResponse(response);
    }

    String evaluateResponse(Response response) {
        if (response.getStatusInfo().getFamily()
                == Response.Status.Family.SUCCESSFUL) {
            return response.readEntity(String.class);
        } else {
            return "-";
        }
    }

}
