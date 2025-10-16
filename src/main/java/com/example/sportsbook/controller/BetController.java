package com.example.sportsbook.controller;

import com.example.sportsbook.dto.CreateBetRequest;
import com.example.sportsbook.model.Bet;
import com.example.sportsbook.repository.BetRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final BetRepository betRepository;

    // No Lombok; plain constructor
    public BetController(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    @GetMapping
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(betRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateBetRequest req) {
        // call the new overload that takes the four fields
        Bet created = betRepository.create(
                req.eventId(),
                req.amount(),
                req.selection(),
                req.odds()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
