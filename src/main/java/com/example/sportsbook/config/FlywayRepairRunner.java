package com.example.sportsbook.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * Repairs Flyway metadata on startup (safe no-op if nothing to repair).
 */
public class FlywayRepairRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(FlywayRepairRunner.class);
    private final Flyway flyway;

    public FlywayRepairRunner(Flyway flyway) {
        this.flyway = flyway;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("Running Flyway repair (if needed)...");
            flyway.repair();
            log.info("Flyway repair complete.");
        } catch (Exception ex) {
            // Don’t fail the app if repair isn’t needed or DB is clean
            log.warn("Flyway repair skipped/failed (non-fatal): {}", ex.getMessage());
        }
    }
}
