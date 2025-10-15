// src/main/java/com/example/sportsbook/bets/dto/BetResponse.java
package com.example.sportsbook.bets.dto;


import com.example.sportsbook.bets.*;
import java.math.BigDecimal;
import java.time.Instant;


public class BetResponse {
public Long id;
public String eventId;
public Selection selection;
public BigDecimal stake;
public BigDecimal oddsDecimal;
public BigDecimal potentialPayout;
public BetStatus status;
public String bettorRef;
public Instant placedAt;


public static BetResponse from(Bet b) {
BetResponse r = new BetResponse();
r.id = b.getId();
r.eventId = b.getEventId();
r.selection = b.getSelection();
r.stake = b.getStake();
r.oddsDecimal = b.getOddsDecimal();
r.potentialPayout = b.getPotentialPayout();
r.status = b.getStatus();
r.bettorRef = b.getBettorRef();
r.placedAt = b.getPlacedAt();
return r;
}
}