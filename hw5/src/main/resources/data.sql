insert into authors(full_name)
values ('Конан Дойл'), ('Агата Кристи'), ('Пушкин А.С.');

insert into genres(name)
values ('Детектив'), ('Роман'), ('Поэма');

insert into books(title, author_id, genre_id)
values ('Шерлок Холмс', 1, 1), ('Восточный экспресс', 2, 1), ('Капитанская дочка', 3, 3);

UPDATE BOOKS SET GENRE_ID=2 WHERE ID=3;
UPDATE BOOKS SET AUTHOR_ID=3 WHERE ID=2;
UPDATE BOOKS SET AUTHOR_ID=2 WHERE ID=3;

