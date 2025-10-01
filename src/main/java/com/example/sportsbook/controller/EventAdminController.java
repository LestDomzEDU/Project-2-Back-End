package com.example.sportsbook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class EventAdminController {
  private final JdbcTemplate jdbc;
  public EventAdminController(JdbcTemplate jdbc){ this.jdbc = jdbc; }

  record CreateEventRequest(String league, String homeTeam, String awayTeam, String startTimeIso) {}

  @PostMapping("/events")
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String,Object> createEvent(@RequestBody CreateEventRequest req){
    long eventId = jdbc.queryForObject(
      "INSERT INTO events (league, home_team, away_team, start_time) VALUES (?,?,?,CAST(? AS TIMESTAMPTZ)) RETURNING id",
      Long.class, req.league(), req.homeTeam(), req.awayTeam(), req.startTimeIso()
    );
    long marketId = jdbc.queryForObject(
      "INSERT INTO markets (event_id, type) VALUES (?, 'MONEYLINE') RETURNING id",
      Long.class, eventId
    );
    jdbc.update("INSERT INTO odds (market_id, selection, american, decimal) VALUES (?,?,?,?)",
      marketId, "HOME", -110, 1.91);
    jdbc.update("INSERT INTO odds (market_id, selection, american, decimal) VALUES (?,?,?,?)",
      marketId, "AWAY", -110, 1.91);

    Map<String,Object> out = new HashMap<>();
    out.put("id", eventId);
    return out;
  }

  record ResultRequest(String result) {}

  @PutMapping("/events/{id}/result")
  public Map<String,Object> setResult(@PathVariable long id, @RequestBody ResultRequest req){
    String result = req.result();
    if (!"HOME".equalsIgnoreCase(result) && !"AWAY".equalsIgnoreCase(result)){
      throw new IllegalArgumentException("result must be HOME or AWAY");
    }
    jdbc.update("UPDATE events SET result = ?, status = 'FINISHED' WHERE id = ?", result.toUpperCase(), id);

    // settle bets
    int won = jdbc.update("UPDATE bets SET state='WON', settled_at=NOW() WHERE event_id=? AND selection = ?", id, result.toUpperCase());
    int lost = jdbc.update("UPDATE bets SET state='LOST', settled_at=NOW() WHERE event_id=? AND selection <> ?", id, result.toUpperCase());

    Map<String,Object> out = new HashMap<>();
    out.put("eventId", id);
    out.put("result", result.toUpperCase());
    out.put("betsWon", won);
    out.put("betsLost", lost);
    return out;
  }
}
