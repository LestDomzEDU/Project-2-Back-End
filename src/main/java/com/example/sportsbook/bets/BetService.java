package com.example.sportsbook.bets;

import com.example.sportsbook.bets.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BetService {
  private final BetRepository repo;

  public BetService(BetRepository repo) { this.repo = repo; }

  @Transactional(readOnly = true)
  public List<Bet> list() { return repo.findAll(); }

  @Transactional(readOnly = true)
  public Bet get(Long id) { return repo.findById(id).orElseThrow(); }

  @Transactional
  public Bet create(BetCreateRequest req) {
    Bet b = new Bet();
    b.setEventId(req.eventId());
    b.setUserId(req.userId());
    b.setSelection(req.selection().toUpperCase());
    b.setOddsDecimal(req.oddsDecimal());
    b.setStake(req.stake());
    b.setBettorRef(req.bettorRef());
    b.setStatus(BetStatus.PENDING);
    return repo.save(b);
  }

  @Transactional
  public Bet update(Long id, BetUpdateRequest req) {
    Bet b = repo.findById(id).orElseThrow();
    if (b.getStatus() != BetStatus.PENDING) {
      throw new IllegalStateException("Only PENDING bets can be updated.");
    }
    if (req.selection() != null && !req.selection().isBlank()) {
      b.setSelection(req.selection().toUpperCase());
    }
    if (req.oddsDecimal() != null) {
      b.setOddsDecimal(req.oddsDecimal());
    }
    if (req.stake() != null) {
      b.setStake(req.stake());
    }
    return repo.save(b);
  }

  @Transactional
  public void delete(Long id) {
    Bet b = repo.findById(id).orElseThrow();
    if (b.getStatus() == BetStatus.PENDING) {
      b.setStatus(BetStatus.CANCELED);
      repo.save(b);
    } else {
      repo.delete(b);
    }
  }

  @Transactional
  public Bet settle(Long id, BetSettleRequest req) {
    Bet b = repo.findById(id).orElseThrow();
    if (b.getStatus() != BetStatus.PENDING) {
      throw new IllegalStateException("Only PENDING bets can be settled.");
    }
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
