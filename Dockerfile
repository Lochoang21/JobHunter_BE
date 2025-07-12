# Build stage
FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY . .

# Build the application
RUN gradle clean build -x test --no-daemon

# Runtime stage
FROM openjdk:17-slim
WORKDIR /app

# Copy the built JAR file
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]