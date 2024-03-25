delete from remark;
delete from book;
delete from author;
delete from genre;
insert into author(id, full_name)
values (1, 'Author_1'), (2, 'Author_2'), (3, 'Author_3');

insert into genre(id, name)
values (1, 'Genre_1'), (2, 'Genre_2'), (3, 'Genre_3');

insert into book(id, title, author_id, genre_id)
values (1, 'BookTitle_1', 1, 1), (2, 'BookTitle_2', 2, 2), (3, 'BookTitle_3', 3, 3);

insert into remark(id, remark_text, book_id)
values (1, 'Remark_11', 1), (2, 'Remark_21', 2), (3, 'Remark_22', 2), (4, 'Remark_31', 3), (5, 'Remark_32', 3), (6, 'Remark_33', 3);
