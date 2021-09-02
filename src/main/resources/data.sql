INSERT INTO users (email, name, password, registered)
VALUES ('user@gmail.com', 'User', '{noop}password', DATEADD('DAY', -10, CURDATE())),
       ('admin@voting.ru', 'Admin', '{noop}admin', DATEADD('DAY', -15, CURDATE()));

INSERT INTO user_role (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);