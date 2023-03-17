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
INSERT INTO text(id, text_set_id, text, _order_) VALUES (101, 1, 'Sample Value2', 0);