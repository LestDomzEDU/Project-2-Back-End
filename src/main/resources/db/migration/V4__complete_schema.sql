
-- Ensure core tables exist and create remaining tables
CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  email TEXT UNIQUE NOT NULL,
  display_name TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS events (
  id BIGSERIAL PRIMARY KEY,
  league TEXT NOT NULL,
  home_team TEXT NOT NULL,
  away_team TEXT NOT NULL,
  start_time TIMESTAMPTZ NOT NULL,
  status TEXT NOT NULL DEFAULT 'SCHEDULED',
  result TEXT
);

CREATE TABLE IF NOT EXISTS markets (
  id BIGSERIAL PRIMARY KEY,
  event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
  type TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS odds (
  id BIGSERIAL PRIMARY KEY,
  market_id BIGINT NOT NULL REFERENCES markets(id) ON DELETE CASCADE,
  selection TEXT NOT NULL, -- 'HOME' or 'AWAY'
  american INT NOT NULL,
  decimal NUMERIC(6,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS bets (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
  selection TEXT NOT NULL, -- 'HOME' or 'AWAY'
  stake_cents INT NOT NULL CHECK (stake_cents > 0),
  price_american INT NOT NULL,
  placed_at TIMESTAMPTZ DEFAULT NOW(),
  state TEXT NOT NULL DEFAULT 'PENDING', -- PENDING/WON/LOST
  settled_at TIMESTAMPTZ,
  payout_cents INT
);

-- Seed demo user and two events with markets/odds if empty
INSERT INTO users (email, display_name)
SELECT 'demo@sportsbook.local','Demo User'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='demo@sportsbook.local');

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM events) THEN
    INSERT INTO events (league,home_team,away_team,start_time) VALUES
      ('NFL','49ers','Rams', NOW() + INTERVAL '3 day'),
      ('NBA','Warriors','Lakers', NOW() + INTERVAL '5 day');
  END IF;
END $$;

-- Create moneyline markets/odds for events that don't have them yet
INSERT INTO markets (event_id,type)
SELECT e.id, 'MONEYLINE' FROM events e
WHERE NOT EXISTS (SELECT 1 FROM markets m WHERE m.event_id = e.id AND m.type='MONEYLINE');

INSERT INTO odds (market_id, selection, american, decimal)
SELECT m.id, 'HOME', -150, 1.67 FROM markets m
WHERE NOT EXISTS (SELECT 1 FROM odds o WHERE o.market_id=m.id AND o.selection='HOME');

INSERT INTO odds (market_id, selection, american, decimal)
SELECT m.id, 'AWAY', 130, 2.30 FROM markets m
WHERE NOT EXISTS (SELECT 1 FROM odds o WHERE o.market_id=m.id AND o.selection='AWAY');
