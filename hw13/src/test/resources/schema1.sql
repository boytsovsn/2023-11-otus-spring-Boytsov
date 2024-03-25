create table author (
                        id bigserial not null,
                        full_name varchar(255),
                        primary key (id)
);
create table book (
                      author_id bigint,
                      genre_id bigint,
                      id bigserial not null,
                      title varchar(255),
                      primary key (id)
);
create table genre (
                       id bigserial not null,
                       name varchar(255),
                       primary key (id)
);
create table remark (
                        book_id bigint,
                        id bigserial not null,
                        remark_text varchar(255),
                        primary key (id)
);

alter table if exists book add constraint FKklnrv3weler2ftkweewlky958 foreign key (author_id) references author;
alter table if exists book add constraint FKm1t3yvw5i7olwdf32cwuul7ta foreign key (genre_id) references genre;
alter table if exists remark add constraint FK3r3qrq3bm49cg50n7na33qf4l foreign key (book_id) references book;

create table users (
                       lock boolean,
                       roles smallint check (roles between 0 and 1),
                       id bigserial not null,
                       email varchar(255),
                       firstname varchar(255),
                       lastname varchar(255),
                       login varchar(255),
                       password varchar(255),
                       role varchar(255),
                       surname varchar(255),
                       primary key (id)
);

CREATE TABLE IF NOT EXISTS acl_sid (
                                       id bigserial NOT NULL,
                                       principal int NOT NULL,
                                       sid varchar(100) NOT NULL,
                                       PRIMARY KEY (id),
                                       CONSTRAINT unique_uk_1 UNIQUE (sid,principal)
);

CREATE TABLE IF NOT EXISTS acl_class (
                                         id bigserial NOT NULL,
                                         class varchar(255) NOT NULL,
                                         PRIMARY KEY (id),
                                         CONSTRAINT unique_uk_2 UNIQUE (class)
);

CREATE TABLE IF NOT EXISTS acl_entry (
                                         id bigserial NOT NULL,
                                         acl_object_identity bigint NOT NULL,
                                         ace_order int NOT NULL,
                                         sid bigint NOT NULL,
                                         mask int NOT NULL,
                                         granting int NOT NULL,
                                         audit_success int NOT NULL,
                                         audit_failure int NOT NULL,
                                         PRIMARY KEY (id),
                                         CONSTRAINT unique_uk_4 UNIQUE (acl_object_identity,ace_order)
);

CREATE TABLE IF NOT EXISTS acl_object_identity (
                                                   id bigserial NOT NULL,
                                                   object_id_class bigint NOT NULL,
                                                   object_id_identity bigint NOT NULL,
                                                   parent_object bigint DEFAULT NULL,
                                                   owner_sid bigint DEFAULT NULL,
                                                   entries_inheriting int NOT NULL,
                                                   PRIMARY KEY (id),
                                                   CONSTRAINT unique_uk_3 UNIQUE (object_id_class,object_id_identity)
);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

--
-- Constraints for table acl_object_identity
--
ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);