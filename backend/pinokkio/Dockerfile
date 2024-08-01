FROM gradle:8.8-jdk21 AS builder

WORKDIR /app

COPY pinokkio/gradle/ gradle/

COPY pinokkio/build.gradle pinokkio/settings.gradle pinokkio/gradlew ./

RUN chmod +x gradlew

COPY pinokkio/src/ src/

RUN ./gradlew build -x test --no-daemon

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
