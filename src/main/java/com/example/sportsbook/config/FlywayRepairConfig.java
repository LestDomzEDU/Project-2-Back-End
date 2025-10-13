package com.example.sportsbook.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayRepairConfig {
  @Bean
  public FlywayMigrationStrategy repairThenMigrate() {
    return flyway -> {
      try { flyway.repair(); } catch (Exception ignored) {}
      flyway.migrate();
    };
  }
}
