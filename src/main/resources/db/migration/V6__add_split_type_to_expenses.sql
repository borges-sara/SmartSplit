-- Add split type (equal / percentage / amount) to expenses

ALTER TABLE expenses
    ADD COLUMN split_type VARCHAR(255);