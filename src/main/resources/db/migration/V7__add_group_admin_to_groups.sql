ALTER TABLE groups
    ADD COLUMN admin_email VARCHAR REFERENCES users(email);
