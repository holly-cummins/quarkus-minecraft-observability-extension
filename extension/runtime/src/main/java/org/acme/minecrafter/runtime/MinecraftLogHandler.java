package org.acme.minecrafter.runtime;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MinecraftLogHandler extends Handler {
    private final MinecraftService minecraft;

    public MinecraftLogHandler(MinecraftService minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void publish(LogRecord record) {
        String formattedMessage = String.format(record.getMessage(), record.getParameters());
        System.out.println("⛏️ [Minecrafter] " + formattedMessage);

        // TODO this hangs if the minecraft server is a dev service; make it fire-and-forget
        // minecraft.log(formattedMessage);

    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}

