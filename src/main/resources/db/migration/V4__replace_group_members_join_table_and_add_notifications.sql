-- Replace the plain group_members join table with the richer GroupMember entity
-- table (adds a role per membership), and add notifications.

DROP TABLE group_members;

CREATE SEQUENCE group_members_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE group_members (
    id BIGINT PRIMARY KEY,
    group_id BIGINT REFERENCES groups(id),
    user_id BIGINT REFERENCES users(id),
    role VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255)
);

CREATE SEQUENCE notifications_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE notifications (
    id BIGINT PRIMARY KEY,
    recipient_id BIGINT REFERENCES users(id),
    message VARCHAR(255),
    notification_type VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255)
);