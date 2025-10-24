package com.example.sportsbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class OAuthSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // We rely on session cookies for OAuth; CSRF can be disabled for this API/mobile setup.
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // --- PUBLIC ENDPOINTS ---
                .requestMatchers(
                    "/", "/index.html",
                    "/favicon.ico", "/error",
                    "/public/**",
                    "/oauth2/**", "/login/**",
                    "/api/events/**"          // <-- events + odds are public
                ).permitAll()

                // --- EVERYTHING ELSE REQUIRES AUTH ---
                .anyRequest().authenticated()
            )

            // OAuth2 login (GitHub)
            .oauth2Login(oauth -> oauth
                .defaultSuccessUrl("/api/me", true)
                .failureUrl("/login?error=true")
            )

            // App logout -> back to root
            .logout(logout -> logout.logoutSuccessUrl("/"));

        return http.build();
    }
}
