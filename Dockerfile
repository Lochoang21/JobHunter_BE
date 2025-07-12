# Build stage
FROM gradle:8.7-jdk17 AS build
WORKDIR /app
# Copy only necessary files for dependency resolution
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle
# Download dependencies
RUN gradle build --no-daemon --info -x test
# Copy source code and build the application
COPY src /app/src
RUN gradle bootJar --no-daemon --info -x test

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar
# Expose the port (Render expects this to match your app's port)
EXPOSE 8080
# Set environment variables for Render (optional, adjust as needed)
ENV JAVA_OPTS="-Xms512m -Xmx512m"
# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]