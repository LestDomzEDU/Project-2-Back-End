package com.example.sportsbook.bets;

import com.example.sportsbook.bets.Bet;
import com.example.sportsbook.bets.BetStatus;
import com.example.sportsbook.bets.dto.BetCreateRequest;
import com.example.sportsbook.bets.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BetService {
  private final BetRepository repo;
  public BetService(BetRepository repo) { this.repo = repo; }

  public List<Bet> listAll() { return repo.findAll(); }
  public Bet get(Long id) { return repo.findById(id).orElseThrow(); }

  @Transactional
public Bet create(BetCreateRequest r) {
  Bet b = new Bet();
  b.setEventId(r.eventId());
  b.setUserId(r.userId());
  b.setSelection(r.selection());
  b.setOddsDecimal(r.oddsDecimal());
  b.setStake(r.stake());
  // bettorRef is optional; set it only if you pass it
  // b.setBettorRef(r.bettorRef());
  b.setStatus(BetStatus.PENDING);
  return repo.save(b);
}

  @Transactional
  public Bet update(Long id, BetUpdateRequest req) {
    Bet b = get(id);
    if (b.getStatus() != BetStatus.PENDING)
      throw new IllegalStateException("Only PENDING bets can be updated.");
    if (req.oddsDecimal() != null) b.setOddsDecimal(req.oddsDecimal());
    if (req.stake() != null) b.setStake(req.stake());
    if (req.selection() != null) b.setSelection(req.selection());
    return repo.save(b);
  }

  @Transactional
  public void delete(Long id) {
    Bet b = get(id);
    if (b.getStatus() == BetStatus.PENDING) {
      b.setStatus(BetStatus.CANCELED);
      repo.save(b);
    } else {
      repo.delete(b);
    }
  }

  @Transactional
  public Bet settle(Long id, BetSettleRequest req) {
    Bet b = get(id);
    if (b.getStatus() != BetStatus.PENDING)
      throw new IllegalStateException("Only PENDING bets can be settled.");
    switch (req.result()) {
      case WON -> b.setStatus(BetStatus.WON);
      case LOST -> b.setStatus(BetStatus.LOST);
      case VOID -> b.setStatus(BetStatus.VOID);
      default -> throw new IllegalArgumentException("Result must be WON/LOST/VOID");
    }
    return repo.save(b);
  }

  public BigDecimal potentialPayout(BigDecimal stake, BigDecimal decimalOdds) {
    return stake.multiply(decimalOdds);
  }
}
