package org.acme.minecrafter.runtime;


import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper
        implements ExceptionMapper<Exception> {
    @Context
    private MinecraftService minecraft;

    @Override
    public Response toResponse(Exception e) {
        minecraft.boom();

        // We lose some detail about the exceptions here, especially for 404, but we will live with that
        return Response.serverError()
                       .build();

    }
}

