package com.example.sportsbook.repository;

import com.example.sportsbook.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EventRepository {

    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Event> EVENT_ROW_MAPPER = new RowMapper<Event>() {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event e = new Event();
            e.setId(rs.getLong("id"));
            e.setLeague(rs.getString("league"));
            e.setHomeTeam(rs.getString("home_team"));
            e.setAwayTeam(rs.getString("away_team"));
            // IMPORTANT: convert SQL timestamp to LocalDateTime
            e.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
            e.setStatus(rs.getString("status"));
            e.setResult(rs.getString("result")); // already COALESCE'd in SQL
            return e;
        }
    };

    public List<Event> all() {
        // ✅ no backticks; ✅ COALESCE(column, '') not string literal
        final String sql =
            "SELECT id, league, home_team, away_team, start_time, status, " +
            "       COALESCE(result, '') AS result " +
            "FROM events " +
            "ORDER BY start_time ASC";
        return jdbc.query(sql, EVENT_ROW_MAPPER);
    }

    public int create(Event e) {
        final String sql =
            "INSERT INTO events (league, home_team, away_team, start_time, status, result) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
        return jdbc.update(
            sql,
            e.getLeague(),
            e.getHomeTeam(),
            e.getAwayTeam(),
            // Let the driver handle LocalDateTime
            java.sql.Timestamp.valueOf(e.getStartTime()),
            e.getStatus(),
            e.getResult()
        );
    }

    public Event getById(long id) {
        final String sql =
            "SELECT id, league, home_team, away_team, start_time, status, " +
            "       COALESCE(result, '') AS result " +
            "FROM events WHERE id = ?";
        return jdbc.queryForObject(sql, EVENT_ROW_MAPPER, id);
    }

    public int updateResult(long id, String result) {
        final String sql = "UPDATE events SET result = ? WHERE id = ?";
        return jdbc.update(sql, result, id);
    }

    public int delete(long id) {
        final String sql = "DELETE FROM events WHERE id = ?";
        return jdbc.update(sql, id);
    }
}
