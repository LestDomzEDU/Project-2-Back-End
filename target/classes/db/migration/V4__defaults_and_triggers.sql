-- updated_at maintenance (simplified for Flyway on MariaDB)
DROP TRIGGER IF EXISTS bets_set_updated_at;
CREATE TRIGGER bets_set_updated_at
BEFORE UPDATE ON bets
FOR EACH ROW
  SET NEW.updated_at = CURRENT_TIMESTAMP(6);
