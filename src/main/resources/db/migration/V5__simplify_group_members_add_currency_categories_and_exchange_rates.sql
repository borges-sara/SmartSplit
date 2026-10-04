-- Drop the per-membership role and go back to a plain group_members join table
-- (GroupMember entity was removed in favour of a direct Group <-> User @ManyToMany),
-- add per-group base currency and custom categories, and cache external exchange rates.

DROP TABLE group_members;
DROP SEQUENCE group_members_seq;

CREATE TABLE group_members (
    group_id BIGINT NOT NULL REFERENCES groups(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    PRIMARY KEY (group_id, user_id)
);

ALTER TABLE groups
    ADD COLUMN base_currency VARCHAR(3);

CREATE TABLE group_categories (
    group_id BIGINT NOT NULL REFERENCES groups(id),
    category VARCHAR(255)
);

ALTER TABLE expenses
    ALTER COLUMN currency TYPE VARCHAR(3);

CREATE SEQUENCE exchange_rates_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE exchange_rates (
    id BIGINT PRIMARY KEY,
    base_currency VARCHAR(3),
    target_currency VARCHAR(3),
    rate NUMERIC(19,6),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255)
);