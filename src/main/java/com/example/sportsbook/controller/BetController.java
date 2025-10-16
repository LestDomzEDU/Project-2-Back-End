package com.example.sportsbook.controller;

import com.example.sportsbook.dto.CreateBetRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final JdbcTemplate jdbc;

    public BetController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping
    public List<Map<String, Object>> all() {
        return jdbc.queryForList(
                "SELECT id, event_id AS eventId, amount, selection, odds, status " +
                "FROM bets ORDER BY id DESC"
        );
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateBetRequest req, BindingResult br) {

        if (br.hasErrors()) {
            var details = br.getFieldErrors()
                    .stream()
                    .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(Map.of(
                    "ok", false,
                    "error", "Validation failed",
                    "details", details
            ));
        }

        int updated = jdbc.update(
                "INSERT INTO bets (event_id, amount, selection, odds, status) VALUES (?, ?, ?, ?, ?)",
                req.eventId(), req.amount(), req.selection(), req.odds(), "PENDING"
        );

        if (updated == 1) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "ok", true,
                            "message", "Bet created",
                            "eventId", req.eventId(),
                            "selection", req.selection(),
                            "odds", req.odds(),
                            "amount", req.amount()
                    )
            );
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("ok", false, "message", "Insert failed"));
    }
}
