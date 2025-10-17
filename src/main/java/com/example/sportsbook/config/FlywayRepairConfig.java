package com.example.sportsbook.config;
import org.flywaydb.core.Flyway; import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy; import org.springframework.context.annotation.*;
@Configuration public class FlywayRepairConfig {
  @Bean public FlywayMigrationStrategy flywayMigrationStrategy(){ return flyway -> { flyway.repair(); flyway.migrate(); }; }
}
