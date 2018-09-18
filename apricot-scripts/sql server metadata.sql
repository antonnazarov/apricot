-- all tables
select table_name 
from Intermediary_Account.INFORMATION_SCHEMA.tables
where table_type = 'BASE TABLE'
order by table_name 

-- all columns
select table_name, column_name, ordinal_position, is_nullable, data_type, character_maximum_length
from Intermediary_Account.INFORMATION_SCHEMA.COLUMNS
order by table_name, ordinal_position

-- a list of all major constraints (primary keys, foreign keys, unique)
select table_name, constraint_type, constraint_name
from Intermediary_Account.INFORMATION_SCHEMA.TABLE_CONSTRAINTS
order by table_name, constraint_type, constraint_name

-- all "keys" - fields, included in constraints
select table_name, constraint_name, ordinal_position, column_name
from Intermediary_Account.INFORMATION_SCHEMA.KEY_COLUMN_USAGE
order by table_name, constraint_name, ordinal_position

-- the association between primary key constraints and corresponding foreign keys
select unique_constraint_name, constraint_name 
from Intermediary_Account.INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS
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
select * from sys.tables
select * from sys.objects where object_id = 1397580017
select * from sys.objects where parent_object_id = 1397580017
select * from sys.all_columns where object_id = 1397580017
select * from sys.index_columns where object_id = 1397580017



select * from sys.indexes where name = 'triller_idx'
select * from sys.objects where type_desc = 'USER_TABLE'