FROM --platform=linux/amd64 gradle:8.5.0-jdk21 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./

COPY src src

RUN gradle build --no-daemon -x test

FROM --platform=linux/amd64 openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
