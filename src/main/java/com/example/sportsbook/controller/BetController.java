package com.example.sportsbook.controller;

import com.example.sportsbook.dto.CreateBetRequest;
import com.example.sportsbook.model.Bet;
import com.example.sportsbook.repository.BetRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final BetRepository betRepository;

    public BetController(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    @GetMapping
    public List<Bet> all() {
        return betRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Bet> create(@Valid @RequestBody CreateBetRequest req) {
        Bet toSave = new Bet();
        toSave.setEventId(req.getEventId());
        toSave.setUserId(req.getUserId());
        toSave.setAmount(req.getAmount());
        toSave.setSelection(req.getSelection());
        toSave.setOdds(req.getOdds());
        toSave.setResult("pending"); // always pending on create

        Bet saved = betRepository.create(toSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public Bet get(@PathVariable Long id) {
        return betRepository.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        int n = betRepository.delete(id);
        return n > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/result")
    public ResponseEntity<Void> updateResult(@PathVariable Long id, @RequestBody String result) {
        int n = betRepository.updateResult(id, result);
        return n > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
