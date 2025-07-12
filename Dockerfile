# Build stage
FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle bootJar --no-daemon

# Stage 2: Create the final image
FROM openjdk:17-jre-slim-buster
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]