-- src/main/resources/db/migration/V3__indexes.sql
CREATE INDEX idx_events_start_time ON events(start_time);
CREATE INDEX idx_events_league ON events(league);