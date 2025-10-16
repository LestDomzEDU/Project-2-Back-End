package com.example.sportsbook.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(value = "spring.flyway.enabled", havingValue = "true", matchIfMissing = true)
public class FlywayRepairConfig {

    private static final Logger log = LoggerFactory.getLogger(FlywayRepairConfig.class);

    /** Create Flyway manually if Spring Boot didn't */
    @Bean
    @ConditionalOnMissingBean(Flyway.class)
    public Flyway flyway(DataSource dataSource) {
        log.info("Creating Flyway bean manually...");
        return Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .load();
    }

    /** Run Flyway.repair() automatically when the app starts */
    @Bean
    public ApplicationRunner flywayRepairRunner(Flyway flyway) {
        return args -> {
            try {
                log.info("Running Flyway repair...");
                flyway.repair();
                log.info("Flyway repair completed successfully.");
            } catch (Exception ex) {
                log.warn("Flyway repair skipped/failed: {}", ex.getMessage());
            }
        };
    }
}
