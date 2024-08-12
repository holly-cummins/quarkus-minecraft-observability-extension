package org.acme.minecrafter.runtime;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.lang.reflect.Method;


@MinecraftLog
@Interceptor
public class MinecraftLogInterceptor {
    private final MinecraftService minecraft;

    public MinecraftLogInterceptor(MinecraftService minecraft) {
        this.minecraft = minecraft;
    }

    @AroundInvoke
    Object around(InvocationContext context) throws Exception {

        Method method = context.getMethod();
        // Simple implementation for now
        System.out.println("\uD83D\uDDE1️ [Minecrafter] Spotted use of " +
                method.getDeclaringClass()
                      .getSimpleName() + "." +
                method.getName());

        minecraft.recordVisit();

        return context.proceed();
    }

}


