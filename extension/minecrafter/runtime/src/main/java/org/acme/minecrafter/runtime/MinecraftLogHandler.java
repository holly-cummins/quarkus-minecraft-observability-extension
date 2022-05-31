package org.acme.minecrafter.runtime;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MinecraftLogHandler extends Handler {

    @Override
    public void publish(LogRecord record) {

        System.out.println("⛏️ [Minecrafter] " + String.format(record.getMessage(), record.getParameters()));

    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}

