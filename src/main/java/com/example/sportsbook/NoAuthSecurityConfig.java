package com.example.sportsbook;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnProperty(value = "app.oauth.enabled", havingValue = "false", matchIfMissing = true)
public class NoAuthSecurityConfig {
  @Bean
  public SecurityFilterChain noAuth(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/api/health", "/actuator/**", "/api/**").permitAll()
        .anyRequest().permitAll()
      );
    return http.build();
  }
}
