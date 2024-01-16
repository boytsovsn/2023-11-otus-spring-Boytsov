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
    book_id bigint references books(id) on delete cascade,
    primary key (id)
);
