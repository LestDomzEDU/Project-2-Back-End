package com.example.sportsbook.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api")
public class EventAdminController {
  private final JdbcTemplate jdbc;
  public EventAdminController(JdbcTemplate jdbc){ this.jdbc = jdbc; }

  public static class SettleRequest { public String result; } // "HOME" or "AWAY"

  @PutMapping("/events/{id}/settlement")
  @Transactional
  public Map<String,Object> settle(@PathVariable Long id, @RequestBody SettleRequest req){
    if (req == null || req.result == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "result required");
    String result = req.result.trim().toUpperCase();
    if (!result.equals("HOME") && !result.equals("AWAY"))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "result must be HOME or AWAY");

    // Ensure event exists and not already FINAL
    List<Map<String,Object>> rows = jdbc.queryForList(
      "SELECT status FROM events WHERE id = ?", id);
    if (rows.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "event not found");
    String status = String.valueOf(rows.get(0).get("status"));
    if ("FINAL".equalsIgnoreCase(status))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "event already final");

    // Mark event final with result
    int eUpd = jdbc.update(
      "UPDATE events SET status = 'FINAL', result = ? WHERE id = ?", result, id);
    if (eUpd != 1) throw new ResponseStatusException(HttpStatus.CONFLICT, "event changed; try again");

    // Cascade bet outcomes
    int won  = jdbc.update("UPDATE bets SET state = 'WON'  WHERE event_id = ? AND selection = ? AND state = 'PENDING'", id, result);
    int lost = jdbc.update("UPDATE bets SET state = 'LOST' WHERE event_id = ? AND selection <> ? AND state = 'PENDING'", id, result);

    Map<String,Object> out = new LinkedHashMap<>();
    out.put("eventId", id);
    out.put("result", result);
    out.put("betsWon", won);
    out.put("betsLost", lost);
    return out;
  }
}
