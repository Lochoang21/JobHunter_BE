package vn.hoidanit.jobhunter.config;

import io.swagger.v3.oas.models.info.Info;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mysql.cj.x.protobuf.MysqlxCursor.Open;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    private Server createServer(String url, String description) {
        Server server = new Server();
        server.setUrl(url);
        server.setDescription(description);
        return server;
    }

    private Contact createContact() {
        return new Contact()
                .name("HOANG BAO LOC")
                .email("ads.lochoang2101@gmail.com")
                .url("https://hoidanit.vn");

    }
    private License createLicense() {
        return new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

    }
    private Info createApiInfo() {
        return new Info()
                .title("Job Hunter API")
                .description("API for Job Hunter")
                .version("1.0.0")
                .contact(createContact())
                .termsOfService("https://example.vn")
                .license(createLicense());
    }
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .servers(List.of(createServer("http://localhost:8080", "Server URL in Development Environment"),
                        createServer("https://jobhunter.vn", "Server URL in Production Environment")))
                .addSecurityItem(new SecurityRequirement().addList("BEARER-AUTHENTICATION"))
                .components(new Components().addSecuritySchemes("BEARER-AUTHENTICATION", createAPIKeyScheme()));
    }
}
