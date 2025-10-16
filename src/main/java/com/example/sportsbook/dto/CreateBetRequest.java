package com.example.sportsbook.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreateBetRequest {

    @NotNull
    private Long eventId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private String selection; // "HOME" or "AWAY"

    @NotNull
    private Integer odds;

    // --- Getters (these names match what your service/controller call) ---
    public Long getEventId() { return eventId; }
    public BigDecimal getAmount() { return amount; }
    public String getSelection() { return selection; }
    public Integer getOdds() { return odds; }

    // --- Setters (needed by Jackson) ---
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setSelection(String selection) { this.selection = selection; }
    public void setOdds(Integer odds) { this.odds = odds; }
}
