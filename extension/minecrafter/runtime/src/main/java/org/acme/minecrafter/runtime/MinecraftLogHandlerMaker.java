package org.acme.minecrafter.runtime;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.Optional;
import java.util.logging.Handler;

@Recorder
public class MinecraftLogHandlerMaker {
    RuntimeValue<MinecrafterConfig> config;

    public MinecraftLogHandlerMaker(RuntimeValue<MinecrafterConfig> config) {
        this.config = config;
    }

    public RuntimeValue<Optional<Handler>> create() {
        Handler handler = new MinecraftLogHandler(new MinecraftService(config.getValue()));
        return new RuntimeValue<>(Optional.of(handler));

    }
}

