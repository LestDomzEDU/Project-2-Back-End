// src/main/java/com/example/sportsbook/bets/dto/BetUpdateRequest.java
package com.example.sportsbook.bets.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record BetUpdateRequest(
  @DecimalMin("1.01") BigDecimal oddsDecimal,
  @DecimalMin("0.01") BigDecimal stake,
  String selection
) {}
