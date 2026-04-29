package com.ws101.deleon.ecommerceapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS configuration for the REST API.
 * 
 * Enables Cross-Origin Resource Sharing (CORS) to allow frontend applications
 * running on different origins (ports) to communicate with this backend API.
 * 
 * @author Kent Jeanne S. De Leon
 * @author Keniel Drew D. De Asis
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mappings for all API endpoints.
     * 
     * Allows:
     * - Origins: http://localhost:5500, http://localhost:3000, http://localhost:8000
     * - Methods: GET, POST, PUT, DELETE, PATCH, OPTIONS
     * - Headers: Authorization, Content-Type, Accept
     * - Credentials: allowed
     * - Max age: 1 hour
     * 
     * @param registry the CORS registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:5500",
                        "http://localhost:3000",
                        "http://localhost:8000",
                        "http://127.0.0.1:5500",
                        "http://127.0.0.1:3000",
                        "http://127.0.0.1:8000"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
