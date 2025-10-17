package com.example.sportsbook.bets;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="bets")
public class Bet {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @NotNull @Column(name="event_id", nullable=false) private Long eventId;
  @Column(name="user_id") private Long userId;
  @NotBlank @Column(length=120) private String selection;
  @NotNull @DecimalMin("1.01") @Column(name="odds_decimal", precision=8, scale=2, nullable=false) private BigDecimal oddsDecimal;
  @NotNull @DecimalMin("0.01") @Column(precision=10, scale=2, nullable=false) private BigDecimal stake;
  @Column(name="bettor_ref") private String bettorRef;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private BetStatus status = BetStatus.PENDING;
  @Column(name="created_at", nullable=false, updatable=false) private Instant createdAt;
  @Column(name="updated_at") private Instant updatedAt;
  @PrePersist void onCreate(){ this.createdAt = Instant.now(); }
  @PreUpdate void onUpdate(){ this.updatedAt = Instant.now(); }
  public Long getId(){return id;} public Long getEventId(){return eventId;} public void setEventId(Long v){eventId=v;}
  public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;}
  public String getSelection(){return selection;} public void setSelection(String v){selection=v;}
  public BigDecimal getOddsDecimal(){return oddsDecimal;} public void setOddsDecimal(BigDecimal v){oddsDecimal=v;}
  public BigDecimal getStake(){return stake;} public void setStake(BigDecimal v){stake=v;}
  public String getBettorRef(){return bettorRef;} public void setBettorRef(String v){bettorRef=v;}
  public BetStatus getStatus(){return status;} public void setStatus(BetStatus v){status=v;}
  public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
