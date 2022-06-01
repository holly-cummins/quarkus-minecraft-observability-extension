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
./start-database.sh
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

*Note* After every extension change, it will need to be built and the todo app launched.

#### Hello world

Make a class which says hello.

<!-- MARKDOWN-AUTO-DOCS:START (CODE:src=./extension/runtime/src/main/java/org/acme/minecrafter/runtime/HelloRecorder.java) -->
<!-- MARKDOWN-AUTO-DOCS:END -->

.. and hook it into the processor.

<!-- MARKDOWN-AUTO-DOCS:START (CODE:src=./extension/deployment/src/main/java/org/acme/minecrafter/deployment/MinecrafterProcessor.java&lines=36-40) -->
<!-- MARKDOWN-AUTO-DOCS:END -->

#### Custom log handler

...

#### Interceptor on REST endpoints

...

## The 'and another thing' business context update

There's an extra wrinkle. We didn't do all this work just to print to standard out. Our observability console isn't the
terminal, or even Jaeger, or gelf, or anything like that. It's minecraft. Our new CEO is the pre-teen version of boss
baby and lives in minecraft so we need to visualise everything in their preferred platform. If we want to sound
business-y, this is just advanced 'gamification'.

We have a customised minecraft instance which exposes some endpoints we can send endpoints to.

### Show minecraft

Show the minecraft window and start a game (connect to the pre-defined Quarkiverse server).

#### Add rest calls

Add rest calls to the log handler and REST interceptor ...

#### Exception handling

... Visit [http://localhost:8080/api/6](http://localhost:8080/api/6). This will trigger a 404 exception.

## Next app

If we'd just wanted to connect one app to minecraft, we could have done it in several ways.

But now we can take another app, add the dependency, and see *it* turning up in minecraft.

## Discussion of next steps

Here are some of the things that we could do with more time, to talk through.

- Configuration of the extension
- Assigning an animal to each application so we can distinguish source
- Displaying metrics
- Visualise application load - rain means heavy load, snow very very heavy
- Smart log routing, send some to minecraft and not others, choose an action based on log contents
- Consider adding some of the [other minecraft environmental effects](things-we-can-do-in-minecraft.md)