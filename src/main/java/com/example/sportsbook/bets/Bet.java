// src/main/java/com/example/sportsbook/bets/Bet.java
package com.example.sportsbook.bets;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bets")
public class Bet {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "event_id", nullable = false)
  private Long eventId;

  @NotNull
  @Column(name = "user_id", nullable = false)
  private Long userId;

  @NotBlank
  @Column(name = "selection", nullable = false, length = 120)
  private String selection;

  @NotNull
  @Column(name = "odds_decimal", nullable = false, precision = 10, scale = 4)
  private BigDecimal oddsDecimal;

  @NotNull
  @Column(name = "stake", nullable = false, precision = 10, scale = 2)
  private BigDecimal stake;

  @Column(name = "bettor_ref", length = 255)
  private String bettorRef;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 32)
  private BetStatus status = BetStatus.PENDING;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at")
  private Instant updatedAt;

  @PreUpdate
  void touch() { this.updatedAt = Instant.now(); }

  // --- getters/setters expected by BetService ---

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
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
