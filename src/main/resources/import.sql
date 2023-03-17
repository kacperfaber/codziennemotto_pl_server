INSERT INTO `user`(id, email, username, password_hash) VALUES (1, 'kacperf1234@gmail.com', 'kacperfaber', 'HelloWorld123');

INSERT INTO text_set(id, owner_id, title, description) VALUES (2, 1, 'HelloWorld!', 'This is the greatest set in the page.');

INSERT INTO text(id, text_set_id, text, _order_) VALUES (1, 2, 'Hello WOrld, ! #1', 0);
INSERT INTO text(id, text_set_id, text, _order_) VALUES (2, 2, 'Hello WOrld, ! #2', 5);