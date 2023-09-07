-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE subscribes
(
    subscriber_id        INT REFERENCES users (user_id) ON DELETE CASCADE,
    subscribe_to_user_id INT REFERENCES users (user_id) ON DELETE CASCADE
);

