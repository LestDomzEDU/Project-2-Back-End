package com.example.sportsbook.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EventController {

  private final JdbcTemplate jdbc;

  public EventController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  // --- GET all events ---
  @GetMapping("/events")
  public List<Map<String,Object>> events() {
    String sql = "SELECT id, league, home_team, away_team, start_time, status, result "
               + "FROM events ORDER BY start_time ASC";
    return jdbc.queryForList(sql);
  }

  // --- GET one event + odds ---
  @GetMapping("/events/{id}")
  public Map<String,Object> event(@PathVariable long id) {
    Map<String,Object> ev = jdbc.queryForMap(
      "SELECT id, league, home_team, away_team, start_time, status, result FROM events WHERE id = ?", id
    );
    String odds = "SELECT o.id, o.selection, o.american, COALESCE(o.odds_decimal, o.\"decimal\") AS decimal "
                + "FROM markets m JOIN odds o ON o.market_id = m.id "
                + "WHERE m.event_id = ? AND m.\"type\" = 'MONEYLINE' "
                + "ORDER BY o.selection";
    ev.put("odds", jdbc.queryForList(odds, id));
    return ev;
  }

  // --- GET event odds only ---
  @GetMapping("/events/{id}/odds")
  public List<Map<String,Object>> odds(@PathVariable long id) {
    try {
      jdbc.queryForObject("SELECT id FROM events WHERE id = ?", Long.class, id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
    }
    String odds = "SELECT o.id, o.selection, o.american, COALESCE(o.odds_decimal, o.\"decimal\") AS decimal "
                + "FROM markets m JOIN odds o ON o.market_id = m.id "
                + "WHERE m.event_id = ? AND m.\"type\" = 'MONEYLINE' "
                + "ORDER BY o.selection";
    return jdbc.queryForList(odds, id);
  }

  // --- POST create new event ---
  @PostMapping("/events")
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String, Object> createEvent(@RequestBody Map<String, Object> body) {
    String league     = stringVal(body.get("league"));
    String home_team  = stringVal(body.get("home_team"));
    String away_team  = stringVal(body.get("away_team"));
    String start_time = stringVal(body.get("start_time"));
    String status     = stringVal(body.get("status"));

    if (league == null || home_team == null || away_team == null || start_time == null || status == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields");
    }

    Timestamp startTs;
    try {
      startTs = Timestamp.from(OffsetDateTime.parse(start_time).toInstant());
    } catch (DateTimeParseException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid start_time format (use ISO-8601)");
    }

    String sql = "INSERT INTO events (league, home_team, away_team, start_time, status) "
               + "VALUES (?, ?, ?, ?, ?) "
               + "RETURNING id, league, home_team, away_team, start_time, status, result";

    return jdbc.queryForMap(sql, league, home_team, away_team, startTs, status);
  }

  private String stringVal(Object o) {
    return (o == null) ? null : o.toString();
  }
}
