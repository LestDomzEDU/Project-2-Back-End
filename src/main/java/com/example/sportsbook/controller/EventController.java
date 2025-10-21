package com.example.sportsbook.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class EventController {
  private final JdbcTemplate jdbc;
  public EventController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  @GetMapping("/events")
  public List<Map<String,Object>> events() {
    String sql = "SELECT id, league, home_team, away_team, start_time, status, result "
               + "FROM events ORDER BY start_time ASC";
    return jdbc.queryForList(sql);
  }

  @GetMapping("/events/{id}")
  public Map<String,Object> event(@PathVariable long id) {
    Map<String,Object> ev = jdbc.queryForMap(
      "SELECT id, league, home_team, away_team, start_time, status, result FROM events WHERE id = ?", id
    );

    String odds = "SELECT o.id, o.selection, o.american, COALESCE(o.odds_decimal, o.`decimal`) AS decimal "
                + "FROM markets m JOIN odds o ON o.market_id = m.id "
                + "WHERE m.event_id = ? AND m.`type` = 'MONEYLINE' "
                + "ORDER BY o.selection";
    ev.put("odds", jdbc.queryForList(odds, id));
    return ev;
  }

  @GetMapping("/events/{id}/odds")
  public List<Map<String,Object>> odds(@PathVariable long id) {
    // ensure event exists (gives clearer 404 if someone asks for a bad id)
    try {
      jdbc.queryForObject("SELECT id FROM events WHERE id = ?", Long.class, id);
    } catch (EmptyResultDataAccessException e) {
      throw new org.springframework.web.server.ResponseStatusException(
          org.springframework.http.HttpStatus.NOT_FOUND, "Event not found");
    }

    String odds = "SELECT o.id, o.selection, o.american, COALESCE(o.odds_decimal, o.`decimal`) AS decimal "
                + "FROM markets m JOIN odds o ON o.market_id = m.id "
                + "WHERE m.event_id = ? AND m.`type` = 'MONEYLINE' "
                + "ORDER BY o.selection";
    return jdbc.queryForList(odds, id);
  }
}
