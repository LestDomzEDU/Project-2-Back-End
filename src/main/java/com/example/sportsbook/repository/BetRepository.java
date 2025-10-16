package com.example.sportsbook.repository;

import com.example.sportsbook.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Long> {}
