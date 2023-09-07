-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE subscribes
(
    subscribe_id   BIGSERIAL PRIMARY KEY,
    subscriber_id  INT REFERENCES users (user_id) ON DELETE CASCADE,
    target_user_id INT REFERENCES users (user_id) ON DELETE CASCADE CHECK ( target_user_id != subscribes.subscriber_id )
);
