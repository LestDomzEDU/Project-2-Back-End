-- Seed minimal data
INSERT INTO users (email, display_name) VALUES ('demo@example.com', 'Demo User');

-- Two events
INSERT INTO events (league, home_team, away_team, start_time, status) VALUES
('NFL','49ers','Rams', DATE_ADD(UTC_TIMESTAMP(), INTERVAL 1 DAY), 'SCHEDULED'),
('NBA','Warriors','Lakers', DATE_ADD(UTC_TIMESTAMP(), INTERVAL 2 DAY), 'SCHEDULED');

-- Moneyline markets
INSERT INTO markets (event_id, type)
SELECT id, 'MONEYLINE' FROM events;

-- Odds for HOME
INSERT INTO odds (market_id, selection, american, odds_decimal)
SELECT m.id, 'HOME', -150, 1.67
FROM markets m LEFT JOIN odds o ON o.market_id = m.id AND o.selection='HOME'
WHERE o.id IS NULL;

-- Odds for AWAY
INSERT INTO odds (market_id, selection, american, odds_decimal)
SELECT m.id, 'AWAY',  +130, 2.30
FROM markets m LEFT JOIN odds o ON o.market_id = m.id AND o.selection='AWAY'
WHERE o.id IS NULL;
