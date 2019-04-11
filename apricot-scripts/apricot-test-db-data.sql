-- reference religion
insert into religion values ('CHRIST PROT', 'Protestantism');
insert into religion values ('CHRIST CATH', 'Catolicismul este un sistem doctrinar exprimat');
insert into religion values ('BUDDHISM', 'Buddhism is the worlds fourth-largest religion');
insert into religion values ('ATHEISM', 'We believe in nothing, Lebowsky! Nothing!');
---------------------

-- reference department
insert into department values ('C0001', 'IT', 200, 'no comments');
insert into department values ('CB004', 'Bookkeepers', 30, 'working in the down town');
insert into department values ('CS008', 'Service Department', 45, 'have special privileges');
insert into department values ('CS105', 'Security', 20, 'working in shifts');
insert into department values ('CP200', 'Production', 35, 'occupy the management building');
insert into department values ('CETD0', 'Executives', 3, 'having enormously high salaries');
---------------------

-- reference payment_group
insert into payment_group values (10, 'Monthly Remuneration', 'that is payed monthly', 0, 1);
insert into payment_group values (20, 'Special Occasions', 'something which happens only seldom and irregularly', 1, 0);
insert into payment_group values (30, 'Bonus Group', 'bonuses related payments', 0, 0);
---------------------

-- reference position
insert into position values ('MNGR', 'S10', 'senior managers', 'test comment 1');
insert into position values ('MNGR', 'MD000', 'field managers', 'test comment 2');
insert into position values ('STDEV', 'G010', 'senior developers', 'test comment 3');
insert into position values ('STDEV', 'G020', 'enterprise architects', 'test comment 4');
insert into position values ('ANALYST', 'B100', 'business analysts', 'test comment 5');
insert into position values ('ANALYST', 'T001', 'technical analysts', 'test comment 6');
insert into position values ('ANALYST', 'E000', 'senior enterprise analysts', 'test comment 7');
insert into position values ('ANALYST', 'E010', 'enterprise analysts', 'test comment 8');
insert into position values ('SADM', 'D000', 'sys admins (default range)', 'test comment 9');
---------------------

-- person - the key entity

-- reference payment transaction type
insert into payment_transaction_type values (0, 'M. REMUNERATION', 'MONTHLY');
insert into payment_transaction_type values (1, 'INTEREST', 'DAILY');
insert into payment_transaction_type values (2, 'Y BONUS', 'YEARLY');
---------------------

-- payment_code
insert into payment_code values ('HRATE', 'hourly rate');
insert into payment_code values ('MNDDED', 'mandatory deduction');
insert into payment_code values ('STAX', 'social taxation');
insert into payment_code values ('INTRST', 'monthly interest');
insert into payment_code values ('PREVDED', 'deductions of the previous period');
---------------------

-- associate: pcode_in_payment_group
insert into personal_feature_type values ('TLFHM', 'String', 'the home phone number', 1, 1);
insert into personal_feature_type values ('TLFWR', 'String', 'the work phone number', 1, 2);
insert into personal_feature_type values ('MRG', 'String', 'the marriage status', 1, 3);
insert into personal_feature_type values ('CHLP', 'Boolean', 'presence of children', 0, 4);
insert into personal_feature_type values ('RACE', 'String', 'race (optional)', 0, 5);
insert into personal_feature_type values ('TNUM', 'Integer', 'number of teeth (optional)', 0, 6);
---------------------

-- person
insert into person values (0, '00870456', 'Deon', 'Draggeinm', 'CHRIST CATH', 'CB004', null, 'MNGR', 'S10');
insert into person values (1, '00900156', 'Ron', 'Phyfer', 'ATHEISM', 'CS105', 0, 'MNGR', 'S10');
insert into person values (2, '00900158', 'Leon', 'Nylan', 'BUDDHISM', 'CS105', 0, 'ANALYST', 'T001');
insert into person values (3, '00900160', 'Mike', 'Strydom', 'ATHEISM', 'CETD0', 1, 'STDEV', 'G020');
insert into person values (4, '00900162', 'Stas', 'Sivov', 'BUDDHISM', 'CETD0', null, 'MNGR', 'S10');
---------------------

-- pcode_in_payment_group
insert into pcode_in_payment_group values ('HRATE', 10);
insert into pcode_in_payment_group values ('MNDDED', 20);
insert into pcode_in_payment_group values ('PREVDED', 20);
---------------------

-- person_in_payment_group
insert into person_in_payment_group values (0, 10, true);
insert into person_in_payment_group values (1, 10, true);
insert into person_in_payment_group values (2, 20, false);
insert into person_in_payment_group values (3, 30, false);
insert into person_in_payment_group values (4, 30, false);
---------------------

-- payment_transaction
insert into payment_transaction values (0, 'JAN', '2019-01-01', '2019-01-01', 520000, true, 0);
insert into payment_transaction values (1, 'FEB', '2019-02-18', '2019-02-28', 17897.95, true, 1);
insert into payment_transaction values (3, 'MAR', '2019-03-04', '2019-03-12', 32000.04, true, 2);
---------------------

-- person_feature_value
insert into person_feature_value values (0, 0, 'TLFWR', '90295', '2019-04-11');
---------------------

-- payroll
insert into payroll values (0, 0, 0, 'STAX', 'MAR', 21445.00);
---------------------

-- person_feature_history
insert into person_feature_history values (0, '23555', '2019-04-11', '2019-04-11');
insert into person_feature_history values (0, '23553', '2019-04-11', '2019-04-11');
---------------------
