package com.example.examplemod;

import org.jetbrains.annotations.NotNull;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Path("/observability")
public class Endpoint {

    // Ugly and static, but it will work for the moment
    private static Object player;

    @GET
    @Path("/visit")
    public String get() {
        System.out.println("[Quarkcraft] get");
        return invokeOnPlayer("say", "A thing happened out in the real world");

    }

    @GET
    @Path("/boom")
    public String explode() {
        System.out.println("[Quarkcraft] boom");
        return invokeOnPlayer("explode", "Something -bad- happened out in the real world");
    }

    @NotNull
    private String invokeOnPlayer(String methodName, String message) {
        if (player != null) {
// The player will be in a different classloader to us, so we need to use more reflection
            try {
                // Cheerfully assume all methods on PlayerWrapper take a string as an argument
                Method m = player.getClass().getMethod(methodName, String.class);
                m.invoke(player, message);
                return "minecraft world updated";
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
