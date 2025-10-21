<<<<<<< HEAD
// src/main/java/com/example/sportsbook/config/FlywayRepairConfig.java
package com.example.sportsbook.config;

=======
package com.example.sportsbook.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
>>>>>>> origin/main
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayRepairConfig {
<<<<<<< HEAD
  // intentionally empty – default Flyway autoconfiguration will just migrate
=======
  @Bean
  public FlywayMigrationStrategy flywayMigrationStrategy() {
    return flyway -> {
      flyway.repair();
      flyway.migrate();
    };
  }
>>>>>>> origin/main
}
