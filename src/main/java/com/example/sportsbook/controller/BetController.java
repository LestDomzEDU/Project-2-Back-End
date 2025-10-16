package com.example.sportsbook.controller;

import com.example.sportsbook.dto.CreateBetRequest;
import com.example.sportsbook.model.Bet;
import com.example.sportsbook.service.BetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final BetService betService;

    public BetController(BetService betService) {
        this.betService = betService;
    }

    @PostMapping
    public ResponseEntity<Bet> create(@Valid @RequestBody CreateBetRequest body) {
        Bet created = betService.createBet(body);
        return ResponseEntity
                .created(URI.create("/api/bets/" + created.getId()))
                .body(created);
    }

    @GetMapping
    public List<Bet> list() {
        return betService.listBets();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        betService.deleteBet(id);
        return ResponseEntity.noContent().build();
    }
}
