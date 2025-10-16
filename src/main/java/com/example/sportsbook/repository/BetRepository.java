package com.example.sportsbook.repository;

import com.example.sportsbook.model.Bet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BetRepository {

    private final JdbcTemplate jdbc;

    public BetRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static class BetRowMapper implements RowMapper<Bet> {
        @Override
        public Bet mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Bet(
                rs.getLong("id"),
                rs.getLong("event_id"),
                rs.getLong("user_id"),
                rs.getBigDecimal("amount"),
                rs.getString("selection"),
                rs.getString("odds"),
                rs.getString("result"),
                rs.getTimestamp("created_at") != null
                    ? rs.getTimestamp("created_at").toLocalDateTime()
                    : null
            );
        }
    }

    public List<Bet> findAll() {
        String sql = """
            SELECT id, event_id, user_id, amount, selection, odds, result, created_at
            FROM bets
            ORDER BY created_at DESC
        """;
        return jdbc.query(sql, new BetRowMapper());
    }

    public Bet create(Bet bet) {
        String sql = """
            INSERT INTO bets (event_id, user_id, amount, selection, odds, result)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        jdbc.update(sql,
            bet.getEventId(),
            bet.getUserId(),
            bet.getAmount(),
            bet.getSelection(),
            bet.getOdds(),
            bet.getResult() == null ? "pending" : bet.getResult()
        );

        // return last inserted row
        String fetch = """
            SELECT id, event_id, user_id, amount, selection, odds, result, created_at
            FROM bets
            ORDER BY id DESC
            LIMIT 1
        """;
        return jdbc.queryForObject(fetch, new BetRowMapper());
    }

    public Bet getById(Long id) {
        String sql = """
            SELECT id, event_id, user_id, amount, selection, odds, result, created_at
            FROM bets WHERE id = ?
        """;
        return jdbc.queryForObject(sql, new BetRowMapper(), id);
    }

    public int delete(Long id) {
        return jdbc.update("DELETE FROM bets WHERE id = ?", id);
    }

    public int updateResult(Long id, String result) {
        return jdbc.update("UPDATE bets SET result = ? WHERE id = ?", result, id);
    }
}
