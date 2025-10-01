-- Allows us to store the winner for an event
ALTER TABLE events ADD COLUMN IF NOT EXISTS result TEXT;  -- 'HOME' or 'AWAY'
