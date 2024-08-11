package com.example.examplemod;


import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * It's a bit weird that this is even needed, but when running in the production server,
 * JAXRS can't find its writers or readers, probably because of class obfuscation
 * and classloader shenanigans.
 */
@Provider
@Produces("text/html")
public class StringMessageBodyWriter implements MessageBodyWriter<String> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return type == String.class;
    }

    @Override
    public long getSize(String String, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        // deprecated by JAX-RS 2.0 and ignored by Jersey runtime
        return 0;
    }

    @Override
    public void writeTo(String string, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
                        OutputStream out) throws IOException, WebApplicationException {

        Writer writer = new PrintWriter(out);
        writer.write("[minecraft servlet] ");
        writer.write(string);
        writer.flush();
        writer.close();
    }
}