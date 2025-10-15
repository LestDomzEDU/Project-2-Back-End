// src/main/java/com/example/sportsbook/bets/BetRepository.java
package com.example.sportsbook.bets;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface BetRepository extends JpaRepository<Bet, Long> {
List<Bet> findByBettorRefOrderByPlacedAtDesc(String bettorRef);
List<Bet> findByEventIdAndStatus(String eventId, BetStatus status);
}