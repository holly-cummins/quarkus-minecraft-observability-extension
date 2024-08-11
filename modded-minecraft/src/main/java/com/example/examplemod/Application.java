package com.example.examplemod;

import java.util.HashSet;
import java.util.Set;

// This is strangely ineffective, presumably because of a deployment detail @ApplicationPath("/api")
public class Application extends javax.ws.rs.core.Application {

    // Ideally this wouldn't be needed, but in the strange obfuscated world of the minecraft mod,
    // class scanning does not work very well.
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(Endpoint.class);
        classes.add(StringMessageBodyWriter.class);
        classes.add(StringMessageBodyReader.class);
        classes.add(PlainStringMessageBodyWriter.class);
        return classes;
    }
}
