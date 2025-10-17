package com.example.sportsbook.bets;
import com.example.sportsbook.bets.dto.*; import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/bets") @CrossOrigin(origins="*")
public class BetController {
  private final BetService svc; public BetController(BetService svc){this.svc=svc;}
  @GetMapping public List<Bet> all(){return svc.listAll();}
  @GetMapping("/event/{eventId}") public List<Bet> byEvent(@PathVariable Long eventId){return svc.listByEvent(eventId);}
  @GetMapping("/user/{userId}") public List<Bet> byUser(@PathVariable Long userId){return svc.listByUser(userId);}
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public Bet create(@Valid @RequestBody BetCreateRequest r){return svc.create(r);}
  @PutMapping("/{id}") public Bet update(@PathVariable Long id, @Valid @RequestBody BetUpdateRequest r){return svc.update(id,r);}
  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){svc.delete(id);}
  @PostMapping("/{id}/settle") public Bet settle(@PathVariable Long id, @Valid @RequestBody BetSettleRequest r){return svc.settle(id,r);}
}
