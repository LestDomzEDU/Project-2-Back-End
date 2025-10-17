package com.example.sportsbook.bets.dto;

public record BetSettleRequest(Result result) {
  public enum Result { WON, LOST, VOID }
}
