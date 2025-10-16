package com.example.sportsbook.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides a safe way to repair Flyway metadata automatically at startup.
 */
@Configuration
public class FlywayRepairConfig {

    @Bean
    public ApplicationRunner flywayRepairRunner(Flyway flyway) {
        return args -> {
            try {
                System.out.println("Running Flyway repair...");
                flyway.repair();
                System.out.println("Flyway repair completed successfully.");
            } catch (Exception ex) {
                System.err.println("Flyway repair skipped/failed: " + ex.getMessage());
            }
        };
    }
}
