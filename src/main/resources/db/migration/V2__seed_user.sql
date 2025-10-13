-- V2__seed.sql

INSERT INTO users (email, display_name) VALUES
  ('demo@sportsbook.local','Demo User');

INSERT INTO events (league, home_team, away_team, start_time, status) VALUES
  ('NFL','49ers','Rams',    NOW() + INTERVAL 3 DAY, 'SCHEDULED'),
  ('NBA','Warriors','Lakers', NOW() + INTERVAL 5 DAY, 'SCHEDULED');

-- Create a MONEYLINE market per event
INSERT INTO markets (event_id, type)
SELECT e.id, 'MONEYLINE' FROM events e;

-- HOME/AWAY odds for each MONEYLINE market
INSERT INTO odds (market_id, selection, american, decimal_odds)
SELECT m.id, 'HOME', -150, 1.67 FROM markets m WHERE m.type='MONEYLINE';

INSERT INTO odds (market_id, selection, american, decimal_odds)
SELECT m.id, 'AWAY',  130, 2.30 FROM markets m WHERE m.type='MONEYLINE';
