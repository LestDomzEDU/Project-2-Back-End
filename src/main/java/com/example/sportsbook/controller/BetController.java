package com.example.sportsbook.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api")
public class BetController {
  private final JdbcTemplate jdbc;
  public BetController(JdbcTemplate jdbc){ this.jdbc = jdbc; }

  // --- DTO ---
  public static class BetRequest {
    public Long userId;
    public Long eventId;
    public String selection;   // "HOME" or "AWAY"
    public Integer stakeCents; // e.g., 1000 = $10.00
  }

  @PostMapping("/bets")
  public Map<String,Object> place(@RequestBody BetRequest r){
    if (r == null || r.userId == null || r.eventId == null || r.selection == null || r.stakeCents == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing fields");
    String sel = r.selection.trim().toUpperCase();
    if (!sel.equals("HOME") && !sel.equals("AWAY"))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "selection must be HOME or AWAY");
    if (r.stakeCents <= 0)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stakeCents must be > 0");

    Integer userExists = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE id = ?", Integer.class, r.userId);
    if (userExists == null || userExists == 0)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not found");

    List<Map<String,Object>> evRows = jdbc.queryForList("SELECT status FROM events WHERE id = ?", r.eventId);
    if (evRows.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "event not found");
    String status = String.valueOf(evRows.get(0).get("status"));
    if (!"SCHEDULED".equalsIgnoreCase(status))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "event not open for betting");

    List<BigDecimal> oddsList = jdbc.query(
      "SELECT o.decimal FROM odds o JOIN markets m ON o.market_id = m.id " +
      "WHERE m.event_id = ? AND m.type = 'MONEYLINE' AND o.selection = ? LIMIT 1",
      (rs, i) -> rs.getBigDecimal(1), r.eventId, sel
    );
    if (oddsList.isEmpty())
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no odds for this selection");
    BigDecimal dec = oddsList.get(0);

    BigDecimal payout = BigDecimal.valueOf(r.stakeCents).multiply(dec).setScale(0, RoundingMode.HALF_UP);
    int potentialPayoutCents = payout.intValueExact();

    Long betId = jdbc.queryForObject(
      "INSERT INTO bets (user_id, event_id, selection, stake_cents, potential_payout_cents, state) " +
      "VALUES (?, ?, ?, ?, ?, 'PENDING') RETURNING id",
      Long.class, r.userId, r.eventId, sel, r.stakeCents, potentialPayoutCents
    );

    Map<String,Object> out = new LinkedHashMap<>();
    out.put("id", betId);
    out.put("userId", r.userId);
    out.put("eventId", r.eventId);
    out.put("selection", sel);
    out.put("stakeCents", r.stakeCents);
    out.put("potentialPayoutCents", potentialPayoutCents);
    out.put("state", "PENDING");
    return out;
  }

  @GetMapping("/users/{userId}/bets")
  public List<Map<String,Object>> history(@PathVariable Long userId){
    return jdbc.queryForList(
      "SELECT b.id, b.event_id, b.selection, b.stake_cents, b.potential_payout_cents, " +
      "b.placed_at, b.state, e.league, e.home_team, e.away_team " +
      "FROM bets b JOIN events e ON b.event_id = e.id " +
      "WHERE b.user_id = ? ORDER BY b.placed_at DESC", userId
    );
  }

  @DeleteMapping("/bets/{id}")
  public Map<String,Object> cancel(@PathVariable Long id){
    // load bet
    List<Map<String,Object>> bets = jdbc.queryForList(
      "SELECT id, event_id, state FROM bets WHERE id = ?", id);
    if (bets.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "bet not found");

    Map<String,Object> b = bets.get(0);
    String state = String.valueOf(b.get("state"));
    if (!"PENDING".equalsIgnoreCase(state))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "only PENDING bets can be cancelled");

    long eventId = ((Number)b.get("event_id")).longValue();
    List<Map<String,Object>> ev = jdbc.queryForList(
      "SELECT status, start_time FROM events WHERE id = ?", eventId);
    if (ev.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "event not found");

    String status = String.valueOf(ev.get(0).get("status"));
    Timestamp start = (Timestamp) ev.get(0).get("start_time");
    if (!"SCHEDULED".equalsIgnoreCase(status) || Instant.now().isAfter(start.toInstant()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "event already started or locked");

    int updated = jdbc.update("UPDATE bets SET state = 'VOID' WHERE id = ? AND state = 'PENDING'", id);
    if (updated != 1) throw new ResponseStatusException(HttpStatus.CONFLICT, "bet state changed; try again");

    return Map.of("id", id, "state", "VOID");
  }
}
