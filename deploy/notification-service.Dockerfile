FROM gradle:8.9-jdk21 AS builder
WORKDIR /app

COPY backend/notification-service/build.gradle backend/notification-service/settings.gradle ./backend/notification-service/

RUN gradle -p backend/notification-service build --no-daemon -x test || return 0

COPY backend/notification-service ./backend/notification-service

RUN gradle -p backend/notification-service clean build --no-daemon -x test

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/backend/notification-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
