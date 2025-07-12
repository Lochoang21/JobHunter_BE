# Build stage
FROM gradle:8.7-jdk17 AS build
WORKDIR /app
# Copy source code
COPY --chown=gradle:gradle . .
# Build ứng dụng (bỏ qua test)
RUN gradle build -x test --no-daemon

# Run stage - Eclipse Temurin (official OpenJDK)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Tạo user non-root
RUN addgroup -g 1001 -S appgroup && \
    adduser -S -u 1001 -G appgroup appuser

# Copy file JAR từ build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Chuyển ownership cho user non-root
RUN chown appuser:appgroup app.jar

# Chạy với user non-root
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]