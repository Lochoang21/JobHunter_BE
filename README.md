# JobHunter Backend

JobHunter Backend là một ứng dụng API được xây dựng bằng Java Spring Boot, cung cấp các dịch vụ backend cho website tìm việc làm JobHunter. Dự án hỗ trợ các chức năng như quản lý công việc, hồ sơ người dùng, và tìm kiếm việc làm.

## Mục lục

- [Giới thiệu](#giới-thiệu)
- [Tính năng](#tính-năng)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cài đặt](#cài-đặt)
- [Chạy dự án](#chạy-dự-án)
- [Cấu trúc thư mục](#cấu-trúc-thư-mục)
- [API Endpoints](#api-endpoints)
- [Đóng góp](#đóng-góp)
- [Liên hệ](#liên-hệ)

## Giới thiệu

JobHunter Backend cung cấp các API RESTful để hỗ trợ website tìm việc làm, cho phép ứng viên tìm kiếm việc làm, nhà tuyển dụng đăng tin tuyển dụng, và quản lý hồ sơ người dùng. Backend được xây dựng với Spring Boot, đảm bảo hiệu suất cao và khả năng mở rộng.

## Tính năng

- **Quản lý công việc**: Tạo, đọc, cập nhật, xóa (CRUD) các tin tuyển dụng.
- **Quản lý người dùng**: Đăng ký, đăng nhập, quản lý thông tin hồ sơ.
- **Tìm kiếm việc làm**: Hỗ trợ tìm kiếm công việc theo từ khóa, địa điểm, ngành nghề, hoặc mức lương.
- **Xác thực và phân quyền**: Sử dụng JWT để xác thực người dùng và phân quyền theo vai trò (ứng viên, nhà tuyển dụng).
- **Tích hợp cơ sở dữ liệu**: Lưu trữ thông tin công việc và người dùng trong cơ sở dữ liệu quan hệ.

## Công nghệ sử dụng

- **Framework**: Spring Boot
- **Ngôn ngữ**: Java
- **Cơ sở dữ liệu**: MySQL/PostgreSQL (tùy cấu hình)
- **Xác thực**: Spring Security, JWT
- **ORM**: Spring Data JPA, Hibernate
- **Công cụ build**: Maven
- **Khác**: Lombok, Swagger (dùng để tài liệu hóa API), SLF4J (logging)

## Cài đặt

Để cài đặt và chạy dự án trên máy local, làm theo các bước sau:

### 1. Clone repository:
```bash
git clone https://github.com/Lochoang21/JobHunter_BE.git
cd JobHunter_BE
```

### 2. Cài đặt dependencies:
```bash
mvn clean install
```

### 3. Cấu hình cơ sở dữ liệu:

- Cài đặt MySQL và tạo một cơ sở dữ liệu (ví dụ: `jobhunter_db`).

- Cập nhật file `src/main/resources/application.properties` hoặc `application.yml` với thông tin cơ sở dữ liệu:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jobhunter_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 4. Cấu hình biến môi trường (nếu cần):

- Thêm các biến môi trường như JWT_SECRET cho xác thực JWT trong file `application.properties`:
```properties
jwt.secret=your_jwt_secret_key
```

## Chạy dự án

### 1. Chạy ứng dụng:
```bash
mvn spring-boot:run
```

API sẽ chạy tại `http://localhost:8080` (mặc định).

### 2. Kiểm tra API:

- Truy cập Swagger UI (nếu được cấu hình) tại: `http://localhost:8080/swagger-ui.html`
- Hoặc sử dụng Postman/Insomnia để kiểm tra các endpoint.

## Cấu trúc thư mục

```
├── src/
│   ├── main/
│   │   ├── java/com/jobhunter/
│   │   │   ├── config/         # Cấu hình ứng dụng (Spring Security, Swagger, v.v.)
│   │   │   ├── controller/     # Các API endpoints
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── entity/         # Các entity JPA
│   │   │   ├── repository/     # Repository cho cơ sở dữ liệu
│   │   │   ├── service/        # Logic nghiệp vụ
│   │   │   └── JobHunterApplication.java  # Class chính của ứng dụng
│   │   └── resources/
│   │       └── application.properties  # Cấu hình ứng dụng
├── pom.xml                     # File cấu hình Maven
└── README.md                   # Tài liệu dự án
```

## API Endpoints

Để xem tài liệu API chi tiết, truy cập Swagger UI sau khi chạy ứng dụng.

## Đóng góp

Chúng tôi hoan nghênh mọi đóng góp để cải thiện dự án! Để đóng góp:

1. Fork repository này.
2. Tạo một branch mới (`git checkout -b feature/your-feature`).
3. Commit thay đổi của bạn (`git commit -m 'Add your feature'`).
4. Push lên branch (`git push origin feature/your-feature`).
5. Tạo một Pull Request.

Vui lòng đảm bảo mã nguồn tuân thủ các quy tắc định dạng (Checkstyle, nếu có) trước khi gửi Pull Request.

## Liên hệ

- **Tác giả**: Lochoang21
- **GitHub**: [Lochoang21](https://github.com/Lochoang21)
- **Email**: [Thêm email của bạn nếu muốn]
- **Issues**: Nếu gặp lỗi hoặc có đề xuất, vui lòng mở issue tại [GitHub Issues](https://github.com/Lochoang21/JobHunter_BE/issues).

