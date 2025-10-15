// src/main/java/com/example/sportsbook/bets/BetsController.java
package com.example.sportsbook.bets;


import com.example.sportsbook.bets.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sportsbook.bets.dto.PlaceBetRequest;
import com.example.sportsbook.bets.dto.BetResponse;


import java.util.List;


@RestController
@RequestMapping("/api/bets")
public class BetsController {
private final BettingService service;


public BetsController(BettingService service) { this.service = service; }


// TEMP: identify bettor from header while OAuth is off
private String bettorFromHeader(String header) {
return (header == null || header.isBlank()) ? "guest" : header.trim();
}


@PostMapping
public ResponseEntity<BetResponse> place(@RequestHeader(value = "X-PLAYER-ID", required = false) String player,
@Valid @RequestBody PlaceBetRequest req) {
var bet = service.placeBet(bettorFromHeader(player), req);
return ResponseEntity.ok(BetResponse.from(bet));
}


@GetMapping
public List<BetResponse> myBets(@RequestHeader(value = "X-PLAYER-ID", required = false) String player) {
return service.listForBettor(bettorFromHeader(player)).stream().map(BetResponse::from).toList();
}
}