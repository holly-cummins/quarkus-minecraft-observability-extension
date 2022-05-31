package org.acme.minecrafter.runtime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

public class HandcraftedMinecraftService implements MinecraftService {


    @Override
    public void recordVisit() {
        invokeMinecraft("visit");
    }

    @Override
    public void boom() {
        invokeMinecraft("boom");
    }


    private void invokeMinecraft(String path) {

//        MinecraftService extensionsService = RestClientBuilder.newBuilder()
//                .baseUri(URI.create("https://stage.code.quarkus.io/api"))
//                .build(MinecraftService.class);

        try {
            URL url = new URL("http://localhost:8081/observability/" + path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            System.out.println("\uD83D\uDDE1️ [Minecrafter] Mod response: " + content);
        } catch (Throwable e) {
            System.out.println("\uD83D\uDDE1️ [Minecrafter] Connection error: " + e);
        }
    }
}

