package io.quarkus.sample;

import io.quarkus.logging.Log;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Path("/")
public class IndexResource {

    @GET
    public Response redirect() {
        Log.info("Redirecting.");
        URI redirect = UriBuilder.fromUri("todo.html").build();
        return Response.temporaryRedirect(redirect).build();
    }
}