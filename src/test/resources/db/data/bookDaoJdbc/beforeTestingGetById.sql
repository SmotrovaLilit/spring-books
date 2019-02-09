alter table genre alter column id restart with 1; insert into book (id, title, year, description, author_id) values (1, 'test book title', 1994, 'test book description', 1)
