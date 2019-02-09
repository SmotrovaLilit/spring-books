insert into book (id, title, year, description, author_id) values (1, 'test book title(1)', 1994, 'test book description(1)', 1), (2, 'test book title(2)', 1995, 'test book description(2)', 1);
insert into book (id, title, year, description, author_id) values (3, 'test book without genre', 1994, 'test book description', 1);
insert into book_genre (book_id, genre_id) values (1, 1), (1, 2), (2, 1)
