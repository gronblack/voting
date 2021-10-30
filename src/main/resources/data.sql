INSERT INTO users (email, name, password, registered)
VALUES ('user@gmail.com', 'User', '{noop}password', DATEADD('DAY', -10, CURRENT_DATE)),
       ('admin@voting.ru', 'Admin', '{noop}admin', DATEADD('DAY', -15, CURRENT_DATE)),
       ('user2@gmail.com', 'User2', '{noop}password2', DATEADD('DAY', -10, CURRENT_DATE));

INSERT INTO user_role (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3);

INSERT INTO restaurant (name, phone)
VALUES ('Noma', '+45-3296-3297'),
       ('Mirazur', '+33-492-41-8686'),
       ('Asador', '+7-495-953-1564');

INSERT INTO dish (name, price, restaurant_id)
VALUES ('Beef Wellington', 150.25, 1),
       ('Onion soup', 80.50, 1),
       ('Peking duck', 110, 1),
       ('Chicken salad', 95.5, 2),
       ('Caesar salad', 100, 2),
       ('Potato frittata', 98.5, 2),
       ('Fish pie', 65.8, 3),
       ('Hummus', 80, 3),
       ('Chocolate ice cream', 50, 3);

INSERT INTO menu_item (actual_date, dish_id)
VALUES (DATEADD('DAY', -1, CURRENT_DATE), 1),
       (DATEADD('DAY', -1, CURRENT_DATE), 2),
       (CURRENT_DATE, 1),
       (CURRENT_DATE, 3),
       (DATEADD('DAY', -1, CURRENT_DATE), 4),
       (DATEADD('DAY', -1, CURRENT_DATE), 5),
       (CURRENT_DATE, 5),
       (CURRENT_DATE, 6),
       (DATEADD('DAY', -1, CURRENT_DATE), 7),
       (DATEADD('DAY', -1, CURRENT_DATE), 8),
       (CURRENT_DATE, 8),
       (CURRENT_DATE, 9);

INSERT INTO vote (actual_date, user_id, restaurant_id)
VALUES ('2020-05-20', 1, 1),
       ('2020-05-20', 2, 1),
       ('2020-05-20', 3, 3),
       ('2020-05-21', 1, 1),
       ('2020-05-21', 2, 1),
       (DATEADD('DAY', -2, CURRENT_DATE), 1, 1),
       (DATEADD('DAY', -2, CURRENT_DATE), 2, 2),
       (DATEADD('DAY', -1, CURRENT_DATE), 1, 3),
       (DATEADD('DAY', -1, CURRENT_DATE), 2, 1),
       (CURRENT_DATE, 1, 2),
       (CURRENT_DATE, 2, 2);
