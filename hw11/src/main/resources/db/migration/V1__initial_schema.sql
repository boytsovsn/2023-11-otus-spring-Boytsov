create table person
(
    id         bigserial    not null primary key,
    last_name  varchar(50)  not null,
    age        int not null
);

create table notes
(
    id           bigserial   not null primary key,
    note_text    varchar(250) not null,
    person_id    bigint not null references person (id)
);
create index idx_notes_person_id on notes (person_id);

create table authors (
                         id bigserial,
                         full_name varchar(255),
                         primary key (id)
);

create table genres (
                        id bigserial,
                        name varchar(255),
                        primary key (id)
);

create table books (
                       id bigserial,
                       title varchar(255),
                       author_id bigint references authors (id),
                       genre_id bigint references genres(id),
                       primary key (id)
);

create table remarks (
                         id bigserial,
                         remark_text varchar(255),
                         book_id bigint references books(id),
                         primary key (id)
);
