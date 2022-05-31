package org.acme.minecrafter.runtime;


import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper
        implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
//       This should be injected, but for now ...
        MinecraftService minecraft = new HandcraftedMinecraftService();
        minecraft.boom();

        return Response.serverError().build();

    }
}
