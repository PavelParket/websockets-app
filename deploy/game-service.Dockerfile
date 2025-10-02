FROM gradle:8.9-jdk21 AS builder
WORKDIR /app

COPY backend/game-service/build.gradle backend/game-service/settings.gradle ./backend/game-service/

RUN gradle -p backend/game-service build --no-daemon -x test || return 0

COPY backend/game-service ./backend/game-service

RUN gradle -p backend/game-service clean build --no-daemon -x test

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/backend/game-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
