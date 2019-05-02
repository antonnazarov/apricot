alter table apricot_view add column is_current boolean;
alter table apricot_view add column detail_level varchar(10);

update apricot_view set is_current = false, detail_level='DEFAULT';
update apricot_view set is_current = true where is_general=true;

alter table apricot_view alter column is_current boolean not null;
alter table apricot_view alter column detail_level varchar(10) not null;