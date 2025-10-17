package com.example.sportsbook.bets;

import com.example.sportsbook.bets.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bets")
@CrossOrigin(origins = "*")
public class BetController {
  private final BetService service;
  public BetController(BetService service) { this.service = service; }

  @GetMapping
  public List<Bet> listAll() { return service.listAll(); }

  @GetMapping("/event/{eventId}")
  public List<Bet> byEvent(@PathVariable Long eventId) { return service.listByEvent(eventId); }

  @GetMapping("/user/{userId}")
  public List<Bet> byUser(@PathVariable Long userId) { return service.listByUser(userId); }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Bet create(@Valid @RequestBody BetCreateRequest req) { return service.create(req); }

  @PutMapping("/{id}")
  public Bet update(@PathVariable Long id, @Valid @RequestBody BetUpdateRequest req) { return service.update(id, req); }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancelOrDelete(@PathVariable Long id) { service.delete(id); }

  @PostMapping("/{id}/settle")
  public Bet settle(@PathVariable Long id, @Valid @RequestBody BetSettleRequest req) { return service.settle(id, req); }
}
