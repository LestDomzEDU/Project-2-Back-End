// src/main/java/com/example/sportsbook/admin/BetAdminController.java
package com.example.sportsbook.admin;


import com.example.sportsbook.bets.BettingService;
import com.example.sportsbook.bets.dto.SetResultRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/bets")
public class BetAdminController {
private final BettingService service;
public BetAdminController(BettingService service) { this.service = service; }


@PostMapping("/settle")
public ResponseEntity<?> settle(@Valid @RequestBody SetResultRequest req) {
int updated = service.settleForEvent(req);
return ResponseEntity.ok().body("Settled " + updated + " bets for event " + req.eventId);
}
}