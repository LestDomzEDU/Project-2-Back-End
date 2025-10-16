package com.example.sportsbook.repository;

import com.example.sportsbook.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EventRepository {

    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Correct SQL: COALESCE on the column, and backtick `status`
    private static final String SELECT_ALL_SQL = """
        SELECT id,
               league,
               home_team,
               away_team,
               start_time,
               `status`,
               COALESCE(`result`, '') AS result
        FROM events
        ORDER BY start_time ASC
        """;

    public List<Event> findAll() {
        return jdbc.query(SELECT_ALL_SQL, new EventRowMapper());
    }

    private static class EventRowMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event e = new Event();
            e.setId(rs.getLong("id"));
            e.setLeague(rs.getString("league"));
            e.setHomeTeam(rs.getString("home_team"));
            e.setAwayTeam(rs.getString("away_team"));

            // Map DATETIME/TIMESTAMP to LocalDateTime safely
            Timestamp ts = rs.getTimestamp("start_time");
            LocalDateTime ldt = (ts != null) ? ts.toLocalDateTime() : null;
            e.setStartTime(ldt);

            e.setStatus(rs.getString("status"));
            e.setResult(rs.getString("result"));
            return e;
        }
    }
}
