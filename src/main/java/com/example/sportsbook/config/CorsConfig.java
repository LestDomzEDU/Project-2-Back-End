package com.example.sportsbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {
  @Bean
  public CorsFilter corsFilter() {
    var conf = new CorsConfiguration();
    conf.setAllowedOrigins(List.of(System.getenv().getOrDefault("FRONTEND_URL","http://localhost:5173")));
    conf.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    conf.setAllowedHeaders(List.of("*"));
    conf.setAllowCredentials(true);

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", conf);
    return new CorsFilter(source);
  }
}
