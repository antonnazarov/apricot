-- clean all data
delete from apricot_column_in_constraint;
delete from apricot_relationship;
delete from apricot_constraint;
delete from apricot_column;
delete from apricot_table;

delete from from apricot_object_layout;
delete from apricot_view;
delete from apricot_snapshot;
delete from apricot_project_parameter;
delete from apricot_project;

insert into apricot_project values (1, 'Intermediary_Account', 'The realistic structure for Intermediary_Account database', 'MSSQLServer', true, '2019-01-08');
insert into apricot_project values (2, 'Intermediary_Agreement', 'The realistic structure for Intermediary_Agreement database', 'MSSQLServer', false, '2019-01-08');
insert into apricot_project values (3, 'Intermediary_Party', 'The realistic structure for Intermediary_Party database', 'MSSQLServer', false, '2019-01-08');
insert into apricot_project values (4, 'Intermediary_Compensation', 'The realistic structure for Intermediary_Compensation database', 'MSSQLServer', false, '2019-01-08');

insert into apricot_snapshot values (1, 1, 'Reverse Eng from 08/01/2019', '2019-01-08', null, 'Test of the real database Intermediary_Account', true);
insert into apricot_snapshot values (2, 2, 'Reverse Eng from 08/01/2019', '2019-01-08', null, 'Test of the real database Intermediary_Agreement', true);
insert into apricot_snapshot values (3, 3, 'Reverse Eng from 08/01/2019', '2019-01-08', null, 'Test of the real database Intermediary_Party', true);
insert into apricot_snapshot values (4, 4, 'Reverse Eng from 08/01/2019', '2019-01-08', null, 'Test of the real database Intermediary_Compensation', true);

insert into apricot_view values (1, 1, 'Main View', 'The main view of the project. It presents the whole list of the tables of the project', '2019-01-11', null, true, 0);

select * from apricot_project;
select * from apricot_snapshot;

