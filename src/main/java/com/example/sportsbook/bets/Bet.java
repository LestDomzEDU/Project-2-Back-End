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

  @Column(name = "event_id", nullable = false)
  @NotNull
  private Long eventId;

  @Column(name = "user_id")
  private Long userId;

  @Column(nullable = false, length = 120)
  @NotBlank
  private String selection;

  @Column(name = "odds_decimal", nullable = false, precision = 8, scale = 4)
  @NotNull @DecimalMin("1.01")
@Column(name = "odds")
private BigDecimal oddsDecimal;

  @Column(nullable = false, precision = 10, scale = 2)
  @NotNull @DecimalMin("0.01")
  private BigDecimal stake;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BetStatus status = BetStatus.PENDING;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @PrePersist
  void onCreate() {
    Instant now = Instant.now();
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  void onUpdate() { updatedAt = Instant.now(); }

  // ==== getters/setters ====
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

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

  public BetStatus getStatus() { return status; }
  public void setStatus(BetStatus status) { this.status = status; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
