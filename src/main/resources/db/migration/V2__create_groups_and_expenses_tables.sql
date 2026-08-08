-- Groups and expenses schema

CREATE SEQUENCE groups_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE groups (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255)
);

CREATE TABLE group_members (
    group_id BIGINT NOT NULL REFERENCES groups(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    PRIMARY KEY (group_id, user_id)
);

CREATE SEQUENCE expenses_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE expenses (
    id BIGINT PRIMARY KEY,
    description VARCHAR(255),
    amount NUMERIC(19,2),
    user_id BIGINT REFERENCES users(id),
    group_id BIGINT REFERENCES groups(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255)
);

