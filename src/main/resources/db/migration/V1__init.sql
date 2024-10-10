CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       created_at DATETIME(6),
                       modified_at DATETIME(6),
                       email VARCHAR(255),
                       image_url VARCHAR(255),
                       nickname VARCHAR(255),
                       password VARCHAR(255),
                       user_role ENUM ('ROLE_ADMIN', 'ROLE_USER'),
                       PRIMARY KEY (id),
                       UNIQUE (email)
);

CREATE TABLE todos (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       created_at DATETIME(6),
                       modified_at DATETIME(6),
                       user_id BIGINT NOT NULL,
                       contents VARCHAR(255),
                       title VARCHAR(255),
                       weather VARCHAR(255),
                       PRIMARY KEY (id),
                       FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE comments (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          created_at DATETIME(6),
                          modified_at DATETIME(6),
                          todo_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          contents VARCHAR(255),
                          PRIMARY KEY (id),
                          FOREIGN KEY (todo_id) REFERENCES todos(id),
                          FOREIGN KEY (user_id) REFERENCES users(id)
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
                          PRIMARY KEY (id),
                          FOREIGN KEY (todo_id) REFERENCES todos(id),
                          FOREIGN KEY (user_id) REFERENCES users(id)
);
