-- Expense category/currency, expense splits, and payment records

ALTER TABLE expenses
    ADD COLUMN currency VARCHAR(255),
    ADD COLUMN category VARCHAR(255);

CREATE SEQUENCE expense_splits_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE expense_splits (
    id BIGINT PRIMARY KEY,
    expense_id BIGINT REFERENCES expenses(id),
    user_id BIGINT REFERENCES users(id),
    amount_to_pay NUMERIC(19,2),
    settled BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255)
);

CREATE SEQUENCE payment_records_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE payment_records (
    id BIGINT PRIMARY KEY,
    amount NUMERIC(19,2) NOT NULL,
    group_id BIGINT REFERENCES groups(id),
    payee_id BIGINT REFERENCES users(id),
    payer_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255)
);