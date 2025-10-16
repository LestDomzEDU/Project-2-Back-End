package com.example.sportsbook.repository;

import com.example.sportsbook.model.Bet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BetRepository {

    private final JdbcTemplate jdbcTemplate;

    public BetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Bet> findAll() {
        String sql = """
            SELECT id, event_id, user_id, amount, selection, odds, status, result, created_at
            FROM bets
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, new BetRowMapper());
    }

    public int create(Bet bet) {
        String sql = """
            INSERT INTO bets (event_id, user_id, amount, selection, odds, status, result, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        return jdbcTemplate.update(
                sql,
                bet.getEventId(),
                bet.getUserId(),
                bet.getAmount(),
                bet.getSelection(),
                bet.getOdds(),
                bet.getStatus(),
                bet.getResult(),
                Timestamp.valueOf(bet.getCreatedAt())
        );
    }

    private static class BetRowMapper implements RowMapper<Bet> {
        @Override
        public Bet mapRow(ResultSet rs, int rowNum) throws SQLException {
            Bet b = new Bet();
            b.setId(rs.getLong("id"));
            b.setEventId(rs.getLong("event_id"));
            b.setUserId(rs.getLong("user_id"));
            b.setAmount(rs.getBigDecimal("amount"));
            b.setSelection(rs.getString("selection"));
            b.setOdds((Integer) rs.getObject("odds")); // handles NULL safely
            b.setStatus(rs.getString("status"));
            b.setResult(rs.getString("result"));
            Timestamp ts = rs.getTimestamp("created_at");
            b.setCreatedAt(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());
            return b;
        }
    }
}
