INSERT INTO `user`(id, email, username, password_hash) VALUES (1, 'kacperf1234@gmail.com', 'kacperfaber', 'HelloWorld123');
INSERT INTO `user`(id, email, username, password_hash) VALUES (2, 'marekfaber@gmail.com', 'MakerXD', 'HelloTest555_');
INSERT INTO `user`(id, email, username, password_hash) VALUES (3, 'darekfatek@gmail.com', 'Darek555', 'HelloTest');

INSERT INTO text_set(id, owner_id, title, description) VALUES (1, 1, 'HelloWorld!', 'This is the greatest set in the page.');

INSERT INTO reader(id, user_id, text_set_id) VALUES(1, 2, 1);
INSERT INTO reader(id, user_id, text_set_id) VALUES(2, 3, 1);