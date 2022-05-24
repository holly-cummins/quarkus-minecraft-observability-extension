package com.example.examplemod;

import java.util.HashSet;
import java.util.Set;

// This is strangely ineffective, presumably because of a deployment detail @ApplicationPath("/api")
public class Application extends javax.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(Endpoint.class);
        return classes;
    }
}
