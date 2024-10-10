DROP TABLE IF EXISTS managers;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS todos;
DROP TABLE IF EXISTS log;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       created_at DATETIME(6),
                       modified_at DATETIME(6),
                       email VARCHAR(255),
                       image_url VARCHAR(255),
                       nickname VARCHAR(255),
                       nickname_hash BIGINT,
                       password VARCHAR(255),
                       user_role ENUM ('ROLE_ADMIN', 'ROLE_USER'),
                       PRIMARY KEY (id, nickname_hash),
                       UNIQUE (email, nickname_hash)
) PARTITION BY HASH(nickname_hash)
    PARTITIONS 4;

CREATE TABLE todos (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       created_at DATETIME(6),
                       modified_at DATETIME(6),
                       user_id BIGINT NOT NULL,
                       contents VARCHAR(255),
                       title VARCHAR(255),
                       weather VARCHAR(255),
                       PRIMARY KEY (id)
);

CREATE TABLE comments (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          created_at DATETIME(6),
                          modified_at DATETIME(6),
                          todo_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          contents VARCHAR(255),
                          PRIMARY KEY (id)
);

CREATE TABLE log (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     created_at DATETIME(6),
                     modified_at DATETIME(6),
                     manager_user_id BIGINT,
                     todo_id BIGINT,
                     user_id BIGINT,
                     message VARCHAR(255),
                     PRIMARY KEY (id)
);

CREATE TABLE managers (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          todo_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          PRIMARY KEY (id)
);