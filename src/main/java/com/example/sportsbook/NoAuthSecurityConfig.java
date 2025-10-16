package com.example.sportsbook.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Loads when:
 *   - app.oauth.enabled is false OR missing
 *   - (and there isn’t another SecurityFilterChain defined already)
 *
 * This simply permits requests. Tighten this if you want auth in non-OAuth mode.
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "app.oauth.enabled", havingValue = "false", matchIfMissing = true)
public class NoAuthSecurityConfig {

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain noAuthSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
