package org.acme.minecrafter.deployment;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LoggUg extends Handler {

    private String logStreamName;
    private String logGroupName;
    private String sequenceToken;

    // ...

    public LoggUg() {

    }

    @Override
    public void publish(LogRecord record) {

        System.out.println("publisheeeeeing" + record);
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}

