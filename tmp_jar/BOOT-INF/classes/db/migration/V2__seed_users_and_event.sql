INSERT INTO users (email, display_name) VALUES
('demo@example.com','Demo Bettor')
ON DUPLICATE KEY UPDATE display_name=VALUES(display_name);

-- one sample event
INSERT INTO events (league, home_team, away_team, start_time, status)
VALUES ('NBA', 'Lakers', 'Warriors', DATE_ADD(NOW(), INTERVAL 1 DAY), 'SCHEDULED');

-- moneyline market + odds for that event
INSERT INTO markets (event_id, type)
SELECT e.id, 'MONEYLINE' FROM events e
WHERE NOT EXISTS (SELECT 1 FROM markets m WHERE m.event_id=e.id AND m.type='MONEYLINE');

INSERT INTO odds (market_id, selection, american, decimal)
SELECT m.id, 'HOME', -150, 1.67 FROM markets m
LEFT JOIN odds o ON o.market_id=m.id AND o.selection='HOME'
WHERE o.id IS NULL;

INSERT INTO odds (market_id, selection, american, decimal)
SELECT m.id, 'AWAY', 130, 2.30 FROM markets m
LEFT JOIN odds o ON o.market_id=m.id AND o.selection='AWAY'
WHERE o.id IS NULL;
