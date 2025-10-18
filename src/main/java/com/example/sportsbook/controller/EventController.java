package com.example.sportsbook.controller;

import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EventController {

  private final JdbcTemplate jdbc;

  public EventController(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @GetMapping("/events")
  public List<Map<String,Object>> listEvents() {
    return jdbc.queryForList("""
      SELECT id, home, away, starts_at, status, COALESCE(result,'') AS result
        FROM events
       ORDER BY id ASC
    """);
  }

  @GetMapping("/events/{id}")
  public Map<String,Object> getEvent(@PathVariable long id) {
    try {
      return jdbc.queryForMap("""
        SELECT id, home, away, starts_at, status, COALESCE(result,'') AS result
          FROM events
         WHERE id = ?
      """, id);
    } catch (EmptyResultDataAccessException e) {
      throw new IllegalArgumentException("event not found");
    }
  }

  @GetMapping("/events/{id}/odds")
  public List<Map<String,Object>> getEventOdds(@PathVariable long id){
    return jdbc.queryForList("""
      SELECT o.id, o.selection, o.american, o.decimal
        FROM markets m
        JOIN odds o ON o.market_id = m.id
       WHERE m.event_id = ?
         AND m.type = 'MONEYLINE'
       ORDER BY o.selection
    """, id);
  }
}
