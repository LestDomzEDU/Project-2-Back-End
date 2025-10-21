-- V7__bettor_ref_nullable.sql
ALTER TABLE bets
  MODIFY bettor_ref VARCHAR(255) NULL DEFAULT NULL;
