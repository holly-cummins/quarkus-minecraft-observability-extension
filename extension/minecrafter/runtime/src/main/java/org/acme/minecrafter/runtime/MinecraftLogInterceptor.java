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
import java.util.Set;

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
    Object around(InvocationContext context) throws Exception {
//       This should be injected, but for now ...
        MinecraftService minecraft = new HandcraftedMinecraftService();

        Method method = context.getMethod();
        // Simple implementation for now
        System.out.println("\uD83D\uDDE1️ [Minecrafter] Spotted use of " +
                method.getDeclaringClass().getSimpleName() + "." +
                method.getName());

        minecraft.recordVisit();

        return context.proceed();
    }

}


