package com.example.sportsbook.bets.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateBetStatusRequest {
  @NotBlank public String status; // WON, LOST, VOID
}
