package com.example.sportsbook.controller;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class EventController {
  private final JdbcTemplate jdbc;
  public EventController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  @GetMapping("/events")
  public List<Map<String,Object>> listEvents() {
    String sql = "SELECT id, league, home_team, away_team, start_time, status, result "
               + "FROM events "
               + "ORDER BY start_time ASC";
    return jdbc.queryForList(sql);
  }

  @GetMapping("/events/{id}")
  public Map<String,Object> getEvent(@PathVariable long id) {
    String ev = "SELECT id, league, home_team, away_team, start_time, status, result "
              + "FROM events WHERE id = ?";
    Map<String,Object> event = jdbc.queryForMap(ev, id);

    String odds = "SELECT o.id, o.selection, o.american, COALESCE(o.odds_decimal, o.`decimal`) AS decimal "
                + "FROM markets m JOIN odds o ON o.market_id = m.id "
                + "WHERE m.event_id = ? AND m.type = 'MONEYLINE' "
                + "ORDER BY o.selection";
    List<Map<String,Object>> list = jdbc.queryForList(odds, id);

    event.put("odds", list);
    return event;
  }

  @GetMapping("/events/{id}/odds")
  public List<Map<String,Object>> getEventOdds(@PathVariable long id) {
    String odds = "SELECT o.id, o.selection, o.american, COALESCE(o.odds_decimal, o.`decimal`) AS decimal "
                + "FROM markets m JOIN odds o ON o.market_id = m.id "
                + "WHERE m.event_id = ? AND m.type = 'MONEYLINE' "
                + "ORDER BY o.selection";
    return jdbc.queryForList(odds, id);
  }
}
