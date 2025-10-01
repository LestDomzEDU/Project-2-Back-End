package com.example.sportsbook.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EventController {
  private final JdbcTemplate jdbc;
  public EventController(JdbcTemplate jdbc){ this.jdbc = jdbc; }

  @GetMapping("/events")
  public List<Map<String,Object>> listEvents(){
    return jdbc.queryForList("""
      SELECT id, league, home_team, away_team, start_time, status, COALESCE(result,'') AS result
      FROM events ORDER BY start_time ASC
    """);
  }

  @GetMapping("/events/{id}")
  public Map<String,Object> getEvent(@PathVariable long id){
    Map<String,Object> event = jdbc.queryForMap("""
      SELECT id, league, home_team, away_team, start_time, status, COALESCE(result,'') AS result
      FROM events WHERE id = ?
    """, id);

    List<Map<String,Object>> odds = jdbc.queryForList("""
      SELECT o.id, o.selection, o.american, o.decimal
      FROM markets m JOIN odds o ON o.market_id = m.id
      WHERE m.event_id = ? AND m.type = 'MONEYLINE'
      ORDER BY o.selection
    """, id);
    event.put("odds", odds);
    return event;
  }

  @GetMapping("/events/{id}/odds")
  public List<Map<String,Object>> getEventOdds(@PathVariable long id){
    return jdbc.queryForList("""
      SELECT o.id, o.selection, o.american, o.decimal
      FROM markets m JOIN odds o ON o.market_id = m.id
      WHERE m.event_id = ? AND m.type = 'MONEYLINE'
      ORDER BY o.selection
    """, id);
  }
}
