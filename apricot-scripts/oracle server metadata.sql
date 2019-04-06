select * from user_tables;
select * from USER_TAB_COLUMNS;
select * from USER_CONSTRAINTS;

select * from USER_CONSTRAINTS where constraint_name ='SYS_C007552' or r_constraint_name ='SYS_C007552';
select * from USER_CONSTRAINTS where table_name = UPPER('person_feature_history')

-- select * from USER_CATALOG;

select * from USER_CONS_COLUMNS;
select * from USER_CONS_COLUMNS where constraint_name='PERSON_DEPARTMENT_IDX';

-- SYS_C007611

select * from USER_INDEXES;


select * from USER_CONSTRAINTS where constraint_name ='SYS_C007578'

select * from USER_IND_COLUMNS;