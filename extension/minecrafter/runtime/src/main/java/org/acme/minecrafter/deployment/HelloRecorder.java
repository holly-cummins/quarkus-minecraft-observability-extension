package org.acme.minecrafter.deployment;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class HelloRecorder {

    public void sayHello(String name) {
        System.out.println("Hello" + name);
    }

}