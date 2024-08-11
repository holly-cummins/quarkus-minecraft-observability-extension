package com.example.examplemod;

import org.jetbrains.annotations.NotNull;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Path("/observability")
public class Endpoint {

    // Ugly and static, but it will work for the moment
    private static Object player;

    @POST
    @Path("/log")
    @Consumes("text/plain")
    public void log(String message) {
        System.out.println("[Quarkcraft] log");
        invokeOnPlayer("say", message);

    }

    // Ideally we would return a string, but when installed as a mod in a container, that gives org.jboss.resteasy.core.NoMessageBodyWriterFoundFailure: Could not find MessageBodyWriter for response object of type: java.lang.String of
    // media type: text/html
    @GET
    @Path("/event")
    public void alert() {
        System.out.println("[Quarkcraft] event");
        invokeOnPlayer("event", "A thing happened out in the real world");

    }

    // Ideally we would return a string, but when installed as a mod in a container, that gives org.jboss.resteasy.core.NoMessageBodyWriterFoundFailure: Could not find MessageBodyWriter for response object of type: java.lang.String of
    // media type: text/html
    @GET
    @Path("/boom")
    public void explode() {
        System.out.println("[Quarkcraft] boom");
        invokeOnPlayer("explode", "Something -bad- happened out in the real world");
    }

    @NotNull
    private String invokeOnPlayer(String methodName, String message) {
        if (player != null) {
// The player will be in a different classloader to us, so we need to use more reflection
            try {
                // Cheerfully assume all methods on PlayerWrapper take a string as an argument
                Method m = player.getClass()
                                 .getMethod(methodName, String.class);
                m.invoke(player, message);
                return "minecraft world updated with " + methodName;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return "internal error";
            }
        } else {
            return "no player logged in";
        }
    }

    public static void setPlayer(Object newPlayer) {
        player = newPlayer;
    }


}
