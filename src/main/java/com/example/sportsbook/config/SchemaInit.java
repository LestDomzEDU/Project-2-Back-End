package com.example.sportsbook.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Ensures the 'events' table exists on startup (useful on fresh JawsDB instances
 * or when Flyway hasn’t run yet). Safe to re-run due to IF NOT EXISTS.
 */
@Component
public class SchemaInit implements ApplicationRunner {

    private final JdbcTemplate jdbc;

    public SchemaInit(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Create table if it does not exist
        jdbc.execute("""
            CREATE TABLE IF NOT EXISTS events (
              id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
              league VARCHAR(32) NOT NULL,
              home_team VARCHAR(64) NOT NULL,
              away_team VARCHAR(64) NOT NULL,
              start_time DATETIME NOT NULL,
              `status` VARCHAR(32) NOT NULL,
              result VARCHAR(255) DEFAULT ''
            )
            """);

        // Helpful index for your most common query
        jdbc.execute("CREATE INDEX IF NOT EXISTS idx_events_start_time ON events (start_time)");

        // If your MySQL version doesn't support "IF NOT EXISTS" for index, use a guard:
        // jdbc.execute("DO 0");
        // We’ll keep it simple; if the index already exists, MySQL will ignore duplicate creates in recent versions.
    }
}
