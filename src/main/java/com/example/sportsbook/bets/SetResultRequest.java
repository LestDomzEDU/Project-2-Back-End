// src/main/java/com/example/sportsbook/bets/dto/SetResultRequest.java
package com.example.sportsbook.bets.dto;


import com.example.sportsbook.bets.Selection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class SetResultRequest {
@NotBlank public String eventId;
@NotNull public Selection winningSelection; // HOME/AWAY/DRAW
}