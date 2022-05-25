package com.example.examplemod;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Path("/observability")
public class Endpoint {

    // Ugly and static, but it will work for the moment
    private static Object player;

    @GET
    public String get() {
        System.out.println("QUARKCRAFT - The endpoint was hit");
        if (player != null) {
// The player will be in a different classloader to us, so we need to use more reflection
            try {
                Method m = player.getClass().getMethod("say", String.class);
                m.invoke(player, "Ooh look, something happened");
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
