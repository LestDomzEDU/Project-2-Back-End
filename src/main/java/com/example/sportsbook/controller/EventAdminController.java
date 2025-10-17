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

  public EventAdminController(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @PostMapping("/events/{id}/result/{result}")
  @ResponseStatus(HttpStatus.OK)
  public Map<String,Object> setEventResult(@PathVariable long id, @PathVariable String result) {
    String normalized = result.toUpperCase();
    if (!normalized.equals("HOME") && !normalized.equals("AWAY")) {
      throw new IllegalArgumentException("Result must be HOME or AWAY");
    }

    // set event result & mark finished
    jdbc.update("UPDATE events SET result = ?, status = 'FINISHED' WHERE id = ?", normalized, id);

    // settle bets: selection = result -> WON, others -> LOST
    int won = jdbc.update(
      "UPDATE bets SET status='WON' WHERE event_id=? AND UPPER(selection)=?",
      id, normalized
    );
    int lost = jdbc.update(
      "UPDATE bets SET status='LOST' WHERE event_id=? AND UPPER(selection)<>?",
      id, normalized
    );

    Map<String,Object> out = new HashMap<>();
    out.put("eventId", id);
    out.put("result", normalized);
    out.put("betsWon", won);
    out.put("betsLost", lost);
    return out;
  }
}
