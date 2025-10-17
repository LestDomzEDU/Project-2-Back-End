package com.example.sportsbook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class EventAdminController {
  private final JdbcTemplate jdbc;
  public EventAdminController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  @PutMapping("/events/{id}/result")
  @ResponseStatus(HttpStatus.OK)
  public Map<String,Object> setResult(@PathVariable long id, @RequestParam String result) {
    String normalized = result == null ? null : result.trim().toUpperCase();
    if (!"HOME".equals(normalized) && !"AWAY".equals(normalized)) {
      throw new IllegalArgumentException("result must be HOME or AWAY");
    }
    jdbc.update("UPDATE events SET status='FINISHED', result=? WHERE id=?", normalized, id);
    int won = jdbc.update("UPDATE bets SET status='WON' WHERE event_id=? AND UPPER(selection)=?", id, normalized);
    int lost = jdbc.update("UPDATE bets SET status='LOST' WHERE event_id=? AND UPPER(selection)<>?", id, normalized);
    Map<String,Object> out = new HashMap<>();
    out.put("eventId", id);
    out.put("result", normalized);
    out.put("betsWon", won);
    out.put("betsLost", lost);
    return out;
  }
}
