package com.example.sportsbook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BetController {

  private final JdbcTemplate jdbc;
  public BetController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  public static record BetRequest(long userId, long eventId, String selection, int stakeCents) {}

  @PostMapping("/bets")
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String,Object> placeBet(@RequestBody BetRequest req) {
    // 1) normalize + validate selection & stake
    String sel = req.selection() == null ? "" : req.selection().trim().toUpperCase();
    if (!"HOME".equals(sel) && !"AWAY".equals(sel)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "selection must be HOME or AWAY");
    }
    if (req.stakeCents() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stakeCents must be > 0");
    }

    // 2) ensure event exists
    try {
      jdbc.queryForObject("SELECT id FROM events WHERE id = ?", Long.class, req.eventId());
    } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "event does not exist");
    }

    // 3) fetch decimal price for MONEYLINE & selection
    // Works whether odds table has odds_decimal (JPA) or legacy `decimal`
    BigDecimal decimalPrice;
    try {
      decimalPrice = jdbc.queryForObject("""
        SELECT COALESCE(o.odds_decimal, o.`decimal`)
          FROM markets m
          JOIN odds o ON o.market_id = m.id
         WHERE m.event_id = ?
           AND m.type = 'MONEYLINE'
           AND o.selection = ?
         LIMIT 1
      """, BigDecimal.class, req.eventId(), sel);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "selection not available for this event");
    }

    // 4) compute potential payout (rounded to cents)
    BigDecimal stake = new BigDecimal(req.stakeCents());
    int potentialPayoutCents = stake.multiply(decimalPrice)
                                    .setScale(0, RoundingMode.HALF_UP)
                                    .intValueExact();

    // 5) insert bet (status OPEN/PENDING semantics kept simple)
    jdbc.update("""
      INSERT INTO bets(user_id, event_id, selection, stake_cents, potential_payout_cents, status, state)
      VALUES (?,?,?,?,?,'OPEN','OPEN')
    """, req.userId(), req.eventId(), sel, req.stakeCents(), potentialPayoutCents);

    Long betId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

    return Map.of(
      "id", betId,
      "userId", req.userId(),
      "eventId", req.eventId(),
      "selection", sel,
      "decimal", decimalPrice,
      "stakeCents", req.stakeCents(),
      "potentialPayoutCents", potentialPayoutCents,
      "state", "OPEN",
      "status", "OPEN"
    );
  }

  @GetMapping("/users/{userId}/bets")
  public java.util.List<Map<String, Object>> listUserBets(@PathVariable long userId) {
    // Join to events using New-bets schema names for display
    return jdbc.queryForList("""
      SELECT
        b.id, b.event_id,
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
        b.state, COALESCE(e.result,'') AS result
      FROM bets b
      JOIN events e ON e.id = b.event_id
     WHERE b.user_id = ?
     ORDER BY b.placed_at DESC
    """, userId);
  }
}
