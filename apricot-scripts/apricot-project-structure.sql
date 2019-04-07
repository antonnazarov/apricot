--
-- tables of the metamodel
--
-- 04/01/2019 v.1.1, the apricot_project was added
-- 24/02/2019 v.1.2 added table apricot_app_parameters
-- 07/04/2019 v.1.3 added unique constraints for all major tables
--
-- PROJECT
--
create table apricot_project (
   project_id long identity primary key,
   project_name varchar(250) not null,
   project_description varchar(2000),
   target_database varchar(50) not null,
   is_current boolean not null,
   project_created datetime not null,
   unique key project_name_unique_key (project_name)
);

create table apricot_project_parameter (
   parameter_id long identity primary key,
   project_id long not null,
   parameter_name varchar(500) not null,
   parameter_value varchar(2000) not null,
   unique key parameter_unique_key (project_id, parameter_name)
);

alter table apricot_project_parameter
   add foreign key(project_id)
   references apricot_project(project_id);

--
-- SNAPSHOT
--
create table apricot_snapshot (
   snapshot_id long identity primary key,
   project_id long not null,
   snapshot_name varchar(250) not null,
   snapshot_created datetime not null,
   snapshot_updated datetime,
   snapshot_comment varchar(500),
   is_default boolean not null,
   unique key snapshot_unique_key (project_id, snapshot_name)
);

alter table apricot_snapshot 
   add foreign key(project_id)
   references apricot_project(project_id);

--
-- LAYOUT
--
create table apricot_view (
   view_id long identity primary key,
   project_id long not null,
   view_name varchar(100) not null,
   view_comment varchar(2000),
   view_created datetime not null,
   view_updated datetime,
   is_general boolean not null,
   ordinal_position int not null,
   unique key view_unique_key (project_id, view_name)
);

alter table apricot_view
   add foreign key(project_id)
   references apricot_project(project_id);

create table apricot_object_layout (
   layout_id long identity primary key,
   view_id long not null,
   object_type varchar(25) not null,
   object_name varchar(250) not null,
   object_layout varchar(1000) not null,
   unique key object_layout_unique_key (view_id, object_name)
);

alter table apricot_object_layout
   add foreign key(view_id)
   references apricot_view(view_id);

--
-- TABLE
--
create table apricot_table (
   table_id long identity primary key,
   table_name varchar(100) not null,
   snapshot_id long not null,
   unique key table_unique_key (snapshot_id, table_name)
);

alter table apricot_table 
   add foreign key(snapshot_id)
   references apricot_snapshot(snapshot_id);
-------------------------

-- columns of the metamodel
create table apricot_column (
   column_id long identity primary key,
   table_id long not null,
   column_name varchar(100) not null,
   ordinal_position int not null,
   is_nullable boolean not null,
   data_type varchar(25) not null,
   value_length varchar(10),
   unique key column_unique_key (table_id, column_name)
);

alter table apricot_column 
   add foreign key(table_id)
   references apricot_table(table_id);
-------------------------

-- constraint
create table apricot_constraint (
   constraint_id long identity primary key,
   constraint_name varchar(100) not null,
   constraint_type varchar(20) not null, -- types: PRIMARY_KEY, FOREIGN_KEY, UNIQUE, UNIQUE_INDEX, NON_UNIQUE_INDEX
   table_id long not null,
   unique key constraint_unique_key (table_id, constraint_name)
);

alter table apricot_constraint
   add foreign key(table_id)
   references apricot_table(table_id);
-------------------------

-- columns in constraint (M-to-M association between constraints and columns)
create table apricot_column_in_constraint (
   constraint_id long not null,
   column_id long not null,
   ordinal_position int not null
);

alter table apricot_column_in_constraint add primary key (constraint_id, column_id);

alter table apricot_column_in_constraint
   add foreign key(constraint_id)
   references apricot_constraint(constraint_id);

alter table apricot_column_in_constraint
   add foreign key(column_id)
   references apricot_column(column_id);
-------------------------

-- parent/child relationships
create table apricot_relationship (
   relationship_id long identity primary Key,
   parent_constraint_id long not null,
   child_constraint_id long not null,
   unique key relationship_unique_key (parent_constraint_id, child_constraint_id)
);

alter table apricot_relationship
   add foreign key(parent_constraint_id)
   references apricot_constraint(constraint_id);

alter table apricot_relationship
   add foreign key(child_constraint_id)
   references apricot_constraint(constraint_id);
-------------------------

-- the application wide configuration parameters
create table apricot_app_parameter (
   app_parameter_id long identity primary Key,
   app_parameter_name varchar(35) not null,
   app_parameter_value varchar(1000) not null,
   unique key app_parameter_unique_key (app_parameter_name)
);

insert into apricot_app_parameter (app_parameter_name, app_parameter_value) values ('database_version', '1.3');
insert into apricot_app_parameter (app_parameter_name, app_parameter_value) values ('MSSQLServer.datatypes', 'bigint;int;smallint;tinyint;bit;decimal;float;numeric;varchar;char;nvarchar;text;varbinary;date;datetime;datetime2');

-- views
create view vw_column as
select atb.table_name, ac.column_name, ac.ordinal_position, ac.is_nullable, ac.data_type, ac.value_length,
   case
      when cic.column_id is not null then ctr.constraint_name
      else null
   end as primary_key,
   t.constraint_name as foreign_key,   
   t.table_name as parent_table,
   atb.table_id, ac.column_id
from apricot_table atb
join apricot_column ac on ac.table_id = atb.table_id
left join apricot_constraint ctr on atb.table_id = ctr.table_id and ctr.constraint_type = 'PRIMARY_KEY'
left join apricot_column_in_constraint cic on cic.constraint_id = ctr.constraint_id and cic.column_id = ac.column_id
left join (
   select ac_1.constraint_id, ac_1.constraint_name, ac_1.table_id, cic_1.column_id, at_1.table_name
   from apricot_constraint ac_1
   join apricot_column_in_constraint cic_1 on cic_1.constraint_id = ac_1.constraint_id
   join apricot_relationship ar on ar.child_constraint_id = ac_1.constraint_id
   join apricot_constraint ac_2 on ar.parent_constraint_id = ac_2.constraint_id
   join apricot_table at_1 on at_1.table_id = ac_2.table_id
   where ac_1.constraint_type = 'FOREIGN_KEY'
) t on t.column_id = ac.column_id
order by atb.table_name, ac.ordinal_position;


