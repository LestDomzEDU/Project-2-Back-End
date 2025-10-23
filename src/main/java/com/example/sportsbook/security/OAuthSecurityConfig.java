package com.example.sportsbook.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Configuration
@ConditionalOnProperty(value = "app.oauth.enabled", havingValue = "true", matchIfMissing = true)
public class OAuthSecurityConfig {

  @Bean
  public SecurityFilterChain oauthSecurity(HttpSecurity http,
                                           ClientRegistrationRepository clientRegistrationRepository) throws Exception {

    // Force GitHub to show the consent/login screen every time
    DefaultOAuth2AuthorizationRequestResolver resolver =
        new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

    resolver.setAuthorizationRequestCustomizer(builder -> builder
        .additionalParameters(params -> {
          // GitHub doesn't standardize "prompt", but it ignores unknown params.
          // Adding both increases chances the UI is shown each time.
          params.put("prompt", "consent");
          params.put("login", "true");
          params.put("allow_signup", "false");
        })
    );

    http
      .csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/index.html", "/public/**", "/health", "/actuator/**").permitAll()
        .requestMatchers("/oauth2/**", "/login/**").permitAll()
        .anyRequest().authenticated()
      )
      .oauth2Login(oauth -> oauth
        .authorizationEndpoint(ae -> ae.authorizationRequestResolver(resolver))
        .defaultSuccessUrl("/api/me", true)   // land somewhere that proves auth
        .failureUrl("/login?error=true")
      )
      .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/")
      );

    return http.build();
  }

  // CORS for local front-end dev; adjust as needed
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of(
      "http://localhost:5173",
      "http://127.0.0.1:5173",
      "http://localhost:3000"
    ));
    config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
