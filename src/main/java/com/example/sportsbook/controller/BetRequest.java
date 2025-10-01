package com.example.sportsbook.controller;
public record BetRequest(long userId, long eventId, String selection, int stakeCents) {}
