package com.example.sportsbook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@CrossOrigin // remove or restrict if you don't want wide-open CORS
public class EventController {

    private final JdbcTemplate jdbc;

    public EventController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // ---------- SQL (safe for MariaDB/MySQL) ----------
    private static final String SELECT_ALL_SQL = """
            SELECT id, league, home_team, away_team, start_time,
                   `status`, COALESCE(`result`, '') AS result
            FROM `events`
            ORDER BY start_time ASC
            """;

    private static final String SELECT_ONE_SQL = """
            SELECT id, league, home_team, away_team, start_time,
                   `status`, COALESCE(`result`, '') AS result
            FROM `events`
            WHERE id = ?
            """;

    // ---------- Mappers / DTO ----------
    private static final RowMapper<EventDto> EVENT_MAPPER = new RowMapper<>() {
        @Override
        public EventDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            EventDto e = new EventDto();
            e.setId(rs.getLong("id"));
            e.setLeague(rs.getString("league"));
            e.setHomeTeam(rs.getString("home_team"));
            e.setAwayTeam(rs.getString("away_team"));
            // Using String keeps things simple regardless of DB column type
            e.setStartTime(rs.getString("start_time"));
            e.setStatus(rs.getString("status"));
            e.setResult(rs.getString("result"));
            return e;
        }
    };

    // ---------- Endpoints ----------

    @GetMapping
    public List<EventDto> getAll() {
        return jdbc.query(SELECT_ALL_SQL, EVENT_MAPPER);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getOne(@PathVariable long id) {
        List<EventDto> rows = jdbc.query(SELECT_ONE_SQL, EVENT_MAPPER, id);
        Optional<EventDto> first = rows.stream().findFirst();
        return first.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ---------- DTO ----------
    public static class EventDto {
        private long id;
        private String league;
        private String homeTeam;
        private String awayTeam;
        private String startTime;
        private String status;
        private String result;

        public long getId() { return id; }
        public void setId(long id) { this.id = id; }
        public String getLeague() { return league; }
        public void setLeague(String league) { this.league = league; }
        public String getHomeTeam() { return homeTeam; }
        public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
        public String getAwayTeam() { return awayTeam; }
        public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
        public String getStartTime() { return startTime; }
        public void setStartTime(String startTime) { this.startTime = startTime; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
    }
}
