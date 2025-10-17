// src/main/java/com/example/sportsbook/bets/Bet.java
package com.example.sportsbook.bets;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bets")
public class Bet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "event_id", nullable = false)
  private Long eventId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "selection", nullable = false, length = 120)
  private String selection;

  @Column(name = "odds_decimal", precision = 10, scale = 4, nullable = false)
  private BigDecimal oddsDecimal;

  @Column(name = "stake", precision = 10, scale = 2, nullable = false)
  private BigDecimal stake;

  @Column(name = "bettor_ref") // nullable by default; DB is NULL DEFAULT NULL
  private String bettorRef;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 32)
  private BetStatus status = BetStatus.PENDING;

  // Let the DB manage timestamps via DEFAULT / ON UPDATE.
  // Mark them not insertable/updatable so Hibernate doesn't try to set them.
  @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", insertable = false, updatable = false)
  private LocalDateTime updatedAt;

  // getters/setters/constructors …
  public Bet() {}
  // add the rest of your accessors as you prefer (Lombok is fine too)
}
