-- tables of the metamodel
create table apricot_table (
   table_id long identity primary key,
   table_name varchar(100) not null
);
-------------------------

-- columns of the metamodel
create table apricot_column (
   column_id long identity primary key,
   table_id long not null,
   column_name varchar(100) not null,
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
   constraint_id long identity primary key,
   constraint_name varchar(100) not null,
   constraint_type varchar(20) not null, -- types: PRIMARY_KEY, FOREIGN_KEY, UNIQUE, UNIQUE_INDEX, NON_UNIQUE_INDEX
   table_id long not null
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


