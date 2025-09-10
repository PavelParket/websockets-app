FROM gradle:8.9-jdk21 AS builder
WORKDIR /app

COPY backend/security-service/build.gradle backend/security-service/settings.gradle ./backend/security-service/

RUN gradle -p backend/security-service build --no-daemon -x test || return 0

COPY backend/security-service ./backend/security-service

RUN gradle -p backend/security-service clean build --no-daemon -x test

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/backend/security-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
