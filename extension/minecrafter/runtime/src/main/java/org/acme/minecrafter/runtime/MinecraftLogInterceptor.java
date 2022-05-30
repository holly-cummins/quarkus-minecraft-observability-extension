package org.acme.minecrafter.runtime;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;


@MinecraftLog
@Interceptor
public class MinecraftLogInterceptor {
    @AroundInvoke
    Object around(InvocationContext context) throws Throwable {
        Method method = context.getMethod();
        // Simple implementation for now
        System.out.println("\uD83D\uDDE1️ [Minecrafter] Spotted use of " +
                method.getDeclaringClass().getSimpleName() + "." +
                method.getName());
        return context.proceed();
    }
}


