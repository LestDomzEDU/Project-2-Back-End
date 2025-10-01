package com.example.sportsbook.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BetController {
  private final JdbcTemplate jdbc;

  public BetController(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @PostMapping("/bets")
  public Map<String, Object> placeBet(@RequestBody BetRequest req) {
    // --- Validate selection & stake ---
    String sel = req.selection() == null ? "" : req.selection().trim().toUpperCase();
    if (!"HOME".equals(sel) && !"AWAY".equals(sel)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "selection must be HOME or AWAY");
    }
    if (req.stakeCents() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stakeCents must be > 0");
    }

    // --- User must exist ---
    Integer userExists = jdbc.queryForObject(
            "SELECT COUNT(*) FROM users WHERE id = ?",
            Integer.class, req.userId());
    if (userExists == null || userExists == 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user does not exist");
    }

    // --- Event must exist & be SCHEDULED ---
    Map<String, Object> evt = jdbc.queryForMap(
            "SELECT status FROM events WHERE id = ?",
            req.eventId());
    String status = String.valueOf(evt.get("status"));
    if (!"SCHEDULED".equalsIgnoreCase(status)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "event is not open for betting");
    }

    // --- Get decimal odds for the selection (required to compute potential payout) ---
    BigDecimal decOdds;
    try {
      decOdds = jdbc.queryForObject("""
          SELECT o.decimal
          FROM markets m
          JOIN odds o ON o.market_id = m.id
          WHERE m.event_id = ? AND m.type = 'MONEYLINE' AND o.selection = ?
          """, BigDecimal.class, req.eventId(), sel);
    } catch (EmptyResultDataAccessException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no odds available for selection");
    }

    // --- Compute potential payout in cents (stake * decimal odds) ---
    BigDecimal stake = new BigDecimal(req.stakeCents());
    int potentialPayoutCents = stake.multiply(decOdds)
            .setScale(0, RoundingMode.HALF_UP)
            .intValue();

    // --- Insert bet (schema WITHOUT price_american; WITH potential_payout_cents) ---
    Long betId = jdbc.queryForObject("""
        INSERT INTO bets (user_id, event_id, selection, stake_cents, potential_payout_cents)
        VALUES (?,?,?,?,?) RETURNING id
        """, Long.class, req.userId(), req.eventId(), sel, req.stakeCents(), potentialPayoutCents);

    Map<String, Object> out = new LinkedHashMap<>();
    out.put("id", betId);
    out.put("state", "PENDING");
    out.put("potentialPayoutCents", potentialPayoutCents);
    return out;
  }

  @GetMapping("/users/{userId}/bets")
  public List<Map<String, Object>> listUserBets(@PathVariable long userId) {
    // We compute a display-only price_american via subquery so the frontend
    // can still show it even though it's not stored on the bet row.
    return jdbc.queryForList("""
        SELECT
          b.id,
          b.event_id,
          e.league,
          e.home_team,
          e.away_team,
          e.start_time,
          b.selection,
          b.stake_cents,
          b.potential_payout_cents,
          -- compute current american price from odds for this bet's selection
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
}
