package org.acme.minecrafter.runtime;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.Optional;
import java.util.logging.Handler;

@Recorder
public class MinecraftLogHandlerMaker {

    public RuntimeValue<Optional<Handler>> create(BeanContainer beanContainer) {
        MinecraftService minecraft = beanContainer.beanInstance(MinecraftService.class);
        Handler handler = new MinecraftLogHandler(minecraft);
        return new RuntimeValue<>(Optional.of(handler));

    }
}

