package org.acme.minecrafter.runtime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Set;

@Path("/extensions")
public interface MinecraftService {

    @GET
    Set<?> getById(@QueryParam("id") String id);
}

