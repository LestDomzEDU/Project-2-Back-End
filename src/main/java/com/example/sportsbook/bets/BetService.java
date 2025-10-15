package com.example.sportsbook.bets;

import com.example.sportsbook.bets.dto.CreateBetRequest;
import com.example.sportsbook.bets.dto.UpdateBetStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BetService {
  private final BetRepository repo;
  public BetService(BetRepository repo) { this.repo = repo; }

  @Transactional
  public Bet create(CreateBetRequest r) {
    var bet = new Bet();
    bet.setBettorRef(r.bettorRef);
    bet.setEventId(r.eventId);
    bet.setSelection(r.selection.toUpperCase());
    bet.setOdds(r.odds);
    bet.setStake(r.stake);
    bet.setStatus("NEW");
    return repo.save(bet);
  }

  public List<Bet> listAll() { return repo.findAll(); }
  public List<Bet> listByBettor(String ref) { return repo.findByBettorRefOrderByCreatedAtDesc(ref); }

  @Transactional
  public Bet updateStatus(Long id, UpdateBetStatusRequest r) {
    var bet = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Bet not found: " + id));
    bet.setStatus(r.status.toUpperCase());
    return bet;
  }

  @Transactional
  public void delete(Long id) { repo.deleteById(id); }
}
