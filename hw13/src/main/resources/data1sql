delete from remark;
delete from book;
delete from author;
delete from genre;

insert into author(id, full_name)
values (1, 'Конан Дойл'), (2, 'Агата Кристи'), (3, 'Пушкин А.С.');

insert into genre(id, name)
values (1, 'Детектив'), (2, 'Роман'), (3, 'Поэма');

insert into book(id, title, author_id, genre_id)
values (1, 'Шерлок Холмс', 1, 1), (2, 'Восточный экспресс', 2, 1), (3, 'Капитанская дочка', 3, 3);
ALTER SEQUENCE book_id_seq RESTART WITH 4;

UPDATE BOOK SET GENRE_ID=2 WHERE ID=3;
UPDATE BOOK SET AUTHOR_ID=3 WHERE ID=2;
UPDATE BOOK SET AUTHOR_ID=2 WHERE ID=3;

delete from users;

insert into users(id, login, password, role, lock) values
(1, 'admin', 'password', 'EDITOR', false),
(2, 'user', 'password', 'USER', false);

delete from acl_entry;
delete from acl_object_identity;
delete from acl_sid;
delete from acl_class;


INSERT INTO acl_sid (id, principal, sid) VALUES
(1, true, 'admin'),
(2, true, 'user');

INSERT INTO acl_class (id, class) VALUES
(1, 'ru.otus.hw.models.entities.Book');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 1, false),
(2, 1, 2, NULL, 1, false),
(3, 1, 3, NULL, 1, false),
(4, 1, 4, NULL, 1, false),
(5, 1, 5, NULL, 1, false);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
(1, 1, 1, 1, 1, true, true, true),
(2, 2, 1, 2, 8, true, true, true),
(3, 2, 2, 2, 2, true, true, true),
(4, 2, 3, 2, 1, true, true, true),
(5, 2, 4, 1, 2, true, true, true),
(6, 2, 5, 1, 1, true, true, true),
(7, 3, 1, 1, 8, true, true, true),
(8, 3, 2, 1, 1, true, true, true),
(9, 3, 3, 2, 8, false,true, true),
(10,3, 4, 2, 2, true, true, true),
(11,3, 5, 2, 1, true, true, true),
(12,4, 1, 1, 8, true, true, true),
(13,4, 2, 1, 1, true, true, true),
(14,4, 3, 2, 1, true, true, true),
(15,5, 1, 1, 2, true, true, true),
(16,5, 2, 1, 1, true, true, true),
(17,5, 3, 2, 1, true, true, true);
