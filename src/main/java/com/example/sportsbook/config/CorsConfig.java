package com.example.sportsbook.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        // 👇 list real front-end origins here; no "*" when allowCredentials=true
        .allowedOriginPatterns(
            "http://localhost:3000",
            "http://localhost:5173",
            "https://*.vercel.app",
            "https://*.netlify.app",
            "https://<your-frontend-domain>"
        )
        .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
  }
}
