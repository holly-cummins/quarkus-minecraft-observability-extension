package org.acme.minecrafter.runtime;

import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MinecraftLogHandler extends Handler {

    private String logStreamName;
    private String logGroupName;
    private String sequenceToken;

    // ...


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

