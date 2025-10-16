package com.example.sportsbook.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * This runner ensures that Flyway runs repair/migrate automatically
 * every time the application starts.
 *
 * Fixes the Heroku build error: "class FlywayRepairRunner is public,
 * should be declared in a file named FlywayRepairRunner.java"
 */
@Component
public class FlywayRepairRunner implements ApplicationRunner {

    private final Flyway flyway;

    @Autowired
    public FlywayRepairRunner(Flyway flyway) {
        this.flyway = flyway;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            System.out.println("Running Flyway repair and migrate...");
            flyway.repair();    // cleans up metadata issues
            flyway.migrate();   // applies any pending migrations
            System.out.println("Flyway repair/migrate completed successfully.");
        } catch (Exception e) {
            System.err.println("Error running Flyway repair/migrate: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
