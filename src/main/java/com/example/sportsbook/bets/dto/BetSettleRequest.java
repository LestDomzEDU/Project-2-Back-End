// src/main/java/com/example/sportsbook/bets/dto/BetSettleRequest.java
package com.example.sportsbook.bets.dto;

import jakarta.validation.constraints.NotNull;
import com.example.sportsbook.bets.BetStatus;

public record BetSettleRequest(@NotNull BetStatus result) {}

