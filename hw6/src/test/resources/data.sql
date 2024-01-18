insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into remarks(remark_text, book_id)
values ('Remark_11', 1), ('Remark_21', 2), ('Remark_22', 2), ('Remark_31', 3), ('Remark_32', 3), ('Remark_33', 3);
