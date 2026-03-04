package com.triviktech.config.security;

import com.triviktech.config.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * SecurityConfig class configures Spring Security for the application.
 * It sets up:
 * - JWT authentication filter
 * - Role-based access control for different endpoints
 * - CORS configuration
 * - Stateless session management
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    /**
     * Constructor injection for JWT request filter
     *
     * @param jwtRequestFilter the filter that validates JWT tokens for incoming
     *                         requests
     */
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Configures the SecurityFilterChain for the application.
     *
     * @param httpSecurity the HttpSecurity object provided by Spring Security
     * @return SecurityFilterChain configured with JWT, roles, CORS, and stateless
     *         session
     * @throws Exception in case of any configuration errors
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Enable CORS with the custom configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Disable CSRF for stateless API usage
                .csrf(csrf -> csrf.disable())
                // Set session management to stateless (JWT-based)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure role-based authorization for endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/pms/auth/**", // Public endpoints for authentication
                                "/ws/**" // WebSocket endpoints
                        ).permitAll()
                        .requestMatchers("/api/v1/pms/manager/**").hasRole("MANAGER")
                        .requestMatchers("/api/v1/pms/hr/**", "/api/v1/pms/krakpi/**").hasRole("HR")
                        .requestMatchers("/api/v1/pms/employee/**").hasRole("EMPLOYEE")
                        .anyRequest().authenticated())
                // Add JWT filter before Spring Security's default authorization filter
                .addFilterBefore(jwtRequestFilter, AuthorizationFilter.class)
                .build();
    }

    /**
     * Provides a CORS configuration source for the application.
     * This allows requests from specified origins with allowed methods and headers.
     *
     * @return CorsConfigurationSource object with allowed origins, headers,
     *         methods, and credentials
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed origins: frontend URLs
        configuration.setAllowedOrigins(Arrays.asList(
                Origins.localUrl,
                Origins.localUrl2,
                Origins.serverUrl));

        // Allowed HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Allowed headers in requests
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        // Allow sending credentials (cookies, auth headers)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
        return source;
    }
}
