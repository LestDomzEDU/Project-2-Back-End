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

    private static final RowMapper<Event> ROW_MAPPER = new RowMapper<>() {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event e = new Event();
            e.setId(rs.getLong("id"));
            e.setLeague(rs.getString("league"));
            e.setHomeTeam(rs.getString("home_team"));
            e.setAwayTeam(rs.getString("away_team"));

            Timestamp ts = rs.getTimestamp("start_time");
            LocalDateTime start = (ts != null) ? ts.toLocalDateTime() : null;
            e.setStartTime(start);

            e.setStatus(rs.getString("status"));
            e.setResult(rs.getString("result"));
            return e;
        }
    };

    public List<Event> findAll() {
        // Backticks + IFNULL are safest on MariaDB/JawsDB.
        final String sql =
            "SELECT `id`, `league`, `home_team`, `away_team`, `start_time`, " +
            "       `status`, IFNULL(`result`, '') AS `result` " +
            "FROM `events` " +
            "ORDER BY `start_time` ASC";
        return jdbc.query(sql, ROW_MAPPER);
    }
}
