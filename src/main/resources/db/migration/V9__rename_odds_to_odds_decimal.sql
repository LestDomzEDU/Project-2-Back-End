-- rename the column so it matches what the app sends
ALTER TABLE bets CHANGE COLUMN `odds` `odds_decimal` DECIMAL(10,4) NOT NULL;

-- make sure these are set too (you already did some of this, but safe to include)
ALTER TABLE bets 
  MODIFY `bettor_ref` VARCHAR(255) NULL DEFAULT NULL,
  MODIFY `status`     VARCHAR(32)  NOT NULL DEFAULT 'PENDING',
  MODIFY `created_at` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  MODIFY `updated_at` DATETIME(6)  NULL     DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6);
