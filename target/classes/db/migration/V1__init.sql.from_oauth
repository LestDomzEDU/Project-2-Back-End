CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email TEXT UNIQUE NOT NULL,
  display_name TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE TABLE events (
  id BIGSERIAL PRIMARY KEY,
  league TEXT NOT NULL,
  home_team TEXT NOT NULL,
  away_team TEXT NOT NULL,
  start_time TIMESTAMPTZ NOT NULL,
  status TEXT NOT NULL DEFAULT 'SCHEDULED'
);
CREATE TABLE markets (
  id BIGSERIAL PRIMARY KEY,
  event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
  type TEXT NOT NULL
);
CREATE TABLE odds (
  id BIGSERIAL PRIMARY KEY,
  market_id BIGINT NOT NULL REFERENCES markets(id) ON DELETE CASCADE,
  selection TEXT NOT NULL,
  american INT NOT NULL,
  decimal NUMERIC(6,2) NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE TABLE bets (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
  selection TEXT NOT NULL,
  stake_cents INT NOT NULL,
  potential_payout_cents INT NOT NULL,
  placed_at TIMESTAMPTZ DEFAULT NOW(),
  state TEXT NOT NULL DEFAULT 'PENDING'
);

INSERT INTO events (league,home_team,away_team,start_time) VALUES
('NFL','49ers','Rams', NOW() + INTERVAL '3 day'),
('NBA','Warriors','Lakers', NOW() + INTERVAL '5 day');

INSERT INTO markets (event_id,type) SELECT id,'MONEYLINE' FROM events;

INSERT INTO odds (market_id,selection,american,decimal)
SELECT id,'HOME',-150,1.67 FROM markets;
INSERT INTO odds (market_id,selection,american,decimal)
SELECT id,'AWAY',+130,2.30 FROM markets;