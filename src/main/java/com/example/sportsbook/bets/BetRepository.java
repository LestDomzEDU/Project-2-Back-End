package com.example.sportsbook.bets;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {
  List<Bet> findByEventId(Long eventId);
  List<Bet> findByUserId(Long userId);
}
