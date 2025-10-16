package com.example.sportsbook.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class FlywayRepairRunner implements ApplicationRunner {

    private final Flyway flyway;

    // control with env var: FLYWAY_REPAIR_ON_START=true to run in Heroku when needed
    @Value("${FLYWAY_REPAIR_ON_START:false}")
    private boolean repairOnStart;

    public FlywayRepairRunner(Flyway flyway) {
        this.flyway = flyway;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (repairOnStart) {
            flyway.repair();
        }
    }
}
