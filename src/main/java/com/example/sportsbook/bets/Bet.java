// src/main/java/com/example/sportsbook/bets/Bet.java
package com.example.sportsbook.bets;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bets")
public class Bet {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "event_id", nullable = false)
  private Long eventId;

  @Column(name = "user_id")
  private Long userId;

  @NotBlank
  @Column(nullable = false, length = 120)
  private String selection; // 'HOME' or 'AWAY'

  @NotNull
  @DecimalMin("1.01")
  @Column(name = "odds_decimal", precision = 8, scale = 4, nullable = false)
  private BigDecimal oddsDecimal;

  @NotNull
  @DecimalMin("0.01")
  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal stake;

  @Column(name = "bettor_ref")
  private String bettorRef;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 32)
  private BetStatus status = BetStatus.PENDING;

  @Column(name = "created_at", updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @PrePersist
  public void onCreate() {
    this.createdAt = Instant.now();
  }

  @PreUpdate
  public void onUpdate() {
    this.updatedAt = Instant.now();
  }

  // getters/setters
  public Long getId() { return id; }
  public Long getEventId() { return eventId; }
  public void setEventId(Long eventId) { this.eventId = eventId; }
  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }
  public String getSelection() { return selection; }
  public void setSelection(String selection) { this.selection = selection; }
  public BigDecimal getOddsDecimal() { return oddsDecimal; }
  public void setOddsDecimal(BigDecimal oddsDecimal) { this.oddsDecimal = oddsDecimal; }
  public BigDecimal getStake() { return stake; }
  public void setStake(BigDecimal stake) { this.stake = stake; }
  public String getBettorRef() { return bettorRef; }
  public void setBettorRef(String bettorRef) { this.bettorRef = bettorRef; }
  public BetStatus getStatus() { return status; }
  public void setStatus(BetStatus status) { this.status = status; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
}
