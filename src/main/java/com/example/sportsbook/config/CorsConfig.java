package com.example.sportsbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:19006", "https://sportsbook-api-lester-efa829183023.herokuapp.com", "mysql://o3l8hudq5ybihbjr:b1h74d3w3mveenkf@gmgcjwawatv599gq.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/y3r8m33tban043u4").allowedMethods("GET","POST","PUT","DELETE","OPTIONS").allowCredentials(true);;
      }
    };
  }
}
