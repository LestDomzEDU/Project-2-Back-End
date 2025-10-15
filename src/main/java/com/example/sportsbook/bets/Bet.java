// src/main/java/com/example/sportsbook/bets/Bet.java
package com.example.sportsbook.bets;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "bets", indexes = {
@Index(name = "ix_bets_event", columnList = "eventId"),
@Index(name = "ix_bets_bettor", columnList = "bettorRef")
})
public class Bet {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@Column(nullable = false)
private String eventId; // your event identifier (String to keep flexible)


@Enumerated(EnumType.STRING)
@Column(nullable = false)
private Selection selection; // HOME/AWAY/DRAW


@Column(nullable = false, precision = 12, scale = 2)
@DecimalMin(value = "1.00", message = "Minimum stake is 1.00")
private BigDecimal stake; // money


@Column(nullable = false, precision = 8, scale = 2)
@DecimalMin(value = "1.01", message = "Odds must be ≥ 1.01")
private BigDecimal oddsDecimal; // decimal odds (e.g., 1.50)


@Column(nullable = false, precision = 14, scale = 2)
private BigDecimal potentialPayout; // stake * odds, precomputed for display


@Enumerated(EnumType.STRING)
@Column(nullable = false)
private BetStatus status = BetStatus.PENDING;


// Temporary bettor reference while OAuth is off (e.g., from header X-PLAYER-ID).
// When OAuth is on, set this to the authenticated user email or userId.
@Column(nullable = false)
private String bettorRef;


@Column(nullable = false)
private Instant placedAt = Instant.now();


public Long getId() { return id; }
public String getEventId() { return eventId; }
public void setEventId(String eventId) { this.eventId = eventId; }
public Selection getSelection() { return selection; }
public void setSelection(Selection selection) { this.selection = selection; }
public BigDecimal getStake() { return stake; }
public void setStake(BigDecimal stake) { this.stake = stake; }
public BigDecimal getOddsDecimal() { return oddsDecimal; }
public void setOddsDecimal(BigDecimal oddsDecimal) { this.oddsDecimal = oddsDecimal; }
public BigDecimal getPotentialPayout() { return potentialPayout; }
public void setPotentialPayout(BigDecimal potentialPayout) { this.potentialPayout = potentialPayout; }
public BetStatus getStatus() { return status; }
public void setStatus(BetStatus status) { this.status = status; }
public String getBettorRef() { return bettorRef; }
public void setBettorRef(String bettorRef) { this.bettorRef = bettorRef; }
public Instant getPlacedAt() { return placedAt; }
public void setPlacedAt(Instant placedAt) { this.placedAt = placedAt; }
}