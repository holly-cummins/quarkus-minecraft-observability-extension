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

        // Don't send anything to the remote minecraft instance
        // Even with catches for exceptions, problems in remote calls can generate logging
        // ... and that causes infinite output in the best case
        // ... and a total block of server startup in the bad case
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}

