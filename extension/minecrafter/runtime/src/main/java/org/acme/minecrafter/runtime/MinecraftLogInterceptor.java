package org.acme.minecrafter.runtime;


//import org.jboss.resteasy.reactive.client.impl.ClientBuilderImpl;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

//import org.eclipse.microprofile.rest.client.inject.RestClient;
//import org.jboss.resteasy.reactive.client.impl.ClientBuilderImpl;
//import org.jboss.resteasy.reactive.client.impl.WebTargetImpl;//
//import org.jboss.resteasy.reactive.server.jackson.JacksonBasicMessageBodyReader;

@MinecraftLog
@Interceptor
public class MinecraftLogInterceptor {
//    @RestClient
//    MinecraftService minecraftService;


    @AroundInvoke
    Object around(InvocationContext context) throws Throwable {
//        ClientBuilderImpl clientBuilder = new ClientBuilderImpl();
//        Object thing = clientBuilder.build();

        Method method = context.getMethod();
        // Simple implementation for now
        System.out.println("\uD83D\uDDE1️ [Minecrafter] Spotted use of " +
                method.getDeclaringClass().getSimpleName() + "." +
                method.getName());


//        MinecraftService extensionsService = RestClientBuilder.newBuilder()
//                .baseUri(URI.create("https://stage.code.quarkus.io/api"))
//                .build(MinecraftService.class);

        URL url = new URL("http://localhost:8081/observability");
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

        return context.proceed();
    }
}


