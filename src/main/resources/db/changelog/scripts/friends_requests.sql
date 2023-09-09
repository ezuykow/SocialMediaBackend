-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE friends_requests
(
    request_from_user_id INT REFERENCES users (user_id) ON DELETE CASCADE,
    request_to_user_id   INT REFERENCES users (user_id) ON DELETE CASCADE
        CHECK ( request_to_user_id != friends_requests.request_from_user_id )
);

