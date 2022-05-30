package io.quarkus.opentelemetry.exporter.otlp.runtime;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import io.quarkus.arc.DefaultBean;

@Singleton
public class MinecraftExporterProvider {
    @Produces
    @Singleton
    @DefaultBean
    public MinecraftLateBoundBatchSpanProcessor batchSpanProcessorForJaeger() {
        return new MinecraftLateBoundBatchSpanProcessor();
    }
}
