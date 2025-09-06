FROM gradle:8.9-jdk21 AS builder
WORKDIR /app

COPY backend/mediator-service/build.gradle backend/mediator-service/settings.gradle ./backend/mediator-service/

RUN gradle -p backend/mediator-service build --no-daemon -x test || return 0

COPY backend/mediator-service ./backend/mediator-service

RUN gradle -p backend/mediator-service clean build --no-daemon -x test

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/backend/mediator-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
