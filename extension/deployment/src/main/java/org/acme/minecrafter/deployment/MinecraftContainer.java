package org.acme.minecrafter.deployment;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

public class MinecraftContainer extends GenericContainer<MinecraftContainer> {
    private static final int MINECRAFT_PORT = 25565;
    static final int OBSERVABILITY_PORT = 8081;

    public MinecraftContainer(DockerImageName image) {
        super(image);
        // This is a bit of a cheat, since at this point the client isn't ready, but otherwise it's too slow
        waitingFor(Wait.forLogMessage(".*" + "Preparing" + ".*", 1));
    }


    @Override
    protected void configure() {
        List<String> portBindings = new ArrayList<>();
        portBindings.add("25565:25565"); // Make life easy for the minecraft client
        setPortBindings(portBindings);

        withReuse(true);
        addExposedPorts(OBSERVABILITY_PORT);
        addExposedPorts(MINECRAFT_PORT);
    }

    public Integer getApiPort() {
        return this.getMappedPort(OBSERVABILITY_PORT);
    }

    public Integer getGamePort() {
        return this.getMappedPort(MINECRAFT_PORT);
    }
}