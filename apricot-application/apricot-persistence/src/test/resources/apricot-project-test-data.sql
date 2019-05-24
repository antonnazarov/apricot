-------------------------
-- Delete all data
-------------------------
delete from apricot_column_in_constraint;
delete from apricot_relationship;
delete from apricot_constraint;
delete from apricot_column;
delete from apricot_table;
delete from apricot_view;
delete from apricot_snapshot;
delete from apricot_project_parameter;
delete from apricot_project;

-------------------------
-- add test data
-------------------------
insert into apricot_project values (1, 'test_project_001', 'This is the test project', 'MSSQLServer', true, '2019-01-04', 'IDEF1x');
insert into apricot_project_parameter values (1, 1, 'test', 'test');
insert into apricot_snapshot values (1, 1, '2019-01-04', '2019-01-04', 'test comment', true);
insert into apricot_view values (1, 1, 'main view', null, '2019-01-04', '2019-01-04', true, 0);

insert into apricot_table values (10, 'MY TABLE 1', 1);
insert into apricot_column values (101, 10, 'aggre_1', 1, false, 'int', null);
insert into apricot_column values (102, 10, 'aggre_2', 2, false, 'int', null);
insert into apricot_column values (103, 10, 'name', 3, false, 'varchar', '45');
insert into apricot_column values (104, 10, 'surename', 4, false, 'varchar', '50');
insert into apricot_column values (105, 10, 'country_id', 5, false, 'varchar', '20');
insert into apricot_column values (106, 10, 'age', 6, true, 'int', null);
insert into apricot_column values (107, 10, 'ship_id', 7,false, 'int', null);
insert into apricot_column values (108, 10, 'calc_method_id', 8,false, 'int', null);

insert into apricot_constraint values (1000, 'PK_TABLE_1', 'PRIMARY_KEY', 10);
insert into apricot_constraint values (1001, 'UNIQUE_ID', 'UNIQUE', 10);
insert into apricot_constraint values (1002, 'UNIQUE_NAME_ID', 'UNIQUE_INDEX', 10);
insert into apricot_constraint values (1003, 'FK_SHIP', 'FOREIGN_KEY', 10);
insert into apricot_constraint values (1004, 'fk_calc_method_2387009', 'FOREIGN_KEY', 10);

insert into apricot_column_in_constraint values (1000, 101, 1);
insert into apricot_column_in_constraint values (1000, 102, 2);
insert into apricot_column_in_constraint values (1001, 105, 1);
insert into apricot_column_in_constraint values (1002, 103, 1);
insert into apricot_column_in_constraint values (1002, 104, 2);
insert into apricot_column_in_constraint values (1003, 107, 1);
insert into apricot_column_in_constraint values (1004, 108, 1);

-------------------------
insert into apricot_table values (11, 'REF_SHIP', 1);

insert into apricot_column values (201, 11, 'ship_id', 1, false, 'int', null);
insert into apricot_column values (202, 11, 'ship_name', 2, false, 'varchar', '200');
insert into apricot_column values (203, 11, 'ship_weight', 3, false, 'int', null);

insert into apricot_constraint values (2000, 'PK_REF_SHIP', 'PRIMARY_KEY', 11);
insert into apricot_constraint values (2001, 'NAME_IDX', 'NON_UNIQUE_INDEX', 11);

insert into apricot_column_in_constraint values (2000, 201, 1);
insert into apricot_column_in_constraint values (2001, 202, 1);

-------------------------
insert into apricot_relationship values (1, 2000, 1003);
-------------------------

insert into apricot_table values (12, 'CALC_METHOD', 1);
insert into apricot_column values (301, 12, 'method_id', 1, false, 'int', null);
insert into apricot_column values (302, 12, 'method_name', 2, false, 'varchar', '200');
insert into apricot_column values (303, 12, 'method_comment', 3, false, 'varchar', '1000');
insert into apricot_constraint values (3000, 'PK_CALC_METHOD', 'PRIMARY_KEY', 12);
insert into apricot_column_in_constraint values (3000, 301, 1);

insert into apricot_relationship values (2, 3000, 1004);
-------------------------

insert into apricot_table values (13, 'COMBINED_REFERENCE', 1);
insert into apricot_column values (401, 13, 'ref_prefix', 1, false, 'varchar', '10');
insert into apricot_column values (402, 13, 'ref_postfix', 2, false, 'varchar', '20');
insert into apricot_column values (403, 13, 'reference_comment', 3, false, 'varchar', '1000');
insert into apricot_constraint values (4000, 'COMBINED_REFERENCE_PK', 'PRIMARY_KEY', 13);
insert into apricot_column_in_constraint values (4000, 401, 1);
insert into apricot_column_in_constraint values (4000, 402, 2);

insert into apricot_table values (14, 'COMBINED_RFCHILD', 1);
insert into apricot_column values (501, 14, 'id', 1, false, 'int', null);
insert into apricot_column values (502, 14, 'ref_prefix', 2, false, 'varchar', '10');
insert into apricot_column values (503, 14, 'ref_postfix', 3, false, 'varchar', '20');
insert into apricot_constraint values (5000, 'RFCHILD_PK', 'PRIMARY_KEY', 14);
insert into apricot_column_in_constraint values (5000, 501, 1);
insert into apricot_constraint values (5100, 'RFCHILD_REF_FK', 'FOREIGN_KEY', 14);
insert into apricot_column_in_constraint values (5100, 502, 1);
insert into apricot_column_in_constraint values (5100, 503, 2);

insert into apricot_relationship values (3, 4000, 5100);

-------------------------
select * from apricot_table;
select * from apricot_column;
select * from apricot_constraint;
select * from apricot_column_in_constraint;
select * from apricot_relationship;
-------------------------
select * from vw_column;
-------------------------
