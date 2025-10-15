package com.example.sportsbook.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Runs Flyway.repair() once on startup to fix checksum mismatch in production.
 * Remove or disable after the first successful deploy.
 */
@Component
@Profile("!local") // optional: won’t run when you use `--spring.profiles.active=local`
public class FlywayRepairRunner implements ApplicationRunner {
  private final Flyway flyway;
  public FlywayRepairRunner(Flyway flyway) { this.flyway = flyway; }
  @Override public void run(ApplicationArguments args) {
    flyway.repair(); // updates flyway_schema_history checksums to match current scripts
  }
}
