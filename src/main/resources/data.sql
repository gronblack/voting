INSERT INTO users (email, name, password, registered)
VALUES ('user@gmail.com', 'User', '{noop}password', DATEADD('DAY', -10, CURDATE())),
       ('admin@voting.ru', 'Admin', '{noop}admin', DATEADD('DAY', -15, CURDATE()));

INSERT INTO user_role (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO restaurant (name)
VALUES ('Noma'),
       ('Mirazur'),
       ('Asador');

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

INSERT INTO menu (name, date, restaurant_id)
VALUES ('Noma lunch 1', CURRENT_DATE, 1),
       ('Noma lunch 2', CURRENT_DATE, 1),
       ('Mirazur lunch 1', CURRENT_DATE, 2),
       ('Mirazur lunch 2', CURRENT_DATE, 2),
       ('Asador lunch 1', CURRENT_DATE, 3),
       ('Asador lunch 2', CURRENT_DATE, 3);

-- INSERT INTO menu_dishes (menu_id, dish_id)
-- VALUES (1, 1),  /* Noma lunch 1, Beef Wellington */
--        (1, 2),  /* Noma lunch 1, Onion soup */
--        (2, 1),  /* Noma lunch 2, Beef Wellington */
--        (2, 3),  /* Noma lunch 2, Peking duck */
--        (3, 4),  /* Mirazur lunch 1, Chicken salad */
--        (3, 5),  /* Mirazur lunch 1, Caesar salad */
--        (4, 5),  /* Mirazur lunch 2, Caesar salad */
--        (4, 6),  /* Mirazur lunch 2, Potato frittata */
--        (5, 7),  /* Asador lunch 1, Fish pie */
--        (5, 8),  /* Asador lunch 1, Hummus */
--        (6, 8),  /* Asador lunch 2, Hummus */
--        (6, 9);  /* Asador lunch 2, Chocolate ice cream */

INSERT INTO vote (date, user_id, restaurant_id)
VALUES (CURDATE(), 1, 2),
       (DATEADD('DAY', -1, CURDATE()), 1, 3),
       (DATEADD('DAY', -2, CURDATE()), 1, 1),
       (CURDATE(), 2, 2),
       (DATEADD('DAY', -1, CURDATE()), 2, 1),
       (DATEADD('DAY', -2, CURDATE()), 2, 2);