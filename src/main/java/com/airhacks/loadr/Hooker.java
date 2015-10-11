package com.airhacks.loadr;

import javax.ws.rs.client.ClientBuilder;
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

    public String invoke() {
        Response response = this.successTarget.request().get();
        if (response.getStatusInfo().getFamily()
                == Response.Status.Family.SUCCESSFUL) {
            return response.readEntity(String.class);
        } else {
            return "-";
        }
    }

}
