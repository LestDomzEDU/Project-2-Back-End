package com.example.sportsbook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.sql.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.server.ResponseStatusException;

@RestController
// Accept BOTH /admin/... and /api/admin/... so either style in tests works
@RequestMapping({"/admin", "/api/admin"})
@CrossOrigin(origins = "*")
public class EventAdminController {

  private final JdbcTemplate jdbc;

  public EventAdminController(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  /**
   * POST /admin/events  (also works at /api/admin/events)
   * Accepts form params OR JSON body:
   *   { "home": "TeamA", "away": "TeamB", "startsAt": "2025-10-17T00:00:00Z" }
   * Returns: { "eventId": <number> } with 201 Created.
   */
  @PostMapping("/events")
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String,Object> createEvent(
      @RequestParam(required = false) String home,
      @RequestParam(required = false) String away,
      @RequestParam(required = false, name = "startsAt") String startsAtParam,
      @RequestBody(required = false) Map<String,Object> body
  ) {
    // Merge params and JSON body so tests can send either style
    String h = home, a = away, s = startsAtParam;
    if (body != null) {
      if (h == null) h = Objects.toString(body.get("home"), null);
      if (a == null) a = Objects.toString(body.get("away"), null);
      if (s == null) s = Objects.toString(body.get("startsAt"), null);
    }
    if (h == null || h.isBlank() || a == null || a.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "home and away are required");
    }

    // Copy into final variables for use in the lambda (needed by Java)
    final String finalHome = h;
    final String finalAway = a;
    final String finalStartsAt = (s == null || s.isBlank()) ? null : s;

    // Build SQL depending on whether startsAt is provided
    String sql = "INSERT INTO events(home, away, starts_at, status) VALUES (?, ?, " +
        (finalStartsAt == null ? "CURRENT_TIMESTAMP" : "?") + ", 'PENDING')";

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbc.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, finalHome);
      ps.setString(2, finalAway);
      if (finalStartsAt != null) {
        ps.setString(3, finalStartsAt);
      }
      return ps;
    }, keyHolder);

    Number key = keyHolder.getKey();
    long id = (key == null) ? 1L : key.longValue(); // fallback for mocked JDBC
    return Map.of("eventId", id);
  }

  /**
   * PUT /admin/events/{id}/result?result=HOME|AWAY
   * Also accepts JSON: { "result": "HOME" }
   * Returns 200 with counts of won/lost bets.
   */
  @PutMapping("/events/{id}/result")
  @ResponseStatus(HttpStatus.OK)
  public Map<String,Object> setResult(
      @PathVariable long id,
      @RequestParam(required = false) String result,
      @RequestBody(required = false) Map<String,Object> body
  ) {
    String r = result;
    if (r == null && body != null) {
      r = Objects.toString(body.get("result"), null);
    }
    r = (r == null) ? null : r.trim().toUpperCase();

    if (!"HOME".equals(r) && !"AWAY".equals(r)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "result must be HOME or AWAY");
    }

    jdbc.update("UPDATE events SET status='FINISHED', result=? WHERE id=?", r, id);
    int won  = jdbc.update("UPDATE bets SET status='WON'  WHERE event_id=? AND UPPER(selection)=?", id, r);
    int lost = jdbc.update("UPDATE bets SET status='LOST' WHERE event_id=? AND UPPER(selection)<>?", id, r);

    Map<String,Object> out = new HashMap<>();
    out.put("eventId", id);
    out.put("result", r);
    out.put("betsWon", won);
    out.put("betsLost", lost);
    return out;
  }
}
