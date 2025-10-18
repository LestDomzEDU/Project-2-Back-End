package com.example.sportsbook.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EventController {

    private final JdbcTemplate jdbc;

    public EventController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * List events.
     * Reads from Maria columns (home_team, away_team, start_time) but aliases to
     * API field names (home, away, starts_at) for compatibility with the app/tests.
     */
    @GetMapping("/events")
    public List<Map<String, Object>> listEvents() {
        final String sql = """
            SELECT
                id,
                league,
                home_team  AS home,
                away_team  AS away,
                start_time AS starts_at,
                status,
                COALESCE(result, '') AS result
            FROM events
            ORDER BY start_time ASC
            """;
        return jdbc.queryForList(sql);
    }

    /**
     * Get a single event by id, including its MONEYLINE odds.
     */
    @GetMapping("/events/{id}")
    public Map<String, Object> getEvent(@PathVariable long id) {
        final String evSql = """
            SELECT
                id,
                league,
                home_team  AS home,
                away_team  AS away,
                start_time AS starts_at,
                status,
                COALESCE(result, '') AS result
            FROM events
            WHERE id = ?
            """;

        Map<String, Object> event;
        try {
            event = jdbc.queryForMap(evSql, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }

        event.put("odds", getMoneylineOdds(id));
        return event;
    }

    /**
     * Convenience endpoint to fetch just the MONEYLINE odds for an event.
     */
    @GetMapping("/events/{id}/odds")
    public List<Map<String, Object>> getEventOdds(@PathVariable long id) {
        // Ensure the event exists for a cleaner 404 instead of 500
        try {
            jdbc.queryForObject("SELECT id FROM events WHERE id = ?", Long.class, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        return getMoneylineOdds(id);
    }

    // ---------- helpers ----------

    private List<Map<String, Object>> getMoneylineOdds(long eventId) {
        // COALESCE handles either odds_decimal (JPA style) or legacy `decimal` column
        final String oddsSql = """
            SELECT
                o.id,
                o.selection,
                o.american,
                COALESCE(o.odds_decimal, o.`decimal`) AS decimal
            FROM markets m
            JOIN odds o ON o.market_id = m.id
            WHERE m.event_id = ?
              AND m.`type` = 'MONEYLINE'
            ORDER BY o.selection
            """;
        return jdbc.queryForList(oddsSql, eventId);
    }
}
