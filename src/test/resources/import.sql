INSERT INTO `user`(id, email, username, password_hash) VALUES (1, 'kacperf1234@gmail.com', 'kacperfaber', 'HelloWorld123');
INSERT INTO `user`(id, email, username, password_hash) VALUES (2, 'marekfaber@gmail.com', 'MakerXD', 'HelloTest555_');
INSERT INTO `user`(id, email, username, password_hash) VALUES (3, 'darekfatek@gmail.com', 'Darek555', 'HelloTest');

INSERT INTO text_set(id, owner_id, title, description) VALUES (0, 1, 'HelloWorld!', 'This is the greatest set in the page.');

INSERT INTO text(id, text_set_id, text, _order_) VALUES (0, 0, 'Hello World!', 0);
INSERT INTO text(id, text_set_id, text, _order_) VALUES (1, 0, 'Second Text', 0);
INSERT INTO text(id, text_set_id, text, _order_) VALUES (121, 0, 'Text to delete', 0);

INSERT INTO reader(id, user_id, text_set_id) VALUES(0, 2, 0);
INSERT INTO reader(id, user_id, text_set_id) VALUES(1, 3, 0);

INSERT INTO text_set(id, owner_id, title, description) VALUES (1, 1, 'The better one!', 'This is the TextSet with ordered Text...');

INSERT INTO text(id, text_set_id, text, _order_) VALUES (100, 1, 'Sample Value1', 0);
INSERT INTO text(id, text_set_id, text, _order_) VALUES (101, 1, 'Sample Value2', 1);

INSERT INTO `user`(id, email, username, password_hash) VALUES(10, 'test-owner@gmail.com', 'test-user', 'HelloWorld123');
INSERT INTO `user`(id, email, username, password_hash) VALUES(11, 'test-reader@gmail.com', 'test-reader', 'HelloWorld123');

INSERT INTO text_set(id, owner_id, title, description) VALUES (10, 10, 'test-user property!', 'Hello World!');

INSERT INTO reader(id, user_id, text_set_id) VALUES(51, 10, 10);
INSERT INTO reader(id, user_id, text_set_id) VALUES(50, 11, 10);

INSERT INTO text(id, text_set_id, text, _order_, shown) VALUES (50, 10, 'Sample Value1', 0, CURRENT_DATE);
INSERT INTO text(id, text_set_id, text, _order_, shown) VALUES (51, 10, 'Sample Value2', 1, NULL);


INSERT INTO `user`(id, email, username, password_hash) VALUES(-50, 'link-creator@gmail.com', 'join-link-owner', 'HelloWorld123');
INSERT INTO `user`(id, email, username, password_hash) VALUES(-51, 'link-user@gmail.com', 'linker', 'HelloWorld123');

INSERT INTO text_set(id, owner_id, title, description) VALUES (-50, -50, 'test-user property!', 'Hello World!');

INSERT INTO reader(id, user_id, text_set_id) VALUES(-50, 11, 10);

INSERT INTO join_link(id, text_set_id, code, active_until) VALUES(-50, -50, 'EFG', '2024-09-05')
INSERT INTO join_link(id, text_set_id, code, active_until) VALUES(-51, -50, 'ABC', '2025-09-05')
INSERT INTO join_link(id, text_set_id, code, active_until) VALUES(-52, -50, 'Z', '2021-09-05')

INSERT INTO text(id, text_set_id, text, _order_, shown) VALUES (-50, -50, 'Sample Value1', 0, CURRENT_DATE);
INSERT INTO text(id, text_set_id, text, _order_, shown) VALUES (-51, -50, 'Sample Value2', 1, NULL);




INSERT INTO `user`(id, email, username, password_hash) VALUES (30, 'helloworld@java.com', 'helloWorld', 'HelloWorld123');
INSERT INTO `user`(id, email, username, password_hash) VALUES (31, 'helloworld2@java.com', 'helloWorld2', 'HelloWorld123');
INSERT INTO text_set(id, owner_id, title, description) VALUES (30, 30, 'No Texts List', 'no');
INSERT INTO reader(id, user_id, text_set_id) VALUES (31, 31, 30);



INSERT INTO `user`(id, email, username, password_hash) VALUES (60, 'test-id-60@gmail.com', 'test-id-60', 'HelloWorld123');
INSERT INTO `user`(id, email, username, password_hash) VALUES (61, 'test-id-61@gmail.com', 'test-id-61', 'HelloWorld123');

INSERT INTO text_set(id, owner_id, title, description) VALUES(60, 60, '', '');

INSERT INTO text(id, text_set_id, _order_, shown, text) VALUES (60, 60, 0, '2020-05-05', 'World');
INSERT INTO text(id, text_set_id, _order_, shown, text) VALUES (61, 60, 0, NULL, 'Hello');

INSERT INTO reader(id, user_id, text_set_id) VALUES (61, 61, 60);



INSERT INTO `user`(id, email, username, password_hash) VALUES (110, 'test-id-110@gmail.com', 'test-id-110', 'HelloWorld123');
INSERT INTO `user`(id, email, username, password_hash) VALUES (111, 'test-id-111@gmail.com', 'test-id-111', 'HelloWorld123');
INSERT INTO text_set(id, owner_id, title, description) VALUES(110, 110, '', '');
INSERT INTO reader(id, user_id, text_set_id) VALUES (110, 111, 110);
INSERT INTO join_link(id, text_set_id, code, active_until) VALUES(110, 110, 'abc', '2100-05-05');
INSERT INTO join_link(id, text_set_id, code, active_until) VALUES(111, 110, 'abc', '2100-05-05');
INSERT INTO join_link(id, text_set_id, code, active_until) VALUES(112, 110, 'abc', '2100-05-05');