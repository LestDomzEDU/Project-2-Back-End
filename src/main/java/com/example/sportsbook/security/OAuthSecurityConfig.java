package com.example.sportsbook.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnProperty(value = "app.oauth.enabled", havingValue = "true")
public class OAuthSecurityConfig {
  @Bean
  SecurityFilterChain oauthSecurity(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/actuator/**", "/api/events/**", "/api/me").permitAll()
        .anyRequest().authenticated()
      )
      .oauth2Login(Customizer.withDefaults())
      .logout(logout -> logout.logoutSuccessUrl("/").permitAll());
    return http.build();
  }
}