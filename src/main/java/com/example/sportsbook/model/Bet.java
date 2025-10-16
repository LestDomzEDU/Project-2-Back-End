package com.example.sportsbook.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bet {
    private Long id;
    private Long eventId;
    private Long userId;
    private BigDecimal amount;
    private String selection;   // "HOME" or "AWAY"
    private Integer odds;       // e.g., 110, -150
    private String status;      // "pending" or "settled"
    private String result;      // "", "HOME", or "AWAY"
    private LocalDateTime createdAt;

    public Bet() { }

    public Bet(Long id, Long eventId, Long userId, BigDecimal amount,
               String selection, Integer odds, String status, String result,
               LocalDateTime createdAt) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.amount = amount;
        this.selection = selection;
        this.odds = odds;
        this.status = status;
        this.result = result;
        this.createdAt = createdAt;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getSelection() { return selection; }
    public void setSelection(String selection) { this.selection = selection; }

    public Integer getOdds() { return odds; }
    public void setOdds(Integer odds) { this.odds = odds; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
