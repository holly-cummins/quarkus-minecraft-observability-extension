# Minecraft as an observability client: A Quarkus extension demo

![workflow](https://github.com/holly-cummins/quarkus-minecraft-observability-extension/actions/workflows/actions.yml/badge.svg)

## Quick start

### Start the minecraft server

```bash
cd modded-minecraft
./gradlew runServer
```

### Start the minecraft client

You will need the Java edition of Minecraft. Start a multiplayer game, and connect to `http://localhost:25565`.

You will need to configure the client to allow you to `alt-tab` away from the client without it pausing and bringing up
a menu. Edit `options.txt` in
your [minecraft folder](https://gaming.stackexchange.com/questions/15664/can-i-alt-tab-out-of-minecraft-without-the-game-auto-pausing)
, and change `pauseOnLostFocus` to `false`.

```
pauseOnLostFocus:false
```

### Build the extension

```bash
cd extension/minecrafter
mvn install
```

### Start the sample application

```bash
cd quarkus-todo-app
./start-database.sh
quarkus dev
```

(If you do `mvn install`, with podman it should be `TESTCONTAINERS_RYUK_DISABLED="true" mvn install`.)

### Interact with the web app

Arrange your windows so you can see both the minecraft client and the web application.
Visit [http://localhost:8080](http://localhost:8080). While you're interacting with the web app, you should see things
happen in the minecraft world. Visiting the page will cause a mob to spawn, and an exception (like a 404) will cause an
explosion.
