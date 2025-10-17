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

  @Column(name="event_id", nullable=false)
  @NotNull private Long eventId;

  @Column(name="user_id")
  private Long userId;

  @Column(nullable=false, length=120)
  @NotBlank private String selection;

  @Column(name="odds_decimal", nullable=false, precision=8, scale=4)
  @NotNull @DecimalMin("1.01") private BigDecimal oddsDecimal;

  @Column(nullable=false, precision=10, scale=2)
  @NotNull @DecimalMin("0.01") private BigDecimal stake;

  @Enumerated(EnumType.STRING)
  @Column(nullable=false)
  private BetStatus status = BetStatus.PENDING;

  @Column(name="created_at", nullable=false, updatable=false)
  private Instant createdAt;

  @Column(name="updated_at", nullable=false)
  private Instant updatedAt;

  @PrePersist
  void onCreate() {
    Instant now = Instant.now();
    createdAt = now;
    updatedAt = now;
  }
  @PreUpdate
  void onUpdate() { updatedAt = Instant.now(); }

  // getters/setters...
}
