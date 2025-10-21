package com.example.sportsbook.bets;
import com.example.sportsbook.bets.dto.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal; import java.util.*;
@Service public class BetService {
  private final BetRepository repo; public BetService(BetRepository repo){this.repo=repo;}
  @Transactional(readOnly=true) public List<Bet> listAll(){return repo.findAll();}
  @Transactional(readOnly=true) public List<Bet> listByEvent(Long id){return repo.findByEventId(id);}
  @Transactional(readOnly=true) public List<Bet> listByUser(Long id){return repo.findByUserId(id);}
  @Transactional public Bet create(BetCreateRequest r){ Bet b=new Bet(); b.setEventId(r.eventId()); b.setUserId(r.userId()); b.setSelection(r.selection().toUpperCase()); b.setOddsDecimal(r.oddsDecimal()); b.setStake(r.stake()); b.setBettorRef(r.bettorRef()); b.setStatus(BetStatus.PENDING); return repo.save(b);}
  @Transactional public Bet update(Long id, BetUpdateRequest r){ Bet b=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Bet not found")); if(b.getStatus()!=BetStatus.PENDING) throw new IllegalArgumentException("Only PENDING bets can be updated"); if(r.selection()!=null) b.setSelection(r.selection().toUpperCase()); if(r.oddsDecimal()!=null) b.setOddsDecimal(r.oddsDecimal()); if(r.stake()!=null) b.setStake(r.stake()); return repo.save(b);}
  @Transactional public void delete(Long id){ Bet b=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Bet not found")); if(b.getStatus()==BetStatus.PENDING){b.setStatus(BetStatus.CANCELED); repo.save(b);} else repo.delete(b);}
  @Transactional public Bet settle(Long id, BetSettleRequest r){ Bet b=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Bet not found")); if(b.getStatus()!=BetStatus.PENDING) throw new IllegalArgumentException("Only PENDING bets can be settled"); switch(r.result()){ case WON -> b.setStatus(BetStatus.WON); case LOST -> b.setStatus(BetStatus.LOST); case VOID -> b.setStatus(BetStatus.VOID); } return repo.save(b);}
  public BigDecimal potentialPayout(BigDecimal stake, BigDecimal decimalOdds){ return stake.multiply(decimalOdds); }
}
