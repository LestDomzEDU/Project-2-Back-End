-- Create table if it's missing (safe to run repeatedly)
CREATE TABLE IF NOT EXISTS `events` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `league` VARCHAR(32) NOT NULL,
  `home_team` VARCHAR(64) NOT NULL,
  `away_team` VARCHAR(64) NOT NULL,
  `start_time` DATETIME NOT NULL,
  `status` VARCHAR(32) NOT NULL,
  `result` VARCHAR(64) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- If an older schema used different column names, uncomment the renames you need:
-- ALTER TABLE `events` CHANGE COLUMN `startTime` `start_time` DATETIME NOT NULL;
-- ALTER TABLE `events` CHANGE COLUMN `state`     `status`     VARCHAR(32) NOT NULL;
-- ALTER TABLE `events` ADD COLUMN `result` VARCHAR(64) NULL;
