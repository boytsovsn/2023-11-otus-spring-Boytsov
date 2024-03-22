create table IF NOT EXISTS system_message (id integer not null, content varchar(255), primary key (id));

CREATE TABLE IF NOT EXISTS acl_sid (
    id number(20) NOT NULL AUTO_INCREMENT,
    principal number(1) NOT NULL,
    sid varchar(100) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_uk_1 UNIQUE (sid, principal) );

CREATE TABLE IF NOT EXISTS acl_class (
    id number(20) NOT NULL AUTO_INCREMENT,
    class varchar(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_uk_2 UNIQUE (class) );
 
CREATE TABLE IF NOT EXISTS acl_entry (
  id number(20) NOT NULL AUTO_INCREMENT,
  acl_object_identity number(20) NOT NULL,
  ace_order number(11) NOT NULL,
  sid number(20) NOT NULL,
  mask number(11) NOT NULL,
  granting number(1) NOT NULL,
  audit_success number(1) NOT NULL,
  audit_failure number(1) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT unique_uk_4 UNIQUE (acl_object_identity,ace_order)
);
 
CREATE TABLE IF NOT EXISTS acl_object_identity (
  id number(20) NOT NULL AUTO_INCREMENT,
  object_id_class number(20) NOT NULL,
  object_id_identity number(20) NOT NULL,
  parent_object number(20) DEFAULT NULL,
  owner_sid number(20) DEFAULT NULL,
  entries_inheriting number(1) NOT NULL,
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