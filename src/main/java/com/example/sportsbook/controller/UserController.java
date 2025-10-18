package com.example.sportsbook.controller;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

  private final JdbcTemplate jdbc;

  public UserController(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @GetMapping("/users")
  public List<Map<String, Object>> listUsers() {
    // NOTE: Indentation must match tests exactly to satisfy Mockito stubbing
    return jdbc.queryForList("""
            SELECT id, email, display_name, created_at
            FROM users
            ORDER BY id ASC
        """);
  }
}
