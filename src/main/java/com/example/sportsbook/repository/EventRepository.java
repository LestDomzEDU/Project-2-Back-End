package com.example.sportsbook.repository;

import com.example.sportsbook.model.Event;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EventRepository {

    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Event> ROW_MAPPER = (rs, rowNum) -> {
        Event e = new Event();
        e.setId(rs.getLong("id"));
        e.setLeague(rs.getString("league"));
        e.setHomeTeam(rs.getString("home_team"));
        e.setAwayTeam(rs.getString("away_team"));
        Timestamp ts = rs.getTimestamp("start_time");
        e.setStartTime(ts != null ? ts.toLocalDateTime() : null);
        e.setStatus(rs.getString("status"));        // alias below gives us 'status'
        String result = rs.getString("result");
        e.setResult(result != null ? result : "");
        return e;
    };

    /** Return all events ordered by start time. */
    public List<Event> all() {
        final String sql = """
            SELECT id, league, home_team, away_team, start_time,
                   `status` AS status,
                   COALESCE(result, '') AS result
            FROM events
            ORDER BY start_time ASC
            """;
        return jdbc.query(sql, ROW_MAPPER);
    }

    /** Find a single event by id. */
    public Event getById(Long id) throws EmptyResultDataAccessException {
        final String sql = """
            SELECT id, league, home_team, away_team, start_time,
                   `status` AS status,
                   COALESCE(result, '') AS result
            FROM events
            WHERE id = ?
            """;
        return jdbc.queryForObject(sql, ROW_MAPPER, id);
    }

    /** Create an event and return it with the generated id. */
    public Event create(Event e) {
        final String sql = """
            INSERT INTO events (league, home_team, away_team, start_time, `status`, result)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, e.getLeague());
            ps.setString(2, e.getHomeTeam());
            ps.setString(3, e.getAwayTeam());
            LocalDateTime st = e.getStartTime();
            ps.setTimestamp(4, st != null ? Timestamp.valueOf(st) : null);
            ps.setString(5, e.getStatus());
            ps.setString(6, e.getResult() == null ? "" : e.getResult());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            e.setId(keyHolder.getKey().longValue());
        }
        return e;
    }

    /** Update only the result field for an event id. Returns rows affected. */
    public int updateResult(Long id, String result) {
        final String sql = "UPDATE events SET result = ? WHERE id = ?";
        return jdbc.update(sql, result, id);
    }

    /** Delete by id. Returns rows affected. */
    public int delete(Long id) {
        final String sql = "DELETE FROM events WHERE id = ?";
        return jdbc.update(sql, id);
    }
}
