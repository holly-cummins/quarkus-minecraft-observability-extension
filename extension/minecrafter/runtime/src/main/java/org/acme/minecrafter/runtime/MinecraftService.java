package org.acme.minecrafter.runtime;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Set;

public class MinecraftService {

    Client client = ClientBuilder.newClient();
    String BASE_URL = "http://localhost:8081/observability/";

    public void recordVisit() {
        invokeMinecraft("visit");
    }

    public void boom() {
        invokeMinecraft("boom");
    }


    private void invokeMinecraft(String path) {
        try {
            String response = client.target(BASE_URL + path)
                    .request(MediaType.TEXT_PLAIN)
                    .get(String.class);

            System.out.println("\uD83D\uDDE1️ [Minecrafter] Mod response: " + response);
        } catch (Throwable e) {
            System.out.println("\uD83D\uDDE1️ [Minecrafter] Connection error: " + e);
        }
    }
}

