package org.acme.minecrafter.runtime;


import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper
        implements ExceptionMapper<Exception> {
    @Context
    private MinecraftService minecraft;

    @Override
    public Response toResponse(Exception e) {
        minecraft.boom();

        // We lose some detail about the exceptions here, especially for 404, but we will live with that
        return Response.serverError().build();

    }
}
