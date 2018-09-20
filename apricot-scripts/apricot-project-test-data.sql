-------------------------
-- add test data
-------------------------
insert into apricot_table values (10, 'MY TABLE 1');

insert into apricot_column values (101, 10, 'aggre_1', 1, false, 'int', null);
insert into apricot_column values (102, 10, 'aggre_2', 2, false, 'int', null);
insert into apricot_column values (103, 10, 'name', 3, false, 'varchar', '45');
insert into apricot_column values (104, 10, 'surename', 4, false, 'varchar', '50');
insert into apricot_column values (105, 10, 'country_id', 5, false, 'varchar', '20');
insert into apricot_column values (106, 10, 'age', 6, true, 'int', null);
insert into apricot_column values (107, 10, 'ship_id', 7,false, 'int', null);

insert into apricot_constraint values (1000, 'PK_TABLE_1', 'PRIMARY_KEY', 10);
insert into apricot_constraint values (1001, 'UNIQUE_ID', 'UNIQUE', 10);
insert into apricot_constraint values (1002, 'UNIQUE_NAME_ID', 'UNIQUE_INDEX', 10);
insert into apricot_constraint values (1003, 'FK_SHIP', 'FOREIGN_KEY', 10);

insert into apricot_column_in_constraint values (1000, 101, 1);
insert into apricot_column_in_constraint values (1000, 102, 2);
insert into apricot_column_in_constraint values (1001, 105, 1);
insert into apricot_column_in_constraint values (1002, 103, 1);
insert into apricot_column_in_constraint values (1002, 104, 2);
insert into apricot_column_in_constraint values (1003, 107, 1);

-------------------------
insert into apricot_table values (11, 'REF_SHIP');

insert into apricot_column values (201, 11, 'ship_id', 1, false, 'int', null);
insert into apricot_column values (202, 11, 'ship_name', 2, false, 'varchar', '200');
insert into apricot_column values (203, 11, 'ship_weight', 3, false, 'int', null);

insert into apricot_constraint values (2000, 'PK_REF_SHIP', 'PRIMARY_KEY', 11);
insert into apricot_constraint values (2001, 'NAME_IDX', 'NON_UNIQUE_INDEX', 11);

insert into apricot_column_in_constraint values (2000, 201, 1);
insert into apricot_column_in_constraint values (2001, 202, 1);

-------------------------
insert into apricot_relationship values (2000, 1003);
-------------------------

select * from apricot_table;
select * from apricot_column;
select * from apricot_constraint;
select * from apricot_column_in_constraint;
select * from apricot_relationship;
-------------------------
select * from vw_column;
-------------------------
