CREATE TABLE IF NOT EXISTS bets (
  id BIGINT NOT NULL AUTO_INCREMENT,
  bettor_ref VARCHAR(100) NOT NULL,
  event_id BIGINT NOT NULL,
  selection VARCHAR(50) NOT NULL,
  odds DECIMAL(10,4) NOT NULL,
  stake DECIMAL(12,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'NEW',
  created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  INDEX idx_bets_bettor_ref (bettor_ref),
  INDEX idx_bets_event_id (event_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
