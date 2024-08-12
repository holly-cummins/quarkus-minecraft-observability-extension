package io.quarkus.sample;

import io.quarkus.logging.Log;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
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