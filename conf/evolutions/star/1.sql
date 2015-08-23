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

create table resource_db (
  id                        integer primary key AUTOINCREMENT,
  location                  varchar(255),
  name                      varchar(255),
  agent                     integer)
;

alter table resource_db add constraint fk_resource_db_agent_1 foreign key (agent) references agent_wrapper (id);
create index ix_resource_db_agent_1 on resource_db (agent);



# --- !Downs

PRAGMA foreign_keys = OFF;

drop table agent_wrapper;

drop table package;

drop table physics_wrapper;

drop table resource_db;

PRAGMA foreign_keys = ON;

