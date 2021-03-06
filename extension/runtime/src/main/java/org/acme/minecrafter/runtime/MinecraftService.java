package org.acme.minecrafter.runtime;

import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.eclipse.microprofile.config.inject.ConfigProperty;
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

@Singleton
public class MinecraftService {

    private final MinecrafterConfig minecrafterConfig;
    private final Client client;

    public MinecraftService(MinecrafterConfig minecrafterConfig) {
        this.minecrafterConfig = minecrafterConfig;
        this.client = ClientBuilder.newClient();
    }

    public void recordVisit() {
        invokeMinecraft("event");
    }

    public void boom() {
        invokeMinecraft("boom");
    }


    public void log(String message) {
        try {
            client.target(minecrafterConfig.baseURL).path("log")
                    .request(MediaType.TEXT_PLAIN).post(Entity.text(message));
            // Don't log anything back about the response or it ends up with too much circular logging
        } catch (Throwable e) {
            System.out.println("\uD83D\uDDE1️ [Minecrafter] Connection error: " + e);
        }
    }

    private void invokeMinecraft(String path) {
        try {
            String response = client.target(minecrafterConfig.baseURL).path(path)
                    .request(MediaType.TEXT_PLAIN)
                    .get(String.class);

            System.out.println("\uD83D\uDDE1️ [Minecrafter] Mod response: " + response);
        } catch (Throwable e) {
            System.out.println("\uD83D\uDDE1️ [Minecrafter] Connection error: " + e);
        }
    }

}

