package com.example.sportsbook.model;

public class Bet {
    private Long id;
    private Long eventId;
    private Long userId;
    private java.math.BigDecimal amount;
    private String selection;
    private String odds;
    private String result;
    private java.time.LocalDateTime createdAt;

    public Bet() {}

    public Bet(Long id, Long eventId, Long userId,
               java.math.BigDecimal amount, String selection, String odds,
               String result, java.time.LocalDateTime createdAt) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.amount = amount;
        this.selection = selection;
        this.odds = odds;
        this.result = result;
        this.createdAt = createdAt;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public java.math.BigDecimal getAmount() { return amount; }
    public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }

    public String getSelection() { return selection; }
    public void setSelection(String selection) { this.selection = selection; }

    public String getOdds() { return odds; }
    public void setOdds(String odds) { this.odds = odds; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
}
