package org.acme.minecrafter.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "quarkus.minecrafter")
public interface MinecrafterConfig {

    /**
     * The minecraft server's observability base URL
     */
    @WithDefault("http://localhost:8081/")
    String baseURL();

    /**
     * The kind of animal we spawn
     */
    @WithDefault("chicken")
    String animalType();
}
