-- updated_at maintenance
DROP TRIGGER IF EXISTS bets_set_updated_at;
DELIMITER $$
CREATE TRIGGER bets_set_updated_at
BEFORE UPDATE ON bets
FOR EACH ROW
BEGIN
  SET NEW.updated_at = CURRENT_TIMESTAMP(6);
END$$
DELIMITER ;
