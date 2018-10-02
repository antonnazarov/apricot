-- all tables
select table_name 
from INFORMATION_SCHEMA.tables
where table_type = 'BASE TABLE'
order by table_name 

select * from Intermediary_Account.INFORMATION_SCHEMA.tables
order by table_name 

-- all columns
select table_name, column_name, ordinal_position, is_nullable, data_type, character_maximum_length
from Intermediary_Account.INFORMATION_SCHEMA.COLUMNS
order by table_name, ordinal_position

-- a list of all major constraints (primary keys, foreign keys, unique)
select table_name, constraint_type, constraint_name
from INFORMATION_SCHEMA.TABLE_CONSTRAINTS
order by table_name, constraint_type, constraint_name

-- all "keys" - fields, included in constraints
select table_name, constraint_name, ordinal_position, column_name
from INFORMATION_SCHEMA.KEY_COLUMN_USAGE
order by table_name, constraint_name, ordinal_position

-- the association between primary key constraints and corresponding foreign keys
select unique_constraint_name, constraint_name 
from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS
order by unique_constraint_name

-- DO NOT FORGET ABOUT VIEWS
select * from Intermediary_Account.INFORMATION_SCHEMA.VIEWS;
select * from Intermediary_Account.INFORMATION_SCHEMA.VIEW_TABLE_USAGE;
select * from Intermediary_Account.INFORMATION_SCHEMA.VIEW_COLUMN_USAGE;


-- looks like a duplication of the KEY_COLUMN_USAGE- table
select table_name, constraint_name, column_name
from Intermediary_Account.INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE
order by table_name, constraint_name;



select * from sys.indexes where object_id = 1397580017
select * from sys.tables where type = 'U'
select * from sys.objects where object_id = 1397580017
select * from sys.objects where parent_object_id = 1397580017
select * from sys.all_columns where object_id = 1397580017
select * from sys.index_columns where object_id = 1397580017

-- candidate indexes (to be added)
select t.name as table_name, t.object_id, idx.name as index_name, idx.index_id, idx.is_primary_key, idx.is_unique, idx.is_unique_constraint
from sys.indexes idx
join sys.tables t on t.object_id = idx.object_id
where t.type = 'U' and idx.name is not null and idx.is_primary_key = 0 and idx.is_unique_constraint = 0
order by t.name, idx.name

-- having object_id (210099789) and index_id (2)
select name as column_name
from sys.index_columns ic
join sys.all_columns c on c.column_id = ic.column_id and c.object_id = ic.object_id
where ic.object_id = 210099789 and ic.index_id = 2
order by ic.key_ordinal

--  candidate indexes columns
select t.name as table_name, t.object_id, idx.name as index_name, idx.is_unique, idx.is_primary_key, idx.is_unique, idx.is_unique_constraint,
   cl.name as column_name
from sys.indexes idx
join sys.tables t on t.object_id = idx.object_id
join sys.all_columns cl on cl.object_id = t.object_id
join sys.index_columns ic on ic.object_id = t.object_id and ic.column_id = cl.column_id
where t.type = 'U' and idx.name is not null and idx.is_primary_key = 0
order by t.name, idx.name



select * from sys.indexes where name = 'triller_idx'
select * from sys.objects where type_desc = 'USER_TABLE'