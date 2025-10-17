package com.example.sportsbook.bets.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record BetCreateRequest(
  @NotNull Long eventId,
  @NotNull Long userId,
  @NotBlank String selection,
  @NotNull @DecimalMin("1.01") BigDecimal oddsDecimal,
  @NotNull @DecimalMin("0.01") BigDecimal stake,
  String bettorRef
) { }
