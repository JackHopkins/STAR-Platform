# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table agent_wrapper (
  id                        integer primary key AUTOINCREMENT,
  name                      varchar(255),
  verified                  integer(1),
  date                      timestamp)
;

create table package (
  id                        integer primary key AUTOINCREMENT,
  name                      varchar(255),
  verified                  integer(1),
  date                      timestamp)
;

create table physics_wrapper (
  id                        integer primary key AUTOINCREMENT,
  name                      varchar(255),
  verified                  integer(1),
  date                      timestamp)
;

create table resource (
  agent_wrapper_id          integer not null,
  location                  varchar(255),
  name                      varchar(255))
;

alter table resource add constraint fk_resource_agent_wrapper_1 foreign key (agent_wrapper_id) references agent_wrapper (id);
create index ix_resource_agent_wrapper_1 on resource (agent_wrapper_id);



# --- !Downs

PRAGMA foreign_keys = OFF;

drop table agent_wrapper;

drop table package;

drop table physics_wrapper;

drop table resource;

PRAGMA foreign_keys = ON;

