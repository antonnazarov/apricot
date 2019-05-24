alter table apricot_project add column erd_notation varchar(20);
update apricot_project set erd_notation = 'IDEF1x';
alter table apricot_project alter column erd_notation varchar(20) not null;
