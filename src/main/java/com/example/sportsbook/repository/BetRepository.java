package com.example.sportsbook.repository;

import com.example.sportsbook.model.Bet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

@Repository
public class BetRepository {

    private final JdbcTemplate jdbc;

    public BetRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Bet> ROW_MAPPER = (rs, rowNum) -> map(rs);

    private static Bet map(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long eventId = rs.getLong("event_id");
        BigDecimal amount = rs.getBigDecimal("amount");
        String selection = rs.getString("selection");
        Integer odds = rs.getInt("odds");
        // Most schemas don’t need status here; if you added one, you can read it similarly.
        return new Bet(id, eventId, amount, selection, odds);
    }

    public List<Bet> findAll() {
        // minimal column list that matches constructor order in Bet
        String sql = "SELECT id, event_id, amount, selection, odds FROM bets ORDER BY id DESC";
        return jdbc.query(sql, ROW_MAPPER);
    }

    /** Existing method (keep, if you already had it)
    public Bet create(Bet bet) { ... }
    */

    // ******** NEW OVERLOAD that matches the controller call ********
    public Bet create(Long eventId, BigDecimal amount, String selection, Integer odds) {
        String sql = "INSERT INTO bets(event_id, amount, selection, odds) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, eventId);
            ps.setBigDecimal(2, amount);
            ps.setString(3, selection);
            ps.setInt(4, odds);
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey() != null ? ((Number) keyHolder.getKey()).longValue() : null;

        // Read back the row to return a fully mapped Bet
        return jdbc.queryForObject(
                "SELECT id, event_id, amount, selection, odds FROM bets WHERE id = ?",
                ROW_MAPPER,
                id
        );
    }
}
