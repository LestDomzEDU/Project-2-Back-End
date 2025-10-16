package com.example.sportsbook.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateBetRequest(
        @NotNull Long eventId,
        @NotNull @Positive BigDecimal amount,
        @NotBlank @Pattern(regexp = "HOME|AWAY") String selection,
        @NotNull Integer odds
) {}
