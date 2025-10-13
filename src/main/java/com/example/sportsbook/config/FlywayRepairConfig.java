package com.example.sportsbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;

@Configuration
public class FlywayRepairConfig {
  @Bean
  public FlywayMigrationStrategy repairThenMigrate() {
    return flyway -> {            // no flyway.clean() here!
      try { flyway.repair(); } catch (Exception ignored) {}
      flyway.migrate();
    };
  }
}
