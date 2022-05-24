package com.example.examplemod;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

@Path("/observability")
public class Endpoint extends Application {

    @GET
    public String get() {
        System.out.println("QUARKCRAFT - The endpoint was hit");
        return "hello world";
    }


}
