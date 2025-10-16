package com.example.sportsbook.controller;

import com.example.sportsbook.dto.CreateBetRequest;
import com.example.sportsbook.model.Bet;
import com.example.sportsbook.repository.BetRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final BetRepository betRepository;

    public BetController(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    @GetMapping
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(betRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateBetRequest req) {
        // In a real app, userId would come from auth; for now hard-code to 100L.
        Bet bet = new Bet();
        bet.setEventId(req.eventId());
        bet.setUserId(100L);
        bet.setAmount(req.amount());
        bet.setSelection(req.selection());
        bet.setOdds(req.odds());
        bet.setStatus("pending");
        bet.setResult("");
        bet.setCreatedAt(LocalDateTime.now());

        betRepository.create(bet);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
