package org.acme.minecrafter.runtime;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.Optional;
import java.util.logging.Handler;

@Recorder
public class MinecraftLogHandlerMaker {
    public RuntimeValue<Optional<Handler>> create() {

        Handler handler = new MinecraftLogHandler();

        return new RuntimeValue<>(Optional.of(handler));
    }
}

