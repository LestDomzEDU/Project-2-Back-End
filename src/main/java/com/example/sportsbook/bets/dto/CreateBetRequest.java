package com.example.sportsbook.bets.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CreateBetRequest {
  @NotBlank public String bettorRef;
  @NotNull  public Long eventId;
  @NotBlank public String selection;
  @NotNull @DecimalMin("1.0001") public BigDecimal odds;
  @NotNull @DecimalMin("0.01")  public BigDecimal stake;
}
