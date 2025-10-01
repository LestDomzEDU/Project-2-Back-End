package com.example.sportsbook.controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/health")
public class HealthController {
  @GetMapping
  public Map<String,String> ok(){ return Map.of("status","ok"); }
}