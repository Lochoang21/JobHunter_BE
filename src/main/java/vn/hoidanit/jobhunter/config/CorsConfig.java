package vn.hoidanit.jobhunter.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //cho phép các URL kết nối tới backend
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4173", "http://localhost:5173")); //link frontend
        
        //các method được kết nối
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed methods
        
        //các phần header gửi lên
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept","x-no-retry"));
        
        // gửi kèm cookie
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        // How long the response from a pre-flight request can be cached by clients
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply this configuration to all paths
        return source;
    }
}
