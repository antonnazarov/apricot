-- tables of the metamodel
create table apricot_table (
   table_id identity primary key,
   table_name varchar(50) not null
);
-------------------------

-- columns of the metamodel
create table apricot_column (
   column_id identity primary key,
   table_id int not null,
   column_name varchar(50) not null,
   ordinal_position int not null,
   is_nullable boolean not null,
   data_type varchar(25) not null,
   value_length varchar(10)
);

alter table apricot_column 
   add foreign key(table_id)
   references apricot_table(table_id);
-------------------------

-- constraint
create table apricot_constraint (
   constraint_id identity primary key,
   constraint_name varchar(100) not null,
   constraint_type varchar(20) not null, -- types: PRIMARY_KEY, FOREIGN_KEY, UNIQUE, UNIQUE_INDEX, NON_UNIQUE_INDEX
   table_id int not null
);

alter table apricot_constraint
   add foreign key(table_id)
   references apricot_table(table_id);
-------------------------

-- columns in constraint (M-to-M association between constraints and columns)
create table apricot_column_in_constraint (
   constraint_id int not null,
   column_id int not null,
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
   parent_constraint_id int not null,
   child_constraint_id int not null
);

alter table apricot_relationship add primary key (parent_constraint_id, child_constraint_id);

alter table apricot_relationship
   add foreign key(parent_constraint_id)
   references apricot_constraint(constraint_id);

alter table apricot_relationship
   add foreign key(child_constraint_id)
   references apricot_constraint(constraint_id);
-------------------------

-- views
create view vw_column as
select atb.table_name, ac.column_name, ac.ordinal_position, ac.is_nullable, ac.data_type, ac.value_length,
   case
      when cic.column_id is not null then ctr.constraint_name
      else null
   end as primary_key,
   case
      when cic1.column_id is not null then ctr1.constraint_name
      else null
   end as foreign_key,
   atb.table_id, ac.column_id
from apricot_table atb
join apricot_column ac on ac.table_id = atb.table_id
left outer join apricot_constraint ctr on atb.table_id = ctr.table_id and ctr.constraint_type = 'PRIMARY_KEY'
left outer join apricot_column_in_constraint cic on cic.constraint_id = ctr.constraint_id and cic.column_id = ac.column_id
left outer join apricot_constraint ctr1 on atb.table_id = ctr1.table_id and ctr1.constraint_type = 'FOREIGN_KEY'
left outer join apricot_column_in_constraint cic1 on cic1.constraint_id = ctr1.constraint_id and cic1.column_id = ac.column_id
order by atb.table_name, ac.ordinal_position;


