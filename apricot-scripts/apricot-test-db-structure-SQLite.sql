--
-- The SQLite version of the test database.
--
-- reference religion
create table religion (
   religion_name varchar(50) primary key,
   religion_def varchar(500)
);

insert into religion values ('CHRIST PROT', 'Protestantism');
insert into religion values ('CHRIST CATH', 'Catolicismul este un sistem doctrinar exprimat');
insert into religion values ('BUDDHISM', 'Buddhism is the worlds fourth-largest religion');
insert into religion values ('ATHEISM', 'We believe in nothing, Lebowsky! Nothing!');

-- reference department
create table department (
   department_code varchar(20) primary key,
   department_name varchar(100) not null,
   stuff_total int not null,
   special_conditions varchar(500)
);

insert into department values ('C0001', 'IT', 200, 'no comments');
insert into department values ('CB004', 'Bookkeepers', 30, 'working in the down town');
insert into department values ('CS008', 'Service Department', 45, 'have special privileges');
insert into department values ('CS105', 'Security', 20, 'working in shifts');
insert into department values ('CP200', 'Production', 35, 'occupy the management building');
insert into department values ('CETD0', 'Executives', 3, 'having enormously high salaries');

-- reference payment_group
create table payment_group (
   group_id int primary key,
   pg_name varchar(100) not null,
   pg_definition varchar(500) not null,
   privileged_flag bit(1) not null,
   overtime_allowed_flag bit(1) not null
);

insert into payment_group values (10, 'Monthly Remuneration', 'that is payed monthly', '0', '1');
insert into payment_group values (20, 'Special Occasions', 'something which happens only seldom and irregularly', '1', '0');
insert into payment_group values (30, 'Bonus Group', 'bonuses related payments', '0', '0');

-- reference position
create table position (
   position_code varchar(10) not null,
   position_suffix varchar(5) not null,
   position_name varchar(100) not null,
   pos_comment varchar(1000),
   primary key(position_code, position_suffix)
);

insert into position values ('MNGR', 'S10', 'senior managers', 'test comment 1');
insert into position values ('MNGR', 'MD000', 'field managers', 'test comment 2');
insert into position values ('STDEV', 'G010', 'senior developers', 'test comment 3');
insert into position values ('STDEV', 'G020', 'enterprise architects', 'test comment 4');
insert into position values ('ANALYST', 'B100', 'business analysts', 'test comment 5');
insert into position values ('ANALYST', 'T001', 'technical analysts', 'test comment 6');
insert into position values ('ANALYST', 'E000', 'senior enterprise analysts', 'test comment 7');
insert into position values ('ANALYST', 'E010', 'enterprise analysts', 'test comment 8');
insert into position values ('SADM', 'D000', 'sys admins (default range)', 'test comment 9');

-- person - the key entity
create table person (
   person_id int primary key,
   tabel_nummer varchar(10) not null,
   first_name varchar(50) not null,
   second_name varchar(50) not null,
   religion varchar(50),  -- FK from religion
   department varchar(20) not null, -- FK from department
   manager int, -- auto FK from person
   position_code varchar(10) not null, -- FK from position
   position_suffix varchar(5) not null, -- FK from position
   foreign key (religion) references religion(religion_name),
   foreign key(department) references department(department_code),
   foreign key(manager) references person(person_id),
   foreign key (position_code, position_suffix) references position (position_code, position_suffix)
);

create unique index tabel_nummer_idx on person (tabel_nummer);
create index person_name_idx on person(first_name, second_name);
create index person_department_idx on person(department);

insert into person values (10, '234F', 'Don', 'Trottler', 'CHRIST CATH', 'CS008', null, 'F100', 'RM');
insert into person values (20, '234F01', 'Reinhardt', 'Coettzie', 'CHRIST CATH', 'CS008', 10, 'F100', 'RM');
insert into person values (30, '234F02', 'Bobby', 'Haal', 'CHRIST CATH', 'CS008', 10, 'F100', 'RM');
insert into person values (40, '234F03', 'Ron', 'Miglow', 'CHRIST CATH', 'CS008', 10, 'F100', 'RM');

-- associate: person_in_payment_group
create table person_in_payment_group (
   person_id int not null,
   payment_group_id int not null,
   primary_group_flag bit(1) not null,
   primary key (person_id, payment_group_id),
   foreign key(person_id) references person(person_id),
   foreign key(payment_group_id) references payment_group(group_id)
);

insert into person_in_payment_group values (10, 10, 0);
insert into person_in_payment_group values (20, 20, 1);
insert into person_in_payment_group values (30, 10, 0);
insert into person_in_payment_group values (40, 30, 0);
insert into person_in_payment_group values (40, 10, 1);

-- reference payment transaction type
create table payment_transaction_type (
   id int primary key,
   pt_name varchar(50) not null,
   pt_period varchar(10) not null
);

insert into payment_transaction_type values (0, 'M. REMUNERATION', 'MONTHLY');
insert into payment_transaction_type values (1, 'INTEREST', 'DAILY');
insert into payment_transaction_type values (2, 'Y BONUS', 'YEARLY');

-- payment transaction
create table payment_transaction (
   id int primary key,
   transaction_period varchar(7) not null,
   transaction_started_date date not null,
   transaction_finished_date date,
   transaction_overall_amount float not null,
   transaction_approved bit(1) not null,
   transaction_type int not null,
   foreign key(transaction_type) references payment_transaction_type(id)
);

create index transaction_startdate_idx on payment_transaction(transaction_started_date);
create index transaction_type_idx on payment_transaction(transaction_type);

insert into payment_transaction values (1, '20200101', '2020-01-01', '2020-01-31', 234900.32, 1, 1);

-- reference payment transaction type
create table payment_code (
   pcode varchar(10) primary key,
   pcode_name varchar(100) not null
);

insert into payment_code values ('HRATE', 'hourly rate');
insert into payment_code values ('MNDDED', 'mandatory deduction');
insert into payment_code values ('STAX', 'social taxation');
insert into payment_code values ('INTRST', 'monthly interest');
insert into payment_code values ('PREVDED', 'deductions of the previous period');

-- associate: pcode_in_payment_group
create table pcode_in_payment_group (
   pcode varchar(10) not null,
   payment_group_id int not null,
   foreign key(payment_group_id) references payment_group(group_id),
   foreign key(pcode) references payment_code(pcode)
);

create unique index pcode_in_payment_group_idx on pcode_in_payment_group (pcode, payment_group_id);

insert into pcode_in_payment_group values ('HRATE', 10);
insert into pcode_in_payment_group values ('HRATE', 20);
insert into pcode_in_payment_group values ('INTRST', 30);

-- key table: payroll
create table payroll (
   payroll_id int primary key,
   person_id int not null, -- FK from person
   payment_transaction_id int not null, -- FK from payment_transaction
   payment_code varchar(10) not null, -- FK from payment_code
   payment_period varchar(7) not null, -- formatted as MM/YYYY
   payment_amount float not null,
   constraint payroll_code_unique unique (person_id, payment_transaction_id, payment_code, payment_period),
   foreign key(person_id) references person(person_id),
   foreign key(payment_transaction_id) references payment_transaction(id),
   foreign key(payment_code) references payment_code(pcode)
);

create index payroll_person_period_idx on payroll (person_id, payment_period);
create index payroll_transaction_idx on payroll (payment_transaction_id);

insert into payroll values (1, 10, 1, 'HRATE', '2012020', 17895700.32);

-- reference: the personal feature
create table personal_feature_type (
   feature_code varchar(20) primary key,
   feature_field_type varchar(20) not null,
   feature_name varchar(100) not null,
   mandatory_flag bit(1) not null,
   screen_position int
);

insert into personal_feature_type values ('TLFHM', 'String', 'the home phone number', '1', 1);
insert into personal_feature_type values ('TLFWR', 'String', 'the work phone number', '1', 2);
insert into personal_feature_type values ('MRG', 'String', 'the marriage status', '1', 3);
insert into personal_feature_type values ('CHLP', 'Boolean', 'presence of children', '0', 4);
insert into personal_feature_type values ('RACE', 'String', 'race (optional)', '0', 5);
insert into personal_feature_type values ('TNUM', 'Integer', 'number of teeth (optional)', '0', 6);

-- current (actual) person_feature_values
create table person_feature_value (
   id int primary key,
   person_id int not null,
   feature_code varchar(20) not null,
   feature_value varchar(1000) not null,
   created_date date not null,
   constraint person_feature_value_unique unique (person_id, feature_code),
   foreign key(person_id) references person(person_id),
   foreign key(feature_code) references personal_feature_type(feature_code)
);

create index person_feature_idx on person_feature_value (person_id);

-- historical person_feature_values
create table person_feature_history (
   value_id int not null,
   feature_value varchar(1000) not null,
   created_date date not null,
   changed_date date not null,
   foreign key(value_id) references person_feature_value(id)
);

-- all reference tables must have mandatory fields:
-- changed date not null
-- changedBy varchar(25) not null

-- create all necessary non unique indexes

-- create test data for all included tables

create view vw_person as 
select person_id, first_name, second_name, dp.department_name, ps.position_name from person p
join department dp on dp.department_code = p.department
join position ps on ps.position_code = p.position_code and ps.position_suffix = p.position_suffix;
