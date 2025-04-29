## The business context

A business has moved its entire estate to Quarkus. It's awesome! Cloud costs are down, developer productivity is up,
morale is high. In fact, the business is doing so well, it's been acquired.

The new owner has some ... requirements ... for observability. And they need to be implemented right now, across the
entire estate of 300 apps.

A Quarkus extension is well-suited to this task, because it provides a comvenient way of altering application behaviour,
with just a maven dependency change.
(There are [other use cases](https://quarkus.io/guides/writing-extensions#some-types-of-extensions) for extensions, but
this is the one we're looking at here.)

## The demo

### Stand up the demo app

The demo app is a todo list maker.

_Before the demo_ copy the todo app to a fresh directory and edit the pom.xml to remove the extension dependency.

During the demo

```bash
cd quarkus-todo-app
quarkus dev
```

Visit [http://localhost:8080/](http://localhost:8080/) to confirm the app is up and working.

### Write the extension

#### Scaffold

Scaffold the extension using

```bash
mvn io.quarkus.platform:quarkus-maven-plugin:create-extension -N
```

Build it and add it as a dependency to the todo app pom.

```xml

<dependency>
    <groupId>org.acme</groupId>
    <artifactId>demo-extension</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

*Note* After every extension change, it will need to be built and the todo app launched.

#### Hello world

Make a class which says hello.

<!-- MARKDOWN-AUTO-DOCS:START (CODE:src=./extension/runtime/src/main/java/org/acme/minecrafter/runtime/HelloRecorder.java) -->
<!-- The below code snippet is automatically added from ./extension/runtime/src/main/java/org/acme/minecrafter/runtime/HelloRecorder.java -->
```java
package org.acme.minecrafter.runtime;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class HelloRecorder {

    public void sayHello(String name) {
        System.out.println("Hello" + name);
    }

}
```
<!-- MARKDOWN-AUTO-DOCS:END -->

.. and hook it into the processor.

<!-- MARKDOWN-AUTO-DOCS:START (CODE:src=./extension/deployment/src/main/java/org/acme/minecrafter/deployment/MinecrafterProcessor.java&lines=36-40) -->
<!-- The below code snippet is automatically added from ./extension/deployment/src/main/java/org/acme/minecrafter/deployment/MinecrafterProcessor.java -->
```java
class MinecrafterProcessor {

    private static final String FEATURE = "minecrafter";
    private static final DotName JAX_RS_GET = DotName.createSimple("jakarta.ws.rs.GET");
```
<!-- MARKDOWN-AUTO-DOCS:END -->

If you build the extension and then run `quarkus dev` on the app, you should see your hello world message.

#### Custom log handler

Next, do something more interesting by hooking all logging.

```
    @Record(RUNTIME_INIT)
    @BuildStep
    LogHandlerBuildItem addLogHandler(final LogHandlerMaker maker) {
        return new LogHandlerBuildItem(maker.create());
    }
```

```java

@Recorder
public class MinecraftLogHandlerMaker {

    public RuntimeValue<Optional<Handler>> create() {
        Handler handler = new LogHandler();
        return new RuntimeValue<>(Optional.of(handler));

    }
}
```

For the log handler, extend LogHander and add this into the publish method:

```java
   @Override
public void publish(LogRecord record){
        String formattedMessage=String.format(record.getMessage(),record.getParameters());
        System.out.println("⛏️ "+formattedMessage);
        }
```

Test logs come out twice on app startup, once from the Quarkus handler and one from our custom handler.

#### Interceptor on REST endpoints

What we really want to know about is when our REST endpoints are hit.

Declare an interceptor binding.

```

@InterceptorBinding
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DemoLog {
}
```

... and an implementation which is an interceptor:

```java

@DemoLog
@Interceptor
public class LogInterceptor {

    @AroundInvoke
    Object around(InvocationContext context) throws Exception {

        Method method = context.getMethod();
        // Simple implementation for now
        System.out.println("\uD83D\uDDE1️ Spotted use of " +
                method.getDeclaringClass().getSimpleName() + "." +
                method.getName());

        return context.proceed();
    }
}
```

## The 'and another thing' business context update

There's an extra wrinkle. We didn't do all this work just to print to standard out. Our observability console isn't the
terminal, or even Jaeger, or gelf, or anything like that. It's minecraft. Our new CEO is the pre-teen version of boss
baby and lives in minecraft so we need to visualise everything in their preferred platform. If we want to sound
business-y, this is just advanced 'gamification'.

We have a customised minecraft instance which exposes some endpoints we can send endpoints to.

### Show minecraft

Show the minecraft window and start a game (connect to the pre-defined Quarkiverse server).

### Configuration

The minecraft server is on localhost right now, but it could be anywhere, so we'll need some configuration.

<!-- MARKDOWN-AUTO-DOCS:START (CODE:src=./extension/runtime/src/main/java/org/acme/minecrafter/runtime/MinecrafterConfig.java) -->
<!-- The below code snippet is automatically added from ./extension/runtime/src/main/java/org/acme/minecrafter/runtime/MinecrafterConfig.java -->
```java
package org.acme.minecrafter.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigMapping(prefix = "quarkus.minecrafter")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public class MinecrafterConfig {

    /**
     * The minecraft server's observability base URL
     */
    @ConfigItem(defaultValue = "http://localhost:8081/")
    public String baseURL;

    /**
     * The kind of animal we spawn
     */
    @ConfigItem(defaultValue = "chicken")
    public String animalType;
}
```
<!-- MARKDOWN-AUTO-DOCS:END -->

#### Add rest client

Create a JAX-RS client which talks to the endpoints in our minecraft mod.

<!-- MARKDOWN-AUTO-DOCS:START (CODE:src=./extension/runtime/src/main/java/org/acme/minecrafter/runtime/MinecraftService.java) -->
<!-- The below code snippet is automatically added from ./extension/runtime/src/main/java/org/acme/minecrafter/runtime/MinecraftService.java -->
```java
package org.acme.minecrafter.runtime;

import jakarta.inject.Singleton;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public class MinecraftService {

    private final MinecrafterConfig minecrafterConfig;
    private final Client client;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    public MinecraftService(MinecrafterConfig minecrafterConfig) {
        this.minecrafterConfig = minecrafterConfig;
        this.client = ClientBuilder.newClient();
    }

    public void recordVisit() {
        invokeMinecraft("event");
    }

    public void boom() {
        invokeMinecraft("boom");
    }


    public void log(String message) {
        try {
            client.target(minecrafterConfig.baseURL)
                  .path("observability/log")
                  .request(MediaType.TEXT_PLAIN)
                  .post(Entity.text(message));
            // Don't log anything back about the response or it ends up with too much circular logging
        } catch (Throwable e) {
            System.out.println("\uD83D\uDDE1️ [Minecrafter] Connection error: " + e);
        }
    }

    private void invokeMinecraft(String path) {
        executor.submit(() -> invokeMinecraftSynchronously(path));
    }

    private void invokeMinecraftSynchronously(String path) {
        try {
            String response = client.target(minecrafterConfig.baseURL)
                                    .path("observability/" + path)
                                    .request(MediaType.TEXT_PLAIN)
                                    .post(Entity.text(minecrafterConfig.animalType))
                                    .readEntity(String.class);

            System.out.println("\uD83D\uDDE1️ [Minecrafter] Mod response: " + response);
        } catch (Throwable e) {
            System.out.println("\uD83D\uDDE1️ [Minecrafter] Connection error: " + e);
        }
    }

}
```
<!-- MARKDOWN-AUTO-DOCS:END -->

#### Add rest client

Add rest calls to the log handler and REST interceptor.

In the log handler:

```java
        minecraft.log(formattedMessage);
```

In the rest interceptor:

```java
                minecraft.recordVisit();
```

Arrange the windows so you can see both the minecraft game and the terminal and the browser window for the rest app.
Restart the todo app. You should see quarkus logs in the minecraft window.
Visit [http://localhost:8080/](http://localhost:8080/). You should see a lightning flash and a chicken will appear.

#### Exception handling

Next, let's do some exception handling. Create an exception mapper:

<!-- MARKDOWN-AUTO-DOCS:START (CODE:src=./extension/runtime/src/main/java/org/acme/minecrafter/runtime/RestExceptionMapper.java) -->
<!-- The below code snippet is automatically added from ./extension/runtime/src/main/java/org/acme/minecrafter/runtime/RestExceptionMapper.java -->
```java
package org.acme.minecrafter.runtime;


import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper
        implements ExceptionMapper<Exception> {
    @Context
    private MinecraftService minecraft;

    @Override
    public Response toResponse(Exception e) {

        // Tactically suppress target resource exceptions
        // of the form jakarta.ws.rs.NotFoundException: Unable to find matching target resource method
        if (!(e instanceof NotFoundException)) {
            minecraft.boom();
        }

        // We lose some detail about the exceptions here, especially for 404, but we will live with that
        return Response.serverError()
                       .build();

    }
}
```
<!-- MARKDOWN-AUTO-DOCS:END -->

... and hook it in to the extension:

<!-- MARKDOWN-AUTO-DOCS:START (CODE:src=./extension/deployment/src/main/java/org/acme/minecrafter/deployment/MinecrafterProcessor.java&lines=73-78) -->
<!-- The below code snippet is automatically added from ./extension/deployment/src/main/java/org/acme/minecrafter/deployment/MinecrafterProcessor.java -->
```java
                return kind == org.jboss.jandex.AnnotationTarget.Kind.METHOD;
            }

            public void transform(TransformationContext context) {
                if (context.getTarget()
                           .asMethod()
```
<!-- MARKDOWN-AUTO-DOCS:END -->

Visit [http://localhost:8080/api/6](http://localhost:8080/api/6). This will trigger a 404 exception. In minecraft, you
should see the lightning flash for the visit, and then the chicken will explode. If you're unlucky, your character will
be killed by the exploding chicken.

## Next app

If we'd just wanted to connect one app to minecraft, we could have done it in several ways.

But now we can take another app, add the dependency, and see *it* turning up in minecraft.

### Superheroes app

[Alternatively, do the Quarkus starter app]

Quit the todo app (to avoid a port conflict).

Start the superheroes app:

```
docker-compose -f deploy/docker-compose/java17.yml -f deploy/docker-compose/prometheus.yml up --remove-orphans
```

Then visit [http://localhost:8080](http://localhost:8080) and [localhost:8085](http://localhost:8085).

Then modify the pom.xml

```xml

<dependency>
    <groupId>org.acme</groupId>
    <artifactId>demo-extension</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Visit the web page again and you should see the events in the minecraft client.

## Discussion of next steps

Here are some of the things that someone could do with more time. These are good things to talk through after the demo.

- Dev service for the minecraft instance; we won't always have access to the production minecraft server
- Assigning an animal to each application so we can distinguish source
- Displaying metrics
- Visualise application load - rain means heavy load, snow very very heavy
- Smart log routing, send some to minecraft and not others, choose an action based on log contents
- Consider adding some of the [other minecraft environmental effects](things-we-can-do-in-minecraft.md)