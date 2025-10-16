package com.example.sportsbook.repository;

import com.example.sportsbook.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class EventRepository {

    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Event> EVENT_ROW_MAPPER = (rs, rowNum) -> {
        Event e = new Event();
        e.setId(rs.getLong("id"));
        e.setLeague(rs.getString("league"));
        e.setHomeTeam(rs.getString("home_team"));
        e.setAwayTeam(rs.getString("away_team"));
        Timestamp ts = rs.getTimestamp("start_time");
        e.setStartTime(ts != null ? ts.toLocalDateTime() : null);
        e.setStatus(rs.getString("status"));
        e.setResult(rs.getString("result")); // aliased below
        return e;
    };

    public List<Event> findAll() {
        // Use backticks for identifiers that might collide with keywords and to be MySQL/MariaDB-safe.
        final String sql = """
            SELECT id, league, home_team, away_team, start_time, `status`,
                   COALESCE(`result`, '') AS result
            FROM events
            ORDER BY start_time ASC
            """;
        return jdbc.query(sql, EVENT_ROW_MAPPER);
    }
}
