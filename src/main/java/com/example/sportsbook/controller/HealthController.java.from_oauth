// src/main/java/com/example/sportsbook/controller/HealthController.java
package com.example.sportsbook.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {
  @GetMapping("/health")
  public Map<String,String> health() {
    return Map.of("status", "ok");
  }
}
