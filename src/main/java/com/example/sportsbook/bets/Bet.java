package com.example.sportsbook.bets;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bets", indexes = {
  @Index(name = "idx_bets_bettor_ref", columnList = "bettor_ref"),
  @Index(name = "idx_bets_event_id", columnList = "event_id")
})
public class Bet {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "bettor_ref", nullable = false, length = 100)
  @NotBlank
  private String bettorRef;

  @Column(name = "event_id", nullable = false)
  @NotNull
  private Long eventId;

  @Column(name = "selection", nullable = false, length = 50)
  @NotBlank
  private String selection; // e.g., "HOME", "AWAY", "DRAW", "OVER_2_5"

  @Column(name = "odds", nullable = false, precision = 10, scale = 4)
  @NotNull @DecimalMin("1.0001")
  private BigDecimal odds;

  @Column(name = "stake", nullable = false, precision = 12, scale = 2)
  @NotNull @DecimalMin("0.01")
  private BigDecimal stake;

  @Column(name = "status", nullable = false, length = 20)
  @NotBlank
  private String status; // NEW, WON, LOST, VOID

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @PrePersist
  void onCreate() {
    this.createdAt = Instant.now();
    if (this.status == null || this.status.isBlank()) this.status = "NEW";
  }

  // getters/setters…
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getBettorRef() { return bettorRef; }
  public void setBettorRef(String bettorRef) { this.bettorRef = bettorRef; }
  public Long getEventId() { return eventId; }
  public void setEventId(Long eventId) { this.eventId = eventId; }
  public String getSelection() { return selection; }
  public void setSelection(String selection) { this.selection = selection; }
  public BigDecimal getOdds() { return odds; }
  public void setOdds(BigDecimal odds) { this.odds = odds; }
  public BigDecimal getStake() { return stake; }
  public void setStake(BigDecimal stake) { this.stake = stake; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
