package com.example.examplemod;

import org.jetbrains.annotations.NotNull;

import javax.ws.rs.Consumes;
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
        invokeOnPlayer("say", message, null);

    }

    @POST
    @Path("/event")
    @Consumes("text/plain")
    public String alert(String type) {
        System.out.println("[Quarkcraft] event");
        return invokeOnPlayer("event", "A thing happened out in the real world", type);

    }

    @POST
    @Path("/boom")
    public String explode() {
        System.out.println("[Quarkcraft] boom");
        return invokeOnPlayer("explode", "Something -bad- happened out in the real world", null);
    }

    @NotNull
    private String invokeOnPlayer(String methodName, String message, String param) {
        if (player != null) {
// The player may be in a different classloader to us, so we need to use more reflection
            try {
                // Cheerfully assume all methods on PlayerWrapper take a string as an argument
                Method m = player.getClass()
                                 .getMethod(methodName, String.class, String.class);
                m.invoke(player, message, param);
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
