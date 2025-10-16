package com.example.sportsbook.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CreateBetRequest {

    @NotNull
    private Long eventId;

    @NotNull
    private Long userId;

    @NotNull
    @DecimalMin(value = "1.00", message = "amount must be >= 1.00")
    private BigDecimal amount;

    @NotBlank
    private String selection; // e.g., "Warriors" OR "HOME"/"AWAY" if you prefer—controller won't restrict it

    @NotBlank
    @Pattern(regexp = "^[+-]?\\d+$", message = "odds must look like +110 or -150")
    private String odds;

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getSelection() { return selection; }
    public void setSelection(String selection) { this.selection = selection; }

    public String getOdds() { return odds; }
    public void setOdds(String odds) { this.odds = odds; }
}
