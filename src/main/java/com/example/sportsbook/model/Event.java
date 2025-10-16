package com.example.sportsbook.model;

import java.time.LocalDateTime;

public class Event {
    private Long id;
    private String league;
    private String homeTeam;
    private String awayTeam;
    private LocalDateTime startTime;
    private String status;
    private String result;

    public Event() {}

    public Event(Long id, String league, String homeTeam, String awayTeam,
                 LocalDateTime startTime, String status, String result) {
        this.id = id;
        this.league = league;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startTime = startTime;
        this.status = status;
        this.result = result;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLeague() { return league; }
    public void setLeague(String league) { this.league = league; }

    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }

    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    @Override public String toString() {
        return "Event{" +
                "id=" + id +
                ", league='" + league + '\'' +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", startTime=" + startTime +
                ", status='" + status + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
