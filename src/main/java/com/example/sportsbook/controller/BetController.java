package com.example.sportsbook.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BetController {

  private final JdbcTemplate jdbc;

  public BetController(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

@GetMapping("/users/{userId}/bets")
public List<Map<String, Object>> listUserBets(@PathVariable long userId) {
  return jdbc.queryForList("""
      SELECT
        b.id,
        b.event_id,
        e.league,
        e.home_team AS home,
        e.away_team AS away,
        e.start_time AS starts_at,
        b.selection,
        b.stake_cents,
        b.potential_payout_cents,
        (
          SELECT o.american
            FROM markets m
            JOIN odds o ON o.market_id = m.id
           WHERE m.event_id = b.event_id
             AND m.type = 'MONEYLINE'
             AND o.selection = b.selection
           LIMIT 1
        ) AS price_american,
        b.state,
        COALESCE(e.result,'') AS result
      FROM bets b
      JOIN events e ON e.id = b.event_id
     WHERE b.user_id = ?
     ORDER BY b.placed_at DESC
  """, userId);
}


@PostMapping("/bets")
@ResponseStatus(HttpStatus.CREATED)
public Map<String,Object> placeBet(@RequestBody BetRequest req) {
  String sel = req.selection() == null ? "" : req.selection().trim().toUpperCase();
  if (!"HOME".equals(sel) && !"AWAY".equals(sel)) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "selection must be HOME or AWAY");
  }

    // Validate event exists and is PENDING
    Map<String,Object> event;
    try {
      event = jdbc.queryForMap("""
          SELECT id, status
            FROM events
           WHERE id = ?
          """, req.eventId());
    } catch (EmptyResultDataAccessException e) {
      throw new IllegalArgumentException("event not found");
    }
    if (!"PENDING".equals(String.valueOf(event.get("status")))) {
      throw new IllegalArgumentException("event is not PENDING");
    }

    // Make sure MONEYLINE market & selection exists and get decimal price
    Map<String,Object> price;
    try {
      price = jdbc.queryForMap("""
          SELECT o.decimal
            FROM markets m
            JOIN odds o ON o.market_id = m.id
           WHERE m.event_id = ?
             AND m.type = 'MONEYLINE'
             AND o.selection = ?
          """, req.eventId(), sel);
    } catch (EmptyResultDataAccessException e) {
      throw new IllegalArgumentException("selection not available");
    }

    // Calculate potential return (stake * decimal odds), rounded to 2 decimals
    BigDecimal stake = new BigDecimal(req.stakeCents()).movePointLeft(2); // cents -> dollars
    BigDecimal dec = new BigDecimal(price.get("decimal").toString());
    BigDecimal potentialReturn = stake.multiply(dec).setScale(2, RoundingMode.HALF_UP);

    // Insert bet
    jdbc.update("""
        INSERT INTO bets(user_id, event_id, selection, stake_cents, state)
        VALUES (?,?,?,?, 'OPEN')
        """, req.userId(), req.eventId(), sel, req.stakeCents());

    // Return lightweight confirmation payload
    Map<String,Object> out = new LinkedHashMap<>();
    out.put("userId", req.userId());
    out.put("eventId", req.eventId());
    out.put("selection", sel);
    out.put("stakeCents", req.stakeCents());
    out.put("potentialReturn", potentialReturn);
    out.put("state", "OPEN");
    return out;
  }
}
