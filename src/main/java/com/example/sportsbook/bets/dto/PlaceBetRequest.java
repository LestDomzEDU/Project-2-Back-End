// src/main/java/com/example/sportsbook/bets/dto/PlaceBetRequest.java
package com.example.sportsbook.bets.dto;


import com.example.sportsbook.bets.Selection;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;


public class PlaceBetRequest {
@NotBlank
public String eventId;


@NotNull
public Selection selection;


@NotNull @DecimalMin("1.00")
public BigDecimal stake;


@NotNull @DecimalMin("1.01")
public BigDecimal oddsDecimal; // front-end quoted odds (decimal)
}