package com.SUNData.MemberApp.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * SecurityConfig centralizes all security-related configuration:
 * - Authentication & authorization rules
 * - JWT filter integration
 * - CORS configuration for frontend-backend communication
 * - Password encoding
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    /**
     * Constructor injection of the custom JWT filter.
     * This filter validates JWT tokens before requests reach controllers.
     */
    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Defines the main security filter chain.
     * - Disables CSRF (not needed for stateless APIs using JWT).
     * - Enables CORS with our custom configuration.
     * - Configures role-based access rules for API endpoints.
     * - Adds the JWT filter before Spring’s UsernamePasswordAuthenticationFilter.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF disabled for stateless JWT usage
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Apply CORS rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Public endpoints (login, register)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Restricted to ADMIN role
                        .requestMatchers("/api/facilitator/**").hasRole("FACILITATOR") // Restricted to FACILITATOR role
                        .requestMatchers("/api/coordinator/**").hasRole("COORDINATOR") // Restricted to COORDINATOR role
                        .anyRequest().authenticated() // All other endpoints require authentication
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures CORS rules for frontend-backend communication.
     * - Allows requests from localhost (React dev server).
     * - Supports common HTTP methods.
     * - Allows all headers.
     * - Enables credentials (cookies, authorization headers).
     * <p>
     * IMPORTANT: Replace "http://localhost:*" with your production frontend domain when deploying.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*")); // Dev frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    /**
     * Exposes AuthenticationManager as a bean.
     * Required for login/authentication services to perform authentication.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides a PasswordEncoder bean.
     * BCrypt is recommended for secure password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
