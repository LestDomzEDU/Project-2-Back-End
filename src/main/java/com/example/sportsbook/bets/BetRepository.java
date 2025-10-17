package com.example.sportsbook.bets;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*; 
public interface BetRepository extends JpaRepository<Bet, Long> { List<Bet> findByEventId(Long id); List<Bet> findByUserId(Long id); }
