<<<<<<< HEAD
-- src/main/resources/db/migration/V3__indexes.sql
CREATE INDEX idx_events_start_time ON events(start_time);
CREATE INDEX idx_events_league ON events(league);
=======
CREATE INDEX IF NOT EXISTS idx_events_status ON events(status);
CREATE INDEX IF NOT EXISTS idx_markets_event ON markets(event_id);
CREATE INDEX IF NOT EXISTS idx_odds_market ON odds(market_id);
CREATE INDEX IF NOT EXISTS idx_bets_event ON bets(event_id);
CREATE INDEX IF NOT EXISTS idx_bets_user ON bets(user_id);
>>>>>>> origin/main
