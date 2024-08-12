package org.acme.minecrafter.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.AnnotationsTransformerBuildItem;
import io.quarkus.arc.deployment.BeanContainerBuildItem;
import io.quarkus.arc.processor.AnnotationsTransformer;
import io.quarkus.deployment.IsNormal;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.deployment.builditem.LogHandlerBuildItem;
import io.quarkus.deployment.dev.devservices.GlobalDevServicesConfig;
import io.quarkus.resteasy.reactive.spi.ExceptionMapperBuildItem;
import jakarta.ws.rs.Priorities;
import org.acme.minecrafter.runtime.HelloRecorder;
import org.acme.minecrafter.runtime.MinecraftLog;
import org.acme.minecrafter.runtime.MinecraftLogHandlerMaker;
import org.acme.minecrafter.runtime.MinecraftLogInterceptor;
import org.acme.minecrafter.runtime.MinecraftService;
import org.acme.minecrafter.runtime.RestExceptionMapper;
import org.jboss.jandex.DotName;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

import static io.quarkus.deployment.annotations.ExecutionTime.RUNTIME_INIT;
import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

class MinecrafterProcessor {

    private static final String FEATURE = "minecrafter";
    private static final DotName JAX_RS_GET = DotName.createSimple("jakarta.ws.rs.GET");

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @Record(STATIC_INIT)
    @BuildStep
    public void helloBuildStep(HelloRecorder recorder) {
        recorder.sayHello("World");
    }

    @Record(RUNTIME_INIT)
    @BuildStep
    LogHandlerBuildItem addLogHandler(final MinecraftLogHandlerMaker maker, BeanContainerBuildItem beanContainer) {
        return new LogHandlerBuildItem(maker.create(beanContainer.getValue()));
    }

    /**
     * Makes the interceptor as a bean so we can access it.
     */
    @BuildStep
    void beans(BuildProducer<AdditionalBeanBuildItem> producer) {
        producer.produce(AdditionalBeanBuildItem.unremovableOf(MinecraftLogInterceptor.class));
        producer.produce(AdditionalBeanBuildItem.unremovableOf(MinecraftService.class));
    }

    @BuildStep
    AnnotationsTransformerBuildItem transform() {
        return new AnnotationsTransformerBuildItem(new AnnotationsTransformer() {

            public boolean appliesTo(org.jboss.jandex.AnnotationTarget.Kind kind) {
                return kind == org.jboss.jandex.AnnotationTarget.Kind.METHOD;
            }

            public void transform(TransformationContext context) {
                if (context.getTarget()
                           .asMethod()
                           .hasAnnotation(JAX_RS_GET)) {
                    context.transform()
                           .add(MinecraftLog.class)
                           .done();
                }
            }
        });
    }

    @BuildStep
    ExceptionMapperBuildItem exceptionMappers() {
        return new ExceptionMapperBuildItem(RestExceptionMapper.class.getName(),
                Exception.class.getName(), Priorities.USER + 100, true);
    }

    @BuildStep(onlyIfNot = IsNormal.class, onlyIf = GlobalDevServicesConfig.Enabled.class)
    public DevServicesResultBuildItem createContainer(LaunchModeBuildItem launchMode) {
        // Normally, this would be a remote image, but we need to build one with the right mods, so use a local one
        DockerImageName dockerImageName = DockerImageName.parse("minecraft-server");

        // Don't be tempted to put this in a try-with-resources block, even if the IDE advises it
        // Otherwise the dev service gets shut down after startup :)
        MinecraftContainer container = new MinecraftContainer(dockerImageName);
        container.start();

        // Set a config property so that anything using the container can find it, even on the random port

        Map<String, String> props = Map.of("quarkus.minecrafter.base-url",
                "http://" + container.getHost() + ":" + container.getApiPort());

        System.out.println("API port: " + "http://" + container.getHost() + ":" + container.getApiPort());
        System.out.println("Game port: " + "http://" + container.getHost() + ":" + container.getGamePort());

        return new DevServicesResultBuildItem.RunningDevService(FEATURE, container.getContainerId(),
                container::close, props)
                .toBuildItem();
    }
}

