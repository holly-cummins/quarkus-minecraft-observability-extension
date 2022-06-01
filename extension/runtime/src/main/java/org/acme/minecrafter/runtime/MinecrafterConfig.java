package org.acme.minecrafter.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public class MinecrafterConfig {

    /**
     * The minecraft server's observability base URL
     */
    @ConfigItem(defaultValue = "http://localhost:8081/observability/")
    public String baseURL;
}
