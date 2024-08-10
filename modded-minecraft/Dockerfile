FROM gradle:7.4-jdk17 AS build
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradle.properties $APP_HOME
COPY src $APP_HOME

RUN gradle build

FROM webhippie/minecraft-forge:43.3

# This is needed for the client launched with `./gradlew runClient` to be able to connect
ENV MINECRAFT_ONLINE_MODE=false

EXPOSE 8081

COPY --from=build /usr/app/build/libs/modid-1.0.jar ${MINECRAFT_GAME_DIR}/mods