package org.acme.minecrafter.runtime;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;


@MinecraftLog
@Interceptor
public class MinecraftLogInterceptor {
    MinecraftService minecraft = new MinecraftService();

    @AroundInvoke
    Object around(InvocationContext context) throws Exception {

        Method method = context.getMethod();
        // Simple implementation for now
        System.out.println("\uD83D\uDDE1️ [Minecrafter] Spotted use of " +
                method.getDeclaringClass().getSimpleName() + "." +
                method.getName());

        minecraft.recordVisit();

        return context.proceed();
    }

}


