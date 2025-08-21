package com.triviktech.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CorsConfig class is a Spring configuration class responsible for setting up
 * Cross-Origin Resource Sharing (CORS) rules for the application.
 *
 * CORS allows or restricts resources to be requested from a domain different from
 * the domain from which the resource originated.
 */
@Configuration
public class CorsConfig {

    /**
     * Bean that provides custom CORS configuration for the application.
     *
     * @return a WebMvcConfigurer that defines allowed origins, HTTP methods, and credentials.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            /**
             * Configures CORS mappings for the application.
             *
             * @param registry the CorsRegistry to configure allowed origins, methods, etc.
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Apply CORS to all endpoints
                        .allowedOrigins("http://localhost:3000") // Allow requests from frontend running on localhost:3000
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                        .allowCredentials(true); // Allow sending cookies/auth credentials
            }
        };
    }
}
