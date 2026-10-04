-- Currency validation isn't wired up yet, so allow a regular string for now
-- instead of enforcing the ISO 4217 3-character length at the DB level.
ALTER TABLE groups
    ALTER COLUMN base_currency TYPE VARCHAR(255);
