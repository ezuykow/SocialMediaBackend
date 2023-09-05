-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE posts
(
    id             BIGSERIAL PRIMARY KEY,
    author_id      INT REFERENCES users (user_id) ON DELETE CASCADE,
    image_name     VARCHAR(50),
    title          VARCHAR(100)  NOT NULL,
    text           VARCHAR(4096) NOT NULL,
    created_at_UTC DATE          NOT NULL
);
