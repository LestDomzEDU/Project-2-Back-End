package com.example.sportsbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * JSON body for POST /api/bets:
 * {
 *   "eventId": 1,
 *   "amount": 50.00,
 *   "selection": "HOME",
 *   "odds": 110
 * }
 */
public record CreateBetRequest(
        @NotNull(message = "eventId is required")
        Long eventId,

        @NotNull(message = "amount is required")
        @Positive(message = "amount must be positive")
        BigDecimal amount,

        @NotBlank(message = "selection is required")
        @Pattern(regexp = "HOME|AWAY", message = "selection must be either 'HOME' or 'AWAY'")
        String selection,

        @NotNull(message = "odds is required")
        Integer odds
) {}
