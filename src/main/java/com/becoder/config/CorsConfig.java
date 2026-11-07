
package com.becoder.config;

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

        // Allow the React/Vite fronted
        configuration.setAllowedOrigins(
            Arrays.asList("http://localhost:5173")
        );

        // Allow required HTTP methods
        configuration.setAllowedMethods(
            Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        // Allow JWT and JSON headers
        configuration.setAllowedHeaders(
            Arrays.asList("Authorization", "Content-Type", "Accept")
        );

        // Allow authentication-related headers in responses
        configuration.setExposedHeaders(
            Arrays.asList("Authorization")
        );

        // Allow credentials
        configuration.setAllowCredentials(true);

        // Cache preflight response for one hour
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
