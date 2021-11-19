
INSERT INTO users (code, email, is_moderator, name, password, photo, reg_time)
VALUES ('code121220', 'mailone@example.ru', 0, 'Egor Petrov', 'hashword121220', 'https://clck.ru/YvTa9', current_time()),
       ('code121221', 'mailtwo@example.ru', 0, 'Ivan Ivanov', 'hashword121221', 'https://clck.ru/YvTcZ', current_time()),
       ('code121222', 'mailthree@example.ru', 1, 'Алексей Прохоров', 'hashword121222', 'https://clck.ru/YvTgK', current_time()),
       ('code121223', 'mailfour@example.ru', 0, 'Максим Титов', 'hashword121223', 'https://clck.ru/YvTa9', current_time()),
       ('code121224', 'mailfive@example.ru', 1, 'Владимир Тутов', 'hashword121224', 'https://clck.ru/YvTcZ', current_time()),
       ('code121225', 'mailsix@example.ru', 0, 'Евгений Толкунов', 'hashword121225', 'https://clck.ru/YvTgK', current_time());

INSERT INTO posts (date_time, is_active, moderation_status, text, title, view_count, moderator_id, user_id)
VALUES (current_time(), 0, 'NEW', 'This is an article about modern programming languages', 'A couple words about programming languages', 0, NULL, 1),
       (current_time(), 0, 'NEW', 'Today JAVA is still among the top programming language', 'Post about JAVA', 0, NULL, 2),
       (current_time(), 1, 'ACCEPTED', 'The post on the topic of the first programming language', 'The first...', 3, 3, 6);

INSERT INTO post_comments (text, time, parent_id, post_id, user_id)
VALUES ('Отличная статья!', current_time(), null, 1, 2),
       ('Спасибо! Полезная и актуальная информация!', current_time(), null, 3, 4),
       ('I\'ve agreed with the last speaker', current_time(), 1, 1, 3);

INSERT INTO tags (name)
VALUES ('Java'),
       ('Kotlin'),
       ('Python'),
       ('Dart');

INSERT INTO tag2post (tag_id, post_id)
VALUES (1, 2),
       (1, 3),
       (2, 3),
       (3, 3);



