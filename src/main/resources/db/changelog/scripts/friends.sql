-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE friends
(
    target_user_id INT REFERENCES users (user_id) ON DELETE CASCADE,
    friend_id      INT REFERENCES users (user_id) ON DELETE CASCADE
        CHECK ( friend_id != friends.target_user_id )
);
