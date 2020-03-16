-- ****************************************************
-- *                  business_event                  *
-- ****************************************************
SELECT number_rel AS bbb FROM quirell WHERE q='314';

create table business_event (
   business_event_id                    bigint not null,
   amount_in_cents                      bigint not null,
   fee_amount_in_cents                  bigint not null,
   authorized_by                        varchar(20),
   authorized_timestamp                 datetime,
   business_event_type                  int not null,
   transaction_date                     date not null,
   source_system                        varchar(255) not null,
   source_system_reference              varchar(30) not null,
   originating_source_system_reference  varchar(30),
   intermediary_code                    varchar(8),
   source_intermediary_code             varchar(8),
   transaction_category_id              bigint not null,
   fee_transaction_category_id          bigint,
   cost_center                          varchar(6),
   capturer_cost_center                 varchar(6),
   capture_timestamp                    datetime not null,
   captured_by                          varchar(20) not null,
   tax_fund                             varchar(1),
   product_code                         varchar(4),
   bonus_type                           varchar(1),
   kbr_indicator                        varchar(1),
   n_fund_split                         varchar(1),
   plan_number                          varchar(50),
   event_effective_date                 date,
   origin                               varchar(3),
   product_provider                     varchar(2),
   currency                             varchar(1),
   commission_movement_type             varchar(3),
   plan_country                         varchar(1),
   vat_amount_in_cents                  bigint,
   vat_applicable_indicator             bit,
   retroactive_vat_indicator            bit,
   event_status                         varchar(10) not null,
   discriminator                        varchar(30),
   calculated_amount_in_cents_exclusive bigint,
   calculated_vat_amount_in_cents       bigint,
   calculated_vat_applicable_indicator  bit,
   cut_off_period                       date not null,
   cheque_number                        varchar(7),
   loan_number                          varchar(2),
   batch_number                         bigint,
   analysis_code                        varchar(20),
   constraint PK__business__B6F2C4CE89731523 primary key (business_event_id)
);

alter table business_event
add constraint business_event_ss_ssr_bn_uidx1 unique (source_system, source_system_reference, batch_number);

create index be_discriminator_idx on business_event(discriminator);


-- ****************************************************
-- *               business_transaction               *
-- ****************************************************
create table business_transaction (
   business_transaction_id                     bigint not null,
   transaction_number                          varchar(20),
   capturer_cost_center                        varchar(10),
   capturer_id                                 varchar(20),
   intermediary_code                           varchar(8),
   transaction_date                            date,
   transaction_type                            varchar(20),
   financing_type                              varchar(20),
   advisor_reason                              varchar(20),
   payment_date                                date,
   status                                      varchar(15),
   primary_transaction_amount_in_cents         bigint,
   bluestar_financed_amount_in_cents           bigint,
   exceptions_amount_in_cents                  bigint,
   payment_schedule_type                       varchar(50),
   ledger_code                                 varchar(3) not null,
   business_transaction_type                   varchar(50),
   number_rejections_applied                   int,
   total_rejection_amount_in_cents_applied     bigint,
   number_rejections_not_applied               int,
   total_rejection_amount_in_cents_not_applied bigint,
   opening_balance_in_cents                    bigint,
   closing_balance_in_cents                    bigint,
   report_id                                   bigint,
   administration_area_code                    varchar(10),
   cut_off_period                              date not null,
   run_date                                    date,
   cheque_number                               varchar(7),
   transmission_number                         bigint,
   user_generation_number                      bigint,
   funds_transfer_id                           bigint,
   schedule_id                                 bigint,
   created_timestamp                           datetime,
   batch_number                                bigint,
   constraint PK__business__EEEA3E4233625CB7 primary key (business_transaction_id)
);

alter table business_transaction
   add constraint FK_business_transaction_administration_area
   foreign key (administration_area_code) references administration_area (administration_area_code);

alter table business_transaction
   add constraint FK_business_transaction_funds_transfer
   foreign key (funds_transfer_id) references funds_transfer (funds_transfer_id);

alter table business_transaction
   add constraint FK_business_transaction_payment_schedule
   foreign key (schedule_id) references payment_schedule (schedule_id);

alter table business_transaction
   add constraint FK_business_transaction_report
   foreign key (report_id) references report (report_id);


-- ****************************************************
-- *               financial_transaction              *
-- ****************************************************
create table financial_transaction (
   financial_transaction_id bigint not null,
   timestamp                datetime not null,
   description              varchar(255) not null,
   sundry                   varchar(30) not null,
   transaction_date         date not null,
   run_date                 date,
   transaction_status       varchar(30) not null,
   gl_book_month_date       date not null,
   ledger_code              varchar(3),
   cut_off_date             date not null,
   business_event_id        bigint,
   transaction_type         varchar(15) not null,
   transaction_category_id  bigint not null,
   program_reference        varchar(3),
   constraint PK__financia__49DE0486F589E0B8 primary key (financial_transaction_id)
);

create index IDXfinancial_transaction on financial_transaction(financial_transaction_id, run_date);

create index ft_transaction_date_idx on financial_transaction(transaction_date);

create index ft_business_event_idx on financial_transaction(business_event_id);

alter table financial_transaction
   add constraint FK_business_event_financial_transaction
   foreign key (business_event_id) references business_event (business_event_id);


-- ****************************************************
-- *              business_account_entry              *
-- ****************************************************
create table business_account_entry (
   business_account_entry_id          bigint not null,
   business_account_id                bigint not null,
   business_transaction_id            bigint,
   financial_transaction_id           bigint,
   business_account_entry_type        varchar(20) not null,
   cut_off_period                     date not null,
   transaction_date                   date not null,
   amount_in_cents                    bigint not null,
   interest_rate                      decimal,
   business_account_entry_prev_status varchar(20),
   business_account_entry_status      varchar(20) not null,
   status_change_date                 date,
   created_date                       datetime not null,
   transaction_number                 varchar(30),
   payment_method                     varchar(20),
   run_date                           date,
   transaction_number_change_type     varchar(30),
   business_account_entry_prev_type   varchar(20),
   type_change_date                   datetime,
   lock_version                       bigint,
   constraint PK__business__4BC580A7F8036AE8 primary key (business_account_entry_id)
);

create index NonClustered_BussAccId_BussAccEntryType_TransactionDate_Index on business_account_entry(business_account_id, business_account_entry_type, transaction_date);

alter table business_account_entry
   add constraint FK_business_account_entry_business_account
   foreign key (business_account_id) references business_account (business_account_id);

alter table business_account_entry
   add constraint FK_business_account_entry_business_transaction
   foreign key (business_transaction_id) references business_transaction (business_transaction_id);

alter table business_account_entry
   add constraint FK_business_account_entry_financial_transaction
   foreign key (financial_transaction_id) references financial_transaction (financial_transaction_id);


-- ****************************************************
-- *          business_account_entry_payment          *
-- ****************************************************
create table business_account_entry_payment (
   business_account_entry_payment_id bigint not null,
   financial_transaction_id          bigint not null,
   business_account_entry_id         bigint not null,
   amount_in_cents                   bigint not null,
   status                            varchar(20) not null,
   status_change_timestamp           datetime,
   constraint PK__business__A35FAC2011618D2B primary key (business_account_entry_payment_id)
);

alter table business_account_entry_payment
   add constraint FK_busaccount_entry_payment_busaccount
   foreign key (business_account_entry_id) references business_account_entry (business_account_entry_id);

alter table business_account_entry_payment
   add constraint FK_busaccount_entry_payment_financial_transaction
   foreign key (financial_transaction_id) references financial_transaction (financial_transaction_id);


-- ****************************************************
-- *           business_transaction_comment           *
-- ****************************************************
create table business_transaction_comment (
   comment_id              bigint not null,
   business_transaction_id bigint not null,
   constraint PK__business__E79576876D53E9C6 primary key (comment_id)
);

alter table business_transaction_comment
   add constraint FK_business_transaction_comment
   foreign key (business_transaction_id) references business_transaction (business_transaction_id);


