package com.example.sportsbook.controller;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api")
public class EventController {
  private final JdbcTemplate jdbc;
  public EventController(JdbcTemplate jdbc){ this.jdbc = jdbc; }
  @GetMapping("/events")
  public List<Map<String,Object>> listEvents(){
    return jdbc.queryForList(
      "SELECT id, league, home_team, away_team, start_time, status FROM events ORDER BY start_time ASC"
    );
  }
}
