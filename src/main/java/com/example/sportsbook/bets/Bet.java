package com.example.sportsbook.bets;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "bets")
public class Bet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", nullable = false, columnDefinition = "datetime(6)")
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", columnDefinition = "datetime(6)")
  private OffsetDateTime updatedAt;

  @Column(name = "event_id", nullable = false)
  private Long eventId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "selection", length = 120, nullable = false)
  private String selection;

  // IMPORTANT: table column is "odds", Java field is oddsDecimal
  @Column(name = "odds", precision = 10, scale = 4, nullable = false)
  private BigDecimal oddsDecimal;

  @Column(name = "stake", precision = 10, scale = 2, nullable = false)
  private BigDecimal stake;

  @Column(name = "bettor_ref")
  private String bettorRef; // nullable

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 32, nullable = false)
  private BetStatus status = BetStatus.PENDING;

  /* ---- Lifecycle hooks to set timestamps ---- */
  @PrePersist
  protected void onCreate() {
    this.createdAt = OffsetDateTime.now();
    this.updatedAt = this.createdAt;
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = OffsetDateTime.now();
  }

  /* ---- Getters / Setters (no Lombok) ---- */
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

  public OffsetDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

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
}
