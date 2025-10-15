// src/main/java/com/example/sportsbook/security/OAuthSecurityConfig.java
package com.example.sportsbook.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnProperty(value = "app.oauth.enabled", havingValue = "true")
public class OAuthSecurityConfig {
  @Bean
  SecurityFilterChain oauth(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/actuator/**","/api/events/**","/api/me").permitAll()
        .requestMatchers("/api/bets/**").authenticated()
        .requestMatchers("/api/admin/**").authenticated() // later: restrict to ROLE_ADMIN
        .anyRequest().permitAll()
      )
      .oauth2Login(Customizer.withDefaults())
      .logout(l -> l.logoutSuccessUrl("/").permitAll());
    return http.build();
  }
}

