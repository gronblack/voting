INSERT INTO users (email, name, password)
VALUES ('user@gmail.com', 'User', '{noop}password'),
       ('admin@voting.ru', 'Admin', '{noop}admin');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);