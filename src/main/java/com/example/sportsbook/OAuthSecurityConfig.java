package com.example.sportsbook;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnProperty(value = "app.oauth.enabled", havingValue = "true")
public class OAuthSecurityConfig {

  // Spring will only require this bean when OAuth is enabled
  private final ClientRegistrationRepository clientRegistrationRepository;
  public OAuthSecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
    this.clientRegistrationRepository = clientRegistrationRepository;
  }

  @Bean
  public SecurityFilterChain oauth(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/api/health", "/actuator/**").permitAll()
        .anyRequest().authenticated()
      )
      .oauth2Login(Customizer.withDefaults());
    return http.build();
  }
}
