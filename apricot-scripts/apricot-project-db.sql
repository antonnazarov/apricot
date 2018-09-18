--
-- Apricot metadata. First version.
--

-- a storage of free formatted parameters of the project
create table apricot_project_parameter (
   parameter_name varchar(45) primary key,
   parameter_value varchar(1000) not null 
);

-- 
create table apricot_connection (
   connection_id int primary key,
   successful bit not null,
   created datetime not null,
   connection_url varchar(100) not null
);

create table apricot_connection_parameter (
   connection_parameter_name varchar(45) primary key,
   connection_parameter_value varchar(1000) not null
);

-- database snapshot
create table apricot_snapshot (
   snapshot_id int primary key,
   snapshot_name varchar(250),
   created datetime not null,
)

-- tables of the metamodel
create table apricot_table (
   table_id int primary key,
   table_name varchar(50) not null
);

-- columns of the metamodel
create table apricot_column (
   column_id int primary key,
   table_id int not null,
   column_name varchar(50) not null,
);

alter table apricot_column with check add constraint fk_column_table
   foreign key(table_id)
   references apricot_table(table_id);

