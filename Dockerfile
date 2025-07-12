
FROM gradle:8.7-jdk17 AS build
COPY --chown=gradle:gradle . /app/jobhunter
# Đặt thư mục làm việc trong container là /app
WORKDIR /app/jobhunter

# Chạy lệnh Gradle để build ứng dụng và bỏ qua các bài test
RUN gradle clean build -x test --no-daemon

# State 2: Runtime the application
FROM openjdk:17-slim
# Mở cổng 8080 (nếu ứng dụng của bạn sử dụng cổng này, điều này không bắt buộc)
EXPOSE 8080

COPY --from=build /app/jobhunter/build/libs/*.jar /app/app.jar

# Định nghĩa lệnh để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]