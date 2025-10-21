-- V6__seed_one_event.sql
-- If id is auto-increment, omit `id` below and let the DB assign it.
INSERT INTO events (id, home_team, away_team, league, start_time)
VALUES (1, 'Lakers', 'Warriors', 'NBA', '2030-01-01 00:00:00')
ON DUPLICATE KEY UPDATE home_team = VALUES(home_team);
