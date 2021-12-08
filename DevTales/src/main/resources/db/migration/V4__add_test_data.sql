
INSERT INTO users (code, email, is_moderator, name, password, photo, reg_time)
VALUES ('code121220', 'mailone@example.ru', 0, 'Egor Petrov', 'hashword121220', 'https://clck.ru/YvTa9', current_time()),
       ('code121221', 'mailtwo@example.ru', 0, 'Сергей Хмуров', 'hashword121221', 'https://clck.ru/YvTcZ', current_time()),
       ('code121222', 'mailthree@example.ru', 1, 'Алексей Прохоров', 'hashword121222', 'https://clck.ru/YvTgK', current_time()),
       ('code121223', 'mailfour@example.ru', 0, 'Максим Титов', 'hashword121223', 'https://clck.ru/YvTa9', current_time()),
       ('code121224', 'mailfive@example.ru', 1, 'Владимир Тутов', 'hashword121224', 'https://clck.ru/YvTcZ', current_time()),
       ('code121228', 'JamesGosling@example.ru', 1, 'James Gosling', 'hashword121228', 'https://clck.ru/YvTcZ', current_time()),
       ('code121225', 'mailsix@example.ru', 0, 'Евгений Толкунов', 'hashword121225', 'https://clck.ru/YvTgK', current_time());

INSERT INTO posts (date_time, is_active, moderation_status, text, title, view_count, moderator_id, user_id)
VALUES (current_time() - interval 10 minute, 1, 'ACCEPTED', 'What are the pros and cons of a particular programming language? Is X a good language for my task? Googling “best programming language” will give you a standard list of “Python, Java, JavaScript, C#, C++, PHP” with a vague list of pros and cons.', 'These Modern Programming Languages Will Make You Suffer', 0, 3, 1),
       (current_time() - interval 20 minute, 0, 'ACCEPTED', 'Java may still be getting bad press when it comes to (mostly consumer) security, but it continues to be the world`s most popular software development language.', 'Java: Still the Most Popular Programming Language', 0, 5, 2),
       (current_time() - interval 30 minute, 1, 'ACCEPTED', '"Генерация случайных чисел слишком важна, чтобы оставлять ее на волю случая" - Роберт Р. Кавью. Как-то поздним летним вечером мне пришлось разобраться, как устроены генераторы случайных чисел в Windows и Linux.', 'Генераторы случайных чисел в разных ОС', 12, 3, 2),
       (current_time() - interval 40 minute, 1, 'ACCEPTED', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Feugiat pretium nibh ipsum consequat nisl vel pretium lectus.', 'Generated Lorem Ipsum placeholder text', 0, 3, 2),
       (current_time() - interval 50 minute, 1, 'ACCEPTED', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Feugiat pretium nibh ipsum consequat nisl vel pretium lectus.', 'Generated Lorem Ipsum placeholder text', 3, 5, 6),
       (current_time() - interval 60 minute, 1, 'ACCEPTED', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Feugiat pretium nibh ipsum consequat nisl vel pretium lectus.', 'Generated Lorem Ipsum placeholder text', 5, 3, 2),
       (current_time() - interval 70 minute, 0, 'NEW', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 'Fifth post. Generated Lorem Ipsum placeholder text', 0, NULL, 1),
       (current_time() - interval 80 minute, 1, 'ACCEPTED', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 'Sixth post. Generated Lorem Ipsum placeholder text', 1, 3, 6),
       (current_time() - interval 55 minute, 0, 'ACCEPTED', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, ut labore et dolore magna aliqua sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Feugiat pretium nibh ipsum consequat nisl vel pretium lectus.', 'Generated Lorem Ipsum placeholder text', 3, 5, 6),
       (current_time() - interval 65 minute, 1, 'ACCEPTED', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Feugiat pretium nibh ipsum consequat nisl vel pretium lectus.', 'Generated Lorem Ipsum placeholder text', 5, 3, 2),
       (current_time() - interval 75 minute, 0, 'NEW', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 'Fifth post. Generated Lorem Ipsum placeholder text', 0, NULL, 1),
       (current_time() - interval 85 minute, 1, 'ACCEPTED', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 'Sixth post. Generated Lorem Ipsum placeholder text', 1, 3, 6),
       (current_time() - interval 90 minute, 0, 'NEW', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 'Seventh post. Generated Lorem Ipsum placeholder text', 0, NULL, 4),
       (current_time() - interval 100 minute, 1, 'ACCEPTED', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 'Eighth post', 8, 3, 2),
       (current_time() - interval 110 minute, 1, 'ACCEPTED', 'neque gravida in fermentum et sollicitudin ac orci phasellus egestas tellus rutrum tellus pellentesque eu tincidunt tortor aliquam nulla facilisi cras fermentum odio eu feugiat pretium nibh ipsum consequat nisl', 'Generated Lorem Ipsum placeholder text', 3, 3, 6);

INSERT INTO post_comments (text, time, parent_id, post_id, user_id)
VALUES ('Отличная статья!', current_time(), null, 3, 2),
       ('Спасибо! Полезная и актуальная информация!', current_time(), null, 5, 4),
       ('neque gravida in fermentum et sollicitudin ac orci phasellus', current_time(), null, 8, 4),
       ('neque gravida in fermentum et sollicitudin ac orci phasellus', current_time(), null, 6, 5),
       ('I\'ve agreed with the last speaker', current_time(), 1, 3, 3);

INSERT INTO tags (name)
VALUES ('Java'),
       ('Kotlin'),
       ('Python'),
       ('SQL'),
       ('Hibernate'),
       ('Spring'),
       ('PHP'),
       ('Vaadin'),
       ('Flutter'),
       ('Dart');

INSERT INTO post_votes (time, value, post_id, user_id)
VALUES (current_time(), 1, 1, 3),
       (current_time(), 1, 1, 2),
       (current_time(), -1, 10, 4),
       (current_time(), 1, 1, 5),
       (current_time(), 1, 10, 1),
       (current_time(), -1, 1, 6),
       (current_time(), -1, 10, 6),
       (current_time(), 1, 3, 1),
       (current_time(), 1, 3, 3),
       (current_time(), -1, 3, 2),
       (current_time(), 1, 5, 3),
       (current_time(), 1, 5, 2),
       (current_time(), 1, 1, 2),
       (current_time(), -1, 8, 3),
       (current_time(), 1, 5, 4),
       (current_time(), 1, 6, 3),
       (current_time(), 1, 6, 2);

INSERT INTO tag2post (tag_id, post_id)
VALUES (1, 2),
       (1, 3),
       (2, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (2, 7),
       (2, 6),
       (2, 5),
       (2, 8),
       (1, 8),
       (4, 8),
       (6, 8),
       (3, 5),
       (2, 2),
       (3, 3),
       (10, 11),
       (9, 10),
       (8, 9),
       (7, 6),
       (5, 2),
       (3, 7),
       (3, 3);



