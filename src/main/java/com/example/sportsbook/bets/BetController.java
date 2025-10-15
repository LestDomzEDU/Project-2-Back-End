package com.example.sportsbook.bets;

import com.example.sportsbook.bets.dto.CreateBetRequest;
import com.example.sportsbook.bets.dto.UpdateBetStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bets")
public class BetController {

  private final BetService service;
  public BetController(BetService service) { this.service = service; }

  @PostMapping @ResponseStatus(HttpStatus.CREATED)
  public Bet create(@Valid @RequestBody CreateBetRequest req) { return service.create(req); }

  @GetMapping public List<Bet> listAll() { return service.listAll(); }

  @GetMapping("/bettor/{bettorRef}")
  public List<Bet> listByBettor(@PathVariable String bettorRef) { return service.listByBettor(bettorRef); }

  @PutMapping("/{id}/status")
  public Bet updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateBetStatusRequest req) {
    return service.updateStatus(id, req);
  }

  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) { service.delete(id); }
}
