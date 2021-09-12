-- ****************************************************
-- *               tmp_intermediary_view              *
-- ****************************************************
create table tmp_intermediary_view (
   intermediary_code                  varchar(8) not null,
   intermediary_name                  varchar(511),
   branch_code                        varchar(2),
   region_code                        varchar(3),
   cost_center                        varchar(6),
   cell_phone_number                  varchar(14),
   redirect_balance_type              varchar(10),
   redirect_balance_intermediary_code varchar(8),
   entity_intermediary_code           varchar(8),
   language_preference                varchar(3),
   constraint PK__tmp_inte__EAEE9AEEAE753453 primary key (intermediary_code)
);

alter table tmp_intermediary_view
add constraint tmp_intermediary_view_unique_idx unique (intermediary_code);

create index tmp_intermediary_view_cost_center_idx on tmp_intermediary_view(intermediary_code, cost_center);


-- ****************************************************
-- *                  process_control                 *
-- ****************************************************
create table process_control (
   process_name         varchar(100) not null,
   process_id           bigint,
   status               varchar(30),
   last_process_date    datetime,
   restartable          bit not null,
   lock_version         bigint,
   process_batch_number bigint,
   constraint PK__process___A18241BD302CF136 primary key (process_name)
);


-- ****************************************************
-- *                  funds_transfer                  *
-- ****************************************************
create table funds_transfer (
   funds_transfer_id                        bigint not null,
   funds_transfer_entry_class               bigint not null,
   bankserv_user_code                       varchar(4) not null,
   bankserv_user_name                       varchar(30) not null,
   sanpay_bank_branch_code                  varchar(6) not null,
   sanpay_bank_account_number               varchar(11) not null,
   sanpay_short_name                        varchar(10) not null,
   value_for_reporting_in_cents             bigint not null,
   single_transaction_amount_in_cents_limit bigint not null,
   created_by                               varchar(40),
   created_date                             datetime,
   ledger_code                              varchar(3) not null,
   constraint PK__funds_tr__2EBB5FA04F3CB23A primary key (funds_transfer_id)
);


-- ****************************************************
-- *                  debt_management                 *
-- ****************************************************
create table debt_management (
   debt_management_id         bigint not null,
   ceded_to_debt_collector    bit not null,
   in_process_of_write_off    bit not null,
   intermediary_code          varchar(8) not null,
   debt_management_category   varchar(25),
   repayment_agreement        bit not null,
   debt_date                  date,
   last_receipt_date          date,
   print_cob                  date,
   created_timestamp          datetime not null,
   debt_management_status     varchar(25) not null,
   end_of_sms_suspend         date,
   migrated_intermediary_code varchar(8),
   constraint PK__debt_man__54C60D73FAA7D2F2 primary key (debt_management_id)
);

create index debt_management_intemediary_code_idx on debt_management(intermediary_code);


-- ****************************************************
-- *             business_event_reference             *
-- ****************************************************
create table business_event_reference (
   business_event_type             int not null,
   business_event_type_description varchar(88) not null,
   business_event_type_category    varchar(50) not null,
   is_manual_transaction_event     bit not null,
   reporting_view_selector         varchar(255),
   gl_account                      varchar(5),
   program_reference               varchar(3),
   transaction_category_id         bigint,
   vat_type_indicator              varchar(20),
   is_vat_inclusive_indicator      bit,
   processing_group                varchar(255),
   business_event_type_reversal    int,
   category_1                      varchar(60),
   category_2                      varchar(60),
   category_3                      varchar(60),
   loan_type                       varchar(25),
   constraint PK__business__F471A37A88B7AAC4 primary key (business_event_type)
);


-- ****************************************************
-- *                 debt_bank_detail                 *
-- ****************************************************
create table debt_bank_detail (
   bank_detail_id      bigint not null,
   bank_account_holder varchar(100) not null,
   bank_name           varchar(50) not null,
   branch_code         varchar(20),
   account_number      varchar(30) not null,
   account_type        varchar(50),
   constraint PK__debt_ban__7AD4DCABA27CEC07 primary key (bank_detail_id)
);


-- ****************************************************
-- *                      report                      *
-- ****************************************************
create table report (
   report_id   bigint not null,
   report_name varchar(50) not null,
   report_date date not null,
   constraint PK__report__779B7C586A1978E4 primary key (report_id)
);


-- ****************************************************
-- *            gl_rejection_journal_entry            *
-- ****************************************************
create table gl_rejection_journal_entry (
   gl_rejection_journal_entry_id bigint not null,
   transaction_date              date not null,
   company                       varchar(4) not null,
   account                       varchar(5) not null,
   segment3                      varchar(6),
   cost_center                   varchar(6),
   amount                        bigint not null,
   sign                          varchar(1) not null,
   analysis                      varchar(20),
   plan_number                   varchar(12),
   cheque_number                 varchar(7),
   reconciliation_indicator      varchar(1),
   diverse                       varchar(10),
   n_fund                        varchar(1),
   open1                         varchar(7),
   description                   varchar(88),
   error_codes                   varchar(30),
   gl_rejection_journal_id       bigint not null,
   constraint PK__gl_rejec__454D8E4860E8FBAA primary key (gl_rejection_journal_entry_id)
);


-- ****************************************************
-- *                administration_area               *
-- ****************************************************
create table administration_area (
   administration_area_code   varchar(10) not null,
   supports_auto_increase     bit not null,
   approval_group_name        varchar(10),
   funds_transfer_entry_class int,
   administration_area_name   varchar(20),
   constraint PK__administ__67CD88265AFC5B80 primary key (administration_area_code)
);


-- ****************************************************
-- *                 company_reference                *
-- ****************************************************
create table company_reference (
   gl_company_code        varchar(4) not null,
   gl_company_description varchar(255) not null,
   ledger_code            varchar(3),
   fixed_gl_company_code  varchar(4),
   constraint PK__company___C7E7CA00738A1B14 primary key (gl_company_code)
);


-- ****************************************************
-- *            gl_rejection_posting_header           *
-- ****************************************************
create table gl_rejection_posting_header (
   gl_rejection_posting_header_id bigint not null,
   parameter_identification       varchar(5) not null,
   program_id                     varchar(6) not null,
   record_type                    varchar(1) not null,
   version                        varchar(2) not null,
   force_indicator                varchar(5),
   contra_indicator               varchar(1),
   print_indicator                varchar(1),
   post_indicator                 varchar(1) not null,
   jdl                            varchar(6),
   print_minimum                  varchar(1),
   edm_amount                     varchar(16),
   edm_sign                       varchar(1),
   filler1                        varchar(22),
   audit_file                     varchar(3) not null,
   audit_id                       varchar(10) not null,
   open1                          varchar(21),
   filler2                        varchar(10),
   open2                          varchar(113),
   constraint PK__gl_rejec__01B1EED321CB5F32 primary key (gl_rejection_posting_header_id)
);


-- ****************************************************
-- *                 gl_posting_header                *
-- ****************************************************
create table gl_posting_header (
   gl_posting_header_id     bigint not null,
   parameter_identification varchar(5) not null,
   program_id               varchar(6) not null,
   record_type              varchar(1) not null,
   version                  varchar(2) not null,
   force_indicator          varchar(5),
   contra_indicator         varchar(1),
   print_indicator          varchar(1),
   post_indicator           varchar(1) not null,
   jdl                      varchar(6),
   print_minimum            varchar(1),
   edm_amount               varchar(16),
   edm_sign                 varchar(1),
   filler1                  varchar(22),
   audit_file               varchar(3) not null,
   audit_id                 varchar(10) not null,
   open1                    varchar(21),
   filler2                  varchar(10),
   open2                    varchar(113),
   constraint PK__gl_posti__710376563A9C7CBC primary key (gl_posting_header_id)
);


-- ****************************************************
-- *                  reference_data                  *
-- ****************************************************
create table reference_data (
   reference_data_code     varchar(20) not null,
   reference_data_type     varchar(50) not null,
   reference_data_value    varchar(200) not null,
   reporting_view_selector varchar(255),
   constraint PK__referenc__84E95A8109B7DE9C primary key (reference_data_code)
);


-- ****************************************************
-- *                 payment_schedule                 *
-- ****************************************************
create table payment_schedule (
   schedule_id           bigint not null,
   status                varchar(20) not null,
   schedule_run_date     date not null,
   schedule_payment_date date not null,
   payment_category      varchar(20) not null,
   payment_schedule_type varchar(50) not null,
   created_by            varchar(12) not null,
   created_timestamp     datetime not null,
   processed_timestamp   datetime,
   process_cut_off_date  date not null,
   amended_by            varchar(12),
   amended_timestamp     datetime,
   payment_cycle         varchar(20) not null,
   constraint PK__payment___C46A8A6F98489326 primary key (schedule_id)
);


-- ****************************************************
-- *               sub_account_reference              *
-- ****************************************************
create table sub_account_reference (
   sub_account_number            varchar(3) not null,
   sub_account_description       varchar(255) not null,
   include_for_rem_balance       bit not null,
   include_for_financing_balance bit not null,
   constraint PK__sub_acco__A2A29D77B5244F79 primary key (sub_account_number)
);


-- ****************************************************
-- *                  acb_error_code                  *
-- ****************************************************
create table acb_error_code (
   absa_error_code     varchar(3) not null,
   acb_error_code      varchar(3) not null,
   acb_error_code_desc varchar(50) not null,
   constraint PK__acb_erro__7AD3D384676C8AD2 primary key (absa_error_code)
);


-- ****************************************************
-- *                     absa_file                    *
-- ****************************************************
create table absa_file (
   absa_file_id         bigint not null,
   absa_file_type       varchar(30) not null,
   absa_file_name       varchar(100) not null,
   absa_file_status     varchar(20) not null,
   absa_file_created_ts datetime not null,
   constraint PK__absa_fil__6C1C6B13CFFB6C60 primary key (absa_file_id)
);


-- ****************************************************
-- *                BATCH_JOB_INSTANCE                *
-- ****************************************************
create table BATCH_JOB_INSTANCE (
   JOB_INSTANCE_ID bigint not null,
   VERSION         bigint,
   JOB_NAME        varchar(100) not null,
   JOB_KEY         varchar(32) not null,
   constraint PK__BATCH_JO__4848154A19B0651D primary key (JOB_INSTANCE_ID)
);

alter table BATCH_JOB_INSTANCE
add constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY);


-- ****************************************************
-- *            debt_collection_instruction           *
-- ****************************************************
create table debt_collection_instruction (
   debt_collection_instruction_id    bigint not null,
   intermediary_code                 varchar(8) not null,
   administration_area_code          varchar(10) not null,
   repayment_method                  varchar(20) not null,
   initial_payment_date              date not null,
   next_payment_date                 date not null,
   next_raise_date                   date not null,
   previous_raise_date               date,
   amount_in_cents                   bigint not null,
   increase_effective_date           date,
   increase_percentage               decimal,
   increase_term                     bigint,
   increase_end_date                 date,
   bank_reference_number             bigint,
   created_by                        varchar(10) not null,
   created_date                      date not null,
   status                            varchar(20),
   suspended_from_date               date,
   changed_by                        varchar(10),
   changed_date                      date,
   number_increases_applied_for_term bigint,
   actual_amount_in_cents_raised     bigint not null,
   constraint PK__debt_col__19434B5D491A2ACD primary key (debt_collection_instruction_id)
);

alter table debt_collection_instruction
   add constraint FK_debt_collection_instruction_administration_area
   foreign key (administration_area_code) references administration_area (administration_area_code);


-- ****************************************************
-- *                    gl_journal                    *
-- ****************************************************
create table gl_journal (
   gl_journal_id        bigint not null,
   program_indicator    varchar(2) not null,
   program_reference    varchar(3) not null,
   counter              varchar(7) not null,
   product              varchar(9),
   transaction_type     varchar(3),
   channel              varchar(1),
   book_month_date      date not null,
   open1                varchar(8),
   posting_status       varchar(20),
   gl_posting_header_id bigint not null,
   constraint PK__gl_journ__B99AA596899BDAD0 primary key (gl_journal_id)
);

create index gj_program_indicator_idx on gl_journal(program_indicator, program_reference, counter);

alter table gl_journal
   add constraint FK_gl_journal_gl_posting_header
   foreign key (gl_posting_header_id) references gl_posting_header (gl_posting_header_id);


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
   constraint PK__business__EEEA3E42ABD3DEF0 primary key (business_transaction_id)
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
-- *                BATCH_JOB_EXECUTION               *
-- ****************************************************
create table BATCH_JOB_EXECUTION (
   JOB_EXECUTION_ID           bigint not null,
   VERSION                    bigint,
   JOB_INSTANCE_ID            bigint not null,
   CREATE_TIME                datetime not null,
   START_TIME                 datetime,
   END_TIME                   datetime,
   STATUS                     varchar(10),
   EXIT_CODE                  varchar(2500),
   EXIT_MESSAGE               varchar(2500),
   LAST_UPDATED               datetime,
   JOB_CONFIGURATION_LOCATION varchar(2500),
   constraint PK__BATCH_JO__56435A77CEBDDE6F primary key (JOB_EXECUTION_ID)
);

alter table BATCH_JOB_EXECUTION
   add constraint JOB_INST_EXEC_FK
   foreign key (JOB_INSTANCE_ID) references BATCH_JOB_INSTANCE (JOB_INSTANCE_ID);


-- ****************************************************
-- *                 payment_rejection                *
-- ****************************************************
create table payment_rejection (
   payment_rejection_id     bigint not null,
   business_transaction_id  bigint not null,
   rd_reason                varchar(3) not null,
   contract_number          varchar(6) not null,
   payment_type             varchar(20) not null,
   processing_status        varchar(100) not null,
   amount_in_cents          bigint not null,
   bank_branch_code         varchar(8) not null,
   bank_account_number      varchar(16),
   bank_account_type        int not null,
   reference_code           varchar(30) not null,
   rejection_processed_date datetime not null,
   resolved_by              varchar(7),
   resolved_date            datetime,
   action_date              date,
   account_holder_name      varchar(30),
   rejection_qualifier      varchar(5),
   original_sequence_number int,
   transmission_date        date,
   generation_number        int,
   payment_detail_id        bigint,
   transaction_number       varchar(30),
   constraint PK__payment___40FCA06605152A49 primary key (payment_rejection_id)
);

alter table payment_rejection
   add constraint FK_payment_rejection_absa_error_code
   foreign key (rd_reason) references acb_error_code (absa_error_code);

alter table payment_rejection
   add constraint FK_payment_rejection_business_transaction
   foreign key (business_transaction_id) references business_transaction (business_transaction_id);


-- ****************************************************
-- *                  payment_detail                  *
-- ****************************************************
create table payment_detail (
   payment_detail_id              bigint not null,
   intermediary_code              varchar(8) not null,
   reference_code                 varchar(20) not null,
   business_transaction_id        bigint,
   amount_in_cents                bigint,
   role                           varchar(20),
   account_holder_name            varchar(30),
   bank_name                      varchar(30),
   bank_branch_code               varchar(6),
   bank_account_type              varchar(50),
   bank_account_number            varchar(31),
   admin_fee_in_cents             bigint,
   status                         varchar(20),
   payment_detail_type            varchar(20),
   debt_collection_instruction_id bigint,
   payment_date                   date,
   sequence_number                bigint,
   transaction_number             varchar(30),
   constraint PK__payment___C66E6E36EC1115A9 primary key (payment_detail_id)
);

alter table payment_detail
   add constraint FK_payment_detail_business_transaction
   foreign key (business_transaction_id) references business_transaction (business_transaction_id);

alter table payment_detail
   add constraint FK_payment_detail_debt_collection_instruction
   foreign key (debt_collection_instruction_id) references debt_collection_instruction (debt_collection_instruction_id);


-- ****************************************************
-- *                 gl_journal_entry                 *
-- ****************************************************
create table gl_journal_entry (
   gl_journal_entry_id             bigint not null,
   transaction_date                date not null,
   company                         varchar(4) not null,
   account                         varchar(5) not null,
   segment3                        varchar(6),
   cost_center                     varchar(6),
   amount                          bigint not null,
   sign                            varchar(1) not null,
   analysis                        varchar(20),
   plan_number                     varchar(12),
   cheque_number                   varchar(7),
   reconciliation_indicator        varchar(1),
   diverse                         varchar(10),
   n_fund                          varchar(1),
   open1                           varchar(7),
   description                     varchar(88),
   gl_journal_id                   bigint not null,
   gl_rejection_journal_entry_id   bigint,
   originating_gl_journal_entry_id bigint,
   constraint PK__gl_journ__143814B328A70910 primary key (gl_journal_entry_id)
);

create index gje_transaction_date_idx on gl_journal_entry(transaction_date);

create index gje_gl_journal_idx on gl_journal_entry(gl_journal_id);

alter table gl_journal_entry
   add constraint FK_gl_journal_entry_gl_journal
   foreign key (gl_journal_id) references gl_journal (gl_journal_id);

alter table gl_journal_entry
   add constraint FK_gl_journal_entry_gl_rejection_journal_entry
   foreign key (gl_rejection_journal_entry_id) references gl_rejection_journal_entry (gl_rejection_journal_entry_id);


-- ****************************************************
-- *               BATCH_STEP_EXECUTION               *
-- ****************************************************
create table BATCH_STEP_EXECUTION (
   STEP_EXECUTION_ID  bigint not null,
   VERSION            bigint not null,
   STEP_NAME          varchar(100) not null,
   JOB_EXECUTION_ID   bigint not null,
   START_TIME         datetime not null,
   END_TIME           datetime,
   STATUS             varchar(10),
   COMMIT_COUNT       bigint,
   READ_COUNT         bigint,
   FILTER_COUNT       bigint,
   WRITE_COUNT        bigint,
   READ_SKIP_COUNT    bigint,
   WRITE_SKIP_COUNT   bigint,
   PROCESS_SKIP_COUNT bigint,
   ROLLBACK_COUNT     bigint,
   EXIT_CODE          varchar(2500),
   EXIT_MESSAGE       varchar(2500),
   LAST_UPDATED       datetime,
   constraint PK__BATCH_ST__60B8C8A58E1BC71A primary key (STEP_EXECUTION_ID)
);

alter table BATCH_STEP_EXECUTION
   add constraint JOB_EXEC_STEP_FK
   foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID);


-- ****************************************************
-- *                 payment_exception                *
-- ****************************************************
create table payment_exception (
   payment_exception_id                    bigint not null,
   payment_exception_payment_schedule_type varchar(30) not null,
   payment_exception_payment_type          varchar(255) not null,
   payment_exception_payment_cycle         varchar(255) not null,
   payment_exception_payment_date          datetime,
   payment_exception_payment_amount        bigint not null,
   payment_exception_reference_code        varchar(30),
   payment_exception_reason                varchar(255) not null,
   payment_exception_source_type           varchar(30) not null,
   business_transaction_id                 bigint,
   payment_detail_id                       bigint,
   payment_exception_source_name           varchar(50),
   payment_exception_fixed                 bit not null,
   fixed_by                                varchar(255),
   fixed_date                              datetime,
   exception_type                          varchar(30) not null,
   payment_rejection_id                    bigint,
   payment_exception_status                varchar(5),
   allocated_consultant                    varchar(7),
   allocated_date                          date,
   debt_collection_instruction_id          bigint,
   intermediary_code                       varchar(8),
   constraint PK__payment___06FB8D7F6D757735 primary key (payment_exception_id)
);

alter table payment_exception
   add constraint FK_payment_exception_bustran_id
   foreign key (business_transaction_id) references business_transaction (business_transaction_id);

alter table payment_exception
   add constraint FK_payment_exception_paydet_id
   foreign key (payment_detail_id) references payment_detail (payment_detail_id);

alter table payment_exception
   add constraint FK_payment_exception_rejection_id
   foreign key (payment_rejection_id) references payment_rejection (payment_rejection_id);


-- ****************************************************
-- *                payment_adjustment                *
-- ****************************************************
create table payment_adjustment (
   payment_adjustment_id      bigint not null,
   intermediary_code          varchar(8) not null,
   cut_off_date               date not null,
   finance_balance            bigint not null,
   current_transfer_amount    bigint not null,
   new_transfer_amount        bigint not null,
   adjustment                 bigint not null,
   payment_adjustmnent_reason varchar(30) not null,
   captured_by                varchar(10) not null,
   captured_timestamp         datetime not null,
   authorized_by              varchar(10),
   authorized_timestamp       datetime,
   adjustment_checked         bit not null,
   adjustment_status          varchar(10) not null,
   payment_detail_id          bigint,
   constraint PK__payment___716199E36A50DC91 primary key (payment_adjustment_id)
);

create index payment_adjustment_intermediary_idx on payment_adjustment(intermediary_code);

alter table payment_adjustment
   add constraint FK_payment_adj_payment_det
   foreign key (payment_detail_id) references payment_detail (payment_detail_id);


-- ****************************************************
-- *                  debt_collector                  *
-- ****************************************************
create table debt_collector (
   debt_collector_id     varchar(15) not null,
   debt_collector_name   varchar(100) not null,
   debt_collector_status varchar(25) not null,
   bank_detail_id        bigint,
   debt_collector_type   varchar(50),
   fax_number            varchar(50),
   email_address         varchar(50),
   constraint PK__debt_col__0E1F03F5263FD4B4 primary key (debt_collector_id)
);

alter table debt_collector
   add constraint FK_debt_collector_bank_detail
   foreign key (bank_detail_id) references debt_bank_detail (bank_detail_id);


-- ****************************************************
-- *          tmp_intermediary_agreement_view         *
-- ****************************************************
create table tmp_intermediary_agreement_view (
   intermediary_code            varchar(8) not null,
   contract_specification_id    bigint not null,
   termination_date             date,
   termination_transaction_date date,
   open_for_financing           bit,
   status                       varchar(10),
   intermediary_type            varchar(7) not null,
   payment_schedule_type        varchar(50),
   debt_written_off             bit,
   transfer_to_gl_cost_center   varchar(6),
   transfer_to_gl_account       varchar(5),
   contract_code                varchar(50),
   commencement_date            date,
   job_title                    varchar(30),
   owner_agreement              bit,
   constraint PK__tmp_inte__EAEE9AEED15B3A61 primary key (intermediary_code)
);

alter table tmp_intermediary_agreement_view
add constraint tmp_intermediary_agreement_view_unique_idx unique (intermediary_code);

alter table tmp_intermediary_agreement_view
   add constraint FK_tmp_intermediary_agreement_view_tmp_intermediary_view
   foreign key (intermediary_code) references tmp_intermediary_view (intermediary_code);


-- ****************************************************
-- *                   report_sheet                   *
-- ****************************************************
create table report_sheet (
   report_sheet_id   bigint not null,
   report_sheet_name varchar(50),
   report_id         bigint not null,
   last_row_num      int not null,
   constraint PK__report_s__5CA9A249CE53BA2B primary key (report_sheet_id)
);

alter table report_sheet
   add constraint FK_report_sheet_report
   foreign key (report_id) references report (report_id);


-- ****************************************************
-- *          terminated_intermediary_savings         *
-- ****************************************************
create table terminated_intermediary_savings (
   movement_id             bigint not null,
   intermediary_code       varchar(8) not null,
   amount_in_cents         bigint not null,
   business_transaction_id bigint not null,
   constraint PK__terminat__AB1D1022F04D59D2 primary key (movement_id)
);

alter table terminated_intermediary_savings
   add constraint FK_terminated_intermediary_savings_business_transaction
   foreign key (business_transaction_id) references business_transaction (business_transaction_id);


-- ****************************************************
-- *                  debt_write_off                  *
-- ****************************************************
create table debt_write_off (
   write_off_id                bigint not null,
   write_off_type              varchar(25) not null,
   write_off_status            varchar(25) not null,
   debt_management_id          bigint not null,
   created_timestamp           datetime not null,
   modified_timestamp          datetime,
   start_date                  date,
   end_date                    date,
   created_by                  varchar(50),
   changed_by                  varchar(50),
   written_off_amount_in_cents bigint,
   write_off_date              date,
   constraint PK__debt_wri__2177614451B1CBEC primary key (write_off_id)
);

alter table debt_write_off
   add constraint FK_debt_management_write_off
   foreign key (debt_management_id) references debt_management (debt_management_id);


-- ****************************************************
-- *              debt_follow_up_history              *
-- ****************************************************
create table debt_follow_up_history (
   follow_up_id       bigint not null,
   follow_up_date     date,
   follow_up_type     varchar(30) not null,
   follow_up_status   varchar(30) not null,
   created_timestamp  datetime not null,
   updated_timestamp  datetime,
   debt_management_id bigint not null,
   start_date         date,
   end_date           date,
   created_by         varchar(50),
   changed_by         varchar(50),
   ccma_indication    bit not null,
   constraint PK__debt_fol__53C081442743EF7A primary key (follow_up_id)
);

alter table debt_follow_up_history
   add constraint FK_debt_management_follow_up_history
   foreign key (debt_management_id) references debt_management (debt_management_id);


-- ****************************************************
-- *            BATCH_JOB_EXECUTION_CONTEXT           *
-- ****************************************************
create table BATCH_JOB_EXECUTION_CONTEXT (
   JOB_EXECUTION_ID   bigint not null,
   SHORT_CONTEXT      varchar(2500) not null,
   SERIALIZED_CONTEXT text,
   constraint PK__BATCH_JO__56435A77A00ADE52 primary key (JOB_EXECUTION_ID)
);

alter table BATCH_JOB_EXECUTION_CONTEXT
   add constraint JOB_EXEC_CTX_FK
   foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID);


-- ****************************************************
-- *           business_transaction_comment           *
-- ****************************************************
create table business_transaction_comment (
   comment_id              bigint not null,
   business_transaction_id bigint not null,
   constraint PK__business__E7957687217EC760 primary key (comment_id)
);

alter table business_transaction_comment
   add constraint FK_business_transaction_comment
   foreign key (business_transaction_id) references business_transaction (business_transaction_id);


-- ****************************************************
-- *      tmp_intermediary_vat_registration_view      *
-- ****************************************************
create table tmp_intermediary_vat_registration_view (
   intermediary_code varchar(8) not null,
   effective_date    date not null,
   vat_registered    bit,
   constraint PK_vat_registration_view primary key (intermediary_code, effective_date)
);

alter table tmp_intermediary_vat_registration_view
   add constraint FK_registration_tmp_intermediary
   foreign key (intermediary_code) references tmp_intermediary_view (intermediary_code);


-- ****************************************************
-- *        debt_collection_instruction_comment       *
-- ****************************************************
create table debt_collection_instruction_comment (
   comment_id                     bigint not null,
   debt_collection_instruction_id bigint not null,
   constraint PK__debt_col__E79576876C3BE311 primary key (comment_id)
);

alter table debt_collection_instruction_comment
   add constraint FK_debt_collection_instruction_comment
   foreign key (debt_collection_instruction_id) references debt_collection_instruction (debt_collection_instruction_id);


-- ****************************************************
-- *             gl_journal_entry_comment             *
-- ****************************************************
create table gl_journal_entry_comment (
   comment_id          bigint not null,
   gl_journal_entry_id bigint not null,
   constraint PK__gl_journ__E7957687077DFF7F primary key (comment_id)
);

alter table gl_journal_entry_comment
   add constraint FK_gl_journal_entry_comment
   foreign key (gl_journal_entry_id) references gl_journal_entry (gl_journal_entry_id);


-- ****************************************************
-- *        tmp_intermediary_bank_account_view        *
-- ****************************************************
create table tmp_intermediary_bank_account_view (
   bank_account_id        bigint not null,
   intermediary_code      varchar(8) not null,
   bank_account_link_type varchar(255),
   account_holder_name    varchar(50) not null,
   account_number         varchar(50) not null,
   account_type           varchar(50),
   branch_code            varchar(50) not null,
   bank_name              varchar(255) not null,
   external_reference     varchar(255),
   constraint PK__tmp_inte__BDFDB499814CA34A primary key (bank_account_id)
);

alter table tmp_intermediary_bank_account_view
   add constraint FK_tmp_intermediary_bank_account_view_tmp_intermediary_view
   foreign key (intermediary_code) references tmp_intermediary_view (intermediary_code);


-- ****************************************************
-- *                 absa_file_record                 *
-- ****************************************************
create table absa_file_record (
   absa_file_record_id                  bigint not null,
   absa_file_id                         bigint not null,
   record_type                          varchar(30) not null,
   record_identifier                    varchar(3) not null,
   record_status                        varchar(1) not null,
   user_set_number                      int,
   bankserv_record_identifier           char(2),
   first_sequence_number                bigint,
   last_sequence_number                 bigint,
   first_action_date                    date,
   last_action_date                     date,
   service_indicator                    varchar(3),
   bankserv_user_code                   varchar(4),
   error_code                           varchar(5),
   error_message                        varchar(100),
   transmission_date                    date,
   electronic_banking_suite             varchar(5),
   electronic_banking_suite_name        varchar(30),
   transmission_number                  bigint,
   destination                          varchar(5),
   bankserv_creation_date               date,
   bankserv_purge_date                  date,
   generation_number                    int,
   type_of_service                      varchar(10),
   accepted_report                      varchar(1),
   account_type_correct                 varchar(1),
   user_branch                          varchar(6),
   user_nominated_account               varchar(11),
   sequence_number                      int,
   homing_branch                        varchar(6),
   homing_account_number                varchar(20),
   type_of_account                      varchar(2),
   amount                               bigint,
   action_date                          date,
   entry_class                          char(2),
   tax_code                             char(1),
   cheque_number                        varchar(7),
   sanpay_short_name                    varchar(11),
   homing_account_name                  varchar(30),
   homing_institution                   char(2),
   intermediary_code                    varchar(8),
   payment_detail_id                    bigint,
   ledger_code                          varchar(3),
   no_debit_records                     int,
   no_credit_records                    int,
   no_contra_records                    int,
   total_debit_value                    bigint,
   total_credit_value                   bigint,
   hash_total_of_homing_account_numbers bigint,
   number_of_records_in_transmission    int,
   transmission_identifier              varchar(3),
   transmission_status                  varchar(8),
   user_set_status                      varchar(8),
   user_sequance_number                 bigint,
   header_record_indicator              varchar(3),
   sanpay_bank_branch_code              varchar(6),
   sanpay_bank_account_number           varchar(20),
   transaction_type                     varchar(2),
   absa_error_code                      varchar(3),
   rejection_qualifier_code             varchar(5),
   absa_sequence                        bigint,
   number_of_debit_records              bigint,
   number_of_credit_records             bigint,
   homing_account_hash_total            bigint,
   debit_amount_hash_total              bigint,
   credit_amount_hash_total             bigint,
   original_transmission_date           date,
   constraint PK__absa_fil__D95D7AAD6020CA3B primary key (absa_file_record_id)
);

alter table absa_file_record
   add constraint FK_absa_file_to_absa_file_record
   foreign key (absa_file_id) references absa_file (absa_file_id);


-- ****************************************************
-- *            BATCH_JOB_EXECUTION_PARAMS            *
-- ****************************************************
create table BATCH_JOB_EXECUTION_PARAMS (
   JOB_EXECUTION_ID bigint not null,
   TYPE_CD          varchar(6) not null,
   KEY_NAME         varchar(100) not null,
   STRING_VAL       varchar(250),
   DATE_VAL         datetime,
   LONG_VAL         bigint,
   DOUBLE_VAL       float,
   IDENTIFYING      char(1) not null);

alter table BATCH_JOB_EXECUTION_PARAMS
   add constraint JOB_EXEC_PARAMS_FK
   foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID);


-- ****************************************************
-- *              reference_data_category             *
-- ****************************************************
create table reference_data_category (
   reference_data_category_id bigint not null,
   owner_code                 varchar(20) not null,
   member_code                varchar(20) not null,
   constraint PK__referenc__8B1823E864CFD4CD primary key (reference_data_category_id)
);

alter table reference_data_category
add constraint reference_data_category_unique_constraint unique (owner_code, member_code);

alter table reference_data_category
   add constraint FK_reference_data_category_member
   foreign key (member_code) references reference_data (reference_data_code);

alter table reference_data_category
   add constraint FK_reference_data_category_owner
   foreign key (owner_code) references reference_data (reference_data_code);


-- ****************************************************
-- *                debt_cession_detail               *
-- ****************************************************
create table debt_cession_detail (
   cession_detail_id        bigint not null,
   cession_status           varchar(25) not null,
   ceded_date               date,
   ceded_back_date          date,
   ceded_back_reason        varchar(30),
   debt_management_id       bigint not null,
   created_timestamp        datetime not null,
   ceded_by                 varchar(50),
   ceded_back_by            varchar(50),
   debt_collector_id        varchar(15) not null,
   debt_collector_reference varchar(50),
   constraint PK__debt_ces__FBAECB50E59D5E3B primary key (cession_detail_id)
);

create index debt_cession_detail_debt_detail_vw_idx on debt_cession_detail(cession_status, debt_management_id, created_timestamp);

alter table debt_cession_detail
   add constraint FK_debt_cession_detail_debt_collector
   foreign key (debt_collector_id) references debt_collector (debt_collector_id);

alter table debt_cession_detail
   add constraint FK_debt_management_cession_detail
   foreign key (debt_management_id) references debt_management (debt_management_id);


-- ****************************************************
-- *                 process_variable                 *
-- ****************************************************
create table process_variable (
   variable_id    bigint not null,
   process_name   varchar(100) not null,
   variable_name  varchar(100) not null,
   variable_value varchar(500) not null,
   constraint PK__process___EDFBCB2E933A65E0 primary key (variable_id)
);

alter table process_variable
   add constraint FK_process_variable_control
   foreign key (process_name) references process_control (process_name);


-- ****************************************************
-- *            payment_adjustment_comment            *
-- ****************************************************
create table payment_adjustment_comment (
   comment_id            bigint not null,
   payment_adjustment_id bigint not null,
   constraint PK__payment___E795768729EC5494 primary key (comment_id)
);

alter table payment_adjustment_comment
   add constraint FK_payment_adjustment_comment
   foreign key (payment_adjustment_id) references payment_adjustment (payment_adjustment_id);


-- ****************************************************
-- *             payment_exception_comment            *
-- ****************************************************
create table payment_exception_comment (
   comment_id           bigint not null,
   payment_exception_id bigint not null,
   constraint PK__payment___E7957687A16A2F85 primary key (comment_id)
);

alter table payment_exception_comment
   add constraint FK_payment_exception_comment
   foreign key (payment_exception_id) references payment_exception (payment_exception_id);


-- ****************************************************
-- *             debt_repayment_agreement             *
-- ****************************************************
create table debt_repayment_agreement (
   repayment_agreement_id       bigint not null,
   repayment_agreement_type     varchar(25) not null,
   repayment_agreement_amount   bigint not null,
   frequency                    varchar(50),
   due_date                     date,
   initial_payment_date         date,
   repayment_agreement_status   varchar(30) not null,
   debt_management_id           bigint not null,
   created_timestamp            datetime not null,
   modified_timestamp           datetime,
   start_date                   date,
   end_date                     date,
   created_by                   varchar(50),
   changed_by                   varchar(50),
   repayment_agreement_honoured bit,
   constraint PK__debt_rep__6509564F9BB23FA9 primary key (repayment_agreement_id)
);

alter table debt_repayment_agreement
   add constraint FK_debt_management_repayment_agreement
   foreign key (debt_management_id) references debt_management (debt_management_id);


-- ****************************************************
-- *        tmp_intermediary_relationship_view        *
-- ****************************************************
create table tmp_intermediary_relationship_view (
   intermediary_code        varchar(8) not null,
   effective_date           date not null,
   entity_intermediary_code varchar(8),
   constraint PK_relationship_view primary key (intermediary_code, effective_date)
);

alter table tmp_intermediary_relationship_view
   add constraint FK_relationship_tmp_entity_intermediary
   foreign key (entity_intermediary_code) references tmp_intermediary_view (intermediary_code);

alter table tmp_intermediary_relationship_view
   add constraint FK_relationship_tmp_intermediary
   foreign key (intermediary_code) references tmp_intermediary_view (intermediary_code);


-- ****************************************************
-- *                 debt_final_demand                *
-- ****************************************************
create table debt_final_demand (
   final_demand_id    bigint not null,
   delivery_date      date,
   extract_date       date,
   debt_management_id bigint not null,
   final_demand       bit not null,
   created_timestamp  datetime,
   constraint PK__debt_fin__1A3DC2731690676F primary key (final_demand_id)
);

create index debt_final_demand_final_demand_idx on debt_final_demand(debt_management_id, final_demand, created_timestamp);

alter table debt_final_demand
   add constraint FK_debt_management_final_demand
   foreign key (debt_management_id) references debt_management (debt_management_id);


-- ****************************************************
-- *              debt_management_comment             *
-- ****************************************************
create table debt_management_comment (
   comment_id         bigint not null,
   debt_management_id bigint not null,
   constraint PK__debt_man__E7957687B4757559 primary key (comment_id)
);

alter table debt_management_comment
   add constraint FK_debt_management_comment
   foreign key (debt_management_id) references debt_management (debt_management_id);


-- ****************************************************
-- *        tmp_intermediary_historic_link_view       *
-- ****************************************************
create table tmp_intermediary_historic_link_view (
   historic_link_id           bigint not null,
   intermediary_code          varchar(8) not null,
   historic_intermediary_code varchar(8),
   constraint PK__tmp_inte__5CAB788D6A150CD4 primary key (historic_link_id)
);

alter table tmp_intermediary_historic_link_view
   add constraint FK_tmp_intermediary_historic_link_view_tmp_intermediary_view
   foreign key (intermediary_code) references tmp_intermediary_view (intermediary_code);


-- ****************************************************
-- *     tmp_intermediary_agreement_component_view    *
-- ****************************************************
create table tmp_intermediary_agreement_component_view (
   intermediary_code varchar(8) not null,
   component_name    varchar(80) not null,
   component_active  bit);

alter table tmp_intermediary_agreement_component_view
add constraint tmp_intermediary_agreement_component_view_unique_idx unique (intermediary_code, component_name);

alter table tmp_intermediary_agreement_component_view
   add constraint FK_tmp_intermediary_agreement_component_view_tmp_intermediary_agreement_view
   foreign key (intermediary_code) references tmp_intermediary_agreement_view (intermediary_code);


-- ****************************************************
-- *               cost_center_reference              *
-- ****************************************************
create table cost_center_reference (
   gl_cost_center             varchar(6) not null,
   gl_cost_center_description varchar(255) not null,
   gl_cost_center_type        varchar(20) not null,
   gl_company_code            varchar(4) not null,
   gl_cost_center_override    varchar(6),
   gl_cost_center_is_active   bit not null,
   gl_cost_center_source_type varchar(20),
   gl_cost_center_insert_date date,
   constraint PK__cost_cen__88EE41B25194A9D7 primary key (gl_cost_center)
);

alter table cost_center_reference
   add constraint FK_cost_center_reference_company_reference
   foreign key (gl_company_code) references company_reference (gl_company_code);


-- ****************************************************
-- *                    report_cell                   *
-- ****************************************************
create table report_cell (
   report_cell_id  bigint not null,
   report_sheet_id bigint not null,
   column_num      bigint not null,
   row_num         bigint not null,
   cell_type       varchar(10) not null,
   cell_style      int not null,
   string_value    varchar(255),
   decimal_value   decimal,
   long_value      bigint,
   date_value      date,
   constraint PK__report_c__6EEC193B92817B06 primary key (report_cell_id)
);

alter table report_cell
   add constraint FK_report_cell_report
   foreign key (report_sheet_id) references report_sheet (report_sheet_id);


-- ****************************************************
-- *        debt_collection_instruction_history       *
-- ****************************************************
create table debt_collection_instruction_history (
   debt_collection_instruction_history_id bigint not null,
   debt_collection_instruction_id         bigint not null,
   intermediary_code                      varchar(8) not null,
   administration_area_code               varchar(10) not null,
   repayment_method                       varchar(20) not null,
   initial_payment_date                   date not null,
   next_payment_date                      date not null,
   next_raise_date                        date not null,
   previous_raise_date                    date,
   amount_in_cents                        bigint not null,
   increase_effective_date                date,
   increase_percentage                    decimal,
   increase_term                          bigint,
   increase_end_date                      date,
   bank_reference_number                  bigint,
   created_by                             varchar(10),
   created_date                           date,
   status                                 varchar(20),
   suspended_from_date                    date,
   changed_by                             varchar(10),
   changed_date                           date,
   inserted_date                          date not null,
   actual_amount_in_cents_raised          bigint not null,
   constraint PK__debt_col__793F5F2502B80BDE primary key (debt_collection_instruction_history_id)
);

alter table debt_collection_instruction_history
   add constraint FK_debt_collection_instruction_history_administration_area
   foreign key (administration_area_code) references administration_area (administration_area_code);

alter table debt_collection_instruction_history
   add constraint FK_debt_collection_instruction_history_debt_collection_instruction
   foreign key (debt_collection_instruction_id) references debt_collection_instruction (debt_collection_instruction_id);


-- ****************************************************
-- *        debt_management_administration_area       *
-- ****************************************************
create table debt_management_administration_area (
   mngmt_admin_area_id      bigint not null,
   mngmt_status             varchar(25),
   debt_management_id       bigint not null,
   administration_area_code varchar(10) not null,
   created_timestamp        datetime not null,
   modified_timestamp       datetime,
   constraint PK__debt_man__9F228B5F421213D3 primary key (mngmt_admin_area_id)
);

alter table debt_management_administration_area
   add constraint FK_adm_area_debt_management
   foreign key (administration_area_code) references administration_area (administration_area_code);

alter table debt_management_administration_area
   add constraint FK_debt_management_adm_area
   foreign key (debt_management_id) references debt_management (debt_management_id);


-- ****************************************************
-- *           BATCH_STEP_EXECUTION_CONTEXT           *
-- ****************************************************
create table BATCH_STEP_EXECUTION_CONTEXT (
   STEP_EXECUTION_ID  bigint not null,
   SHORT_CONTEXT      varchar(2500) not null,
   SERIALIZED_CONTEXT text,
   constraint PK__BATCH_ST__60B8C8A5D1666595 primary key (STEP_EXECUTION_ID)
);

alter table BATCH_STEP_EXECUTION_CONTEXT
   add constraint STEP_EXEC_CTX_FK
   foreign key (STEP_EXECUTION_ID) references BATCH_STEP_EXECUTION (STEP_EXECUTION_ID);


-- ****************************************************
-- *                     approval                     *
-- ****************************************************
create table approval (
   approval_id             bigint not null,
   business_transaction_id bigint not null,
   role                    varchar(20) not null,
   timestamp               datetime not null,
   username                varchar(7) not null,
   status                  varchar(10) not null,
   constraint PK__approval__C94AE61A4CA22692 primary key (approval_id)
);

alter table approval
   add constraint FK_business_transaction_approval
   foreign key (business_transaction_id) references business_transaction (business_transaction_id);


-- ****************************************************
-- *       business_event_type_processing_group       *
-- ****************************************************
create table business_event_type_processing_group (
   business_event_type int not null,
   processing_group    varchar(100) not null);

alter table business_event_type_processing_group
   add constraint FK_bus_event_type_proc_group
   foreign key (business_event_type) references business_event_reference (business_event_type);


-- ****************************************************
-- *                      account                     *
-- ****************************************************
create table account (
   account_id         bigint not null,
   ledger             varchar(3) not null,
   account_number     varchar(5) not null,
   sub_account_number varchar(3),
   intermediary_code  char(8),
   constraint PK__account__46A222CD748367F5 primary key (account_id)
);

alter table account
add constraint account_idx unique (ledger, account_number, sub_account_number, intermediary_code);

create index IDX_DBA_SL0167 on account(ledger, account_number, sub_account_number, intermediary_code);

alter table account
   add constraint FK_account_sub_account
   foreign key (sub_account_number) references sub_account_reference (sub_account_number);


-- ****************************************************
-- *               gl_rejection_journal               *
-- ****************************************************
create table gl_rejection_journal (
   gl_rejection_journal_id        bigint not null,
   program_indicator              varchar(2) not null,
   program_reference              varchar(3) not null,
   counter                        varchar(7) not null,
   product                        varchar(9),
   transaction_type               varchar(3),
   channel                        varchar(1),
   book_month_date                date not null,
   open1                          varchar(8),
   rejection_status               varchar(10) not null,
   gl_rejection_posting_header_id bigint not null,
   constraint PK__gl_rejec__ECEA267ECC198656 primary key (gl_rejection_journal_id)
);

alter table gl_rejection_journal
   add constraint FK_gl_rejection_journal_gl_rejection_posting_header
   foreign key (gl_rejection_posting_header_id) references gl_rejection_posting_header (gl_rejection_posting_header_id);


-- ****************************************************
-- *                  cut_off_balance                 *
-- ****************************************************
create table cut_off_balance (
   cut_off_balance_id                       bigint not null,
   intermediary_code                        varchar(8) not null,
   sub_account_number                       int,
   sub_account_description                  varchar(50),
   cut_off_date                             date,
   cut_off_balance_in_cents                 bigint not null,
   process_cut_off_date                     date,
   debt_management_balance_in_cents         bigint,
   opening_balance_in_cents                 bigint not null,
   created_timestamp                        datetime not null,
   updated_timestamp                        datetime,
   balance_paid_in_cents_this_period        bigint,
   opening_debt_management_balance_in_cents bigint,
   constraint PK__cut_off___8B25BD2008D2827D primary key (cut_off_balance_id)
);

alter table cut_off_balance
add constraint cut_off_balance_intm_subacc_processdt_uidx1 unique (intermediary_code, sub_account_number, process_cut_off_date);


-- ****************************************************
-- *                 account_reference                *
-- ****************************************************
create table account_reference (
   gl_account                       varchar(5) not null,
   gl_account_description           varchar(255) not null,
   gl_cost_center_type_required     varchar(20) not null,
   gl_analysis_code_length_required varchar(5) not null,
   is_gl_segment3_required          bit not null,
   is_intermediary_code_required    bit not null,
   is_sub_account_required          bit not null,
   is_plan_number_required          varchar(255) not null,
   fixed_gl_company_code            varchar(4),
   fixed_gl_cost_centre             varchar(6),
   analysis_code_automated          varchar(255),
   is_manual_transaction_eligible   bit not null,
   reporting_view_selector          varchar(255) not null,
   post_plan_number_to_walker       bit not null,
   dr_recon_indicator               varchar(1),
   cr_recon_indicator               varchar(1),
   reporting_group                  varchar(90),
   constraint PK__account___9B7FC9E65EDF1D42 primary key (gl_account)
);


-- ****************************************************
-- *         tmp_employee_garnishee_order_view        *
-- ****************************************************
create table tmp_employee_garnishee_order_view (
   employee_garnishee_order_id bigint not null,
   intermediary_code           varchar(8) not null,
   account_holder_name         varchar(50) not null,
   garnishe_key                varchar(50) not null,
   cut_off_date                date not null,
   amount_deducted             bigint not null,
   admin_amount                bigint not null,
   bank_name                   varchar(50),
   bank_branch_code            varchar(50),
   bank_account_type           varchar(50),
   bank_account_number         varchar(50),
   constraint PK__tmp_empl__4030FBA89CEE0413 primary key (employee_garnishee_order_id)
);


-- ****************************************************
-- *              user_generation_number              *
-- ****************************************************
create table user_generation_number (
   user_generation_number_id bigint not null,
   transmission_number       bigint not null,
   user_generation_number    bigint not null,
   record_identifier         bigint not null,
   action_date               date not null,
   user_code                 bigint not null,
   transmission_type         varchar(6) not null,
   status                    varchar(6) not null,
   last_sequence_number      bigint not null,
   created_date              datetime not null,
   accepted_date             datetime,
   error_code                bigint,
   error_message             varchar(60),
   rejected_date             datetime,
   corrected_by              varchar(7),
   resubmitted_date          datetime,
   constraint PK__user_gen__57C7D4454F94834A primary key (user_generation_number_id)
);


-- ****************************************************
-- *                     error_log                    *
-- ****************************************************
create table error_log (
   error_log_id                  bigint not null,
   batch_number                  bigint,
   error_type                    varchar(30) not null,
   source_system                 varchar(255),
   source_system_reference       varchar(30),
   business_event_type           int,
   intermediary_code             varchar(8),
   error_codes                   varchar(255),
   message                       varchar(max),
   additional_message            varchar(max),
   job_execution_id              bigint,
   severity                      varchar(30),
   cut_off_date                  date,
   run_date                      date,
   status                        varchar(30),
   job_name                      varchar(100),
   step_name                     varchar(100),
   persistent_step_short_context varchar(2500),
   persistent_job_short_context  varchar(2500),
   transient_job_short_context   varchar(2500),
   created_timestamp             datetime not null,
   updated_timestamp             datetime,
   updated_by                    varchar(10),
   constraint PK__error_lo__D67FEBA39281B251 primary key (error_log_id)
);


-- ****************************************************
-- *                     changelog                    *
-- ****************************************************
create table changelog (
   ID          numeric not null,
   APPLIED_AT  varchar(25) not null,
   DESCRIPTION varchar(255) not null,
   constraint PK_changelog primary key (ID)
);


-- ****************************************************
-- *           agt_finance_m0_int_migration           *
-- ****************************************************
create table agt_finance_m0_int_migration (
   m0_int_migration_Id              bigint not null,
   intermediary_code                varchar(8) not null,
   financial_date                   date not null,
   interest_charged_on_rem          bigint not null,
   deposit                          bigint not null,
   vat_transfer_from_individuals    bigint not null,
   amount_transfer_from_individuals bigint not null,
   brokerage_income                 bigint not null,
   skip_for_migration               int,
   constraint PK__agt_fina__F7BB031ABD804610 primary key (m0_int_migration_Id)
);


-- ****************************************************
-- *               source_business_event              *
-- ****************************************************
create table source_business_event (
   source_business_event_id            bigint not null,
   amount_in_cents                     bigint,
   fee_amount_in_cents                 bigint,
   business_event_type                 int,
   transaction_date                    varchar(20),
   source_system                       varchar(255),
   source_system_reference             varchar(30),
   originating_source_system_reference varchar(30),
   intermediary_code                   varchar(8),
   transaction_category_id             varchar(10),
   fee_transaction_category_id         varchar(10),
   cost_center                         varchar(6),
   tax_fund                            varchar(1),
   product_code                        varchar(4),
   bonus_type                          varchar(1),
   kbr_indicator                       varchar(1),
   n_fund_split                        varchar(1),
   plan_number                         varchar(50),
   event_effective_date                varchar(20),
   origin                              varchar(3),
   product_provider                    varchar(2),
   currency                            varchar(1),
   commission_movement_type            varchar(3),
   plan_country                        varchar(1),
   vat_amount_in_cents                 bigint,
   vat_applicable_indicator            bit,
   retroactive_vat_indicator           bit,
   cut_off_period                      varchar(20),
   loan_number                         varchar(2),
   batch_number                        bigint,
   processing_status                   varchar(30),
   created_timestamp                   datetime,
   updated_timestamp                   datetime,
   constraint PK__source_b__AB00AE35262BC639 primary key (source_business_event_id)
);


-- ****************************************************
-- *     tmp_intermediary_agreement_attribute_view    *
-- ****************************************************
create table tmp_intermediary_agreement_attribute_view (
   attribute_name    varchar(80),
   boolean_value     bit,
   date_value        date,
   intermediary_code varchar(8) not null,
   component_name    varchar(80) not null);

alter table tmp_intermediary_agreement_attribute_view
add constraint tmp_intermediary_agreement_attribute_view_unique_idx unique (attribute_name, intermediary_code, component_name);

create index IDX_DBA_SL0166 on tmp_intermediary_agreement_attribute_view(intermediary_code, component_name);


-- ****************************************************
-- *       business_account_entry_type_reference      *
-- ****************************************************
create table business_account_entry_type_reference (
   reference_data_code          varchar(20) not null,
   include_in_balance           bit,
   rejection_type               varchar(5),
   affects_capitalised_interest bit,
   include_in_statement_view    bit,
   nett_interest                bit,
   constraint PK__business__84E95A818C561E74 primary key (reference_data_code)
);


-- ****************************************************
-- *                  fringe_benefit                  *
-- ****************************************************
create table fringe_benefit (
   fringe_benefit_id     bigint not null,
   intermediary_code     varchar(8) not null,
   cut_off_date          date not null,
   fringe_benefit_amount bigint not null,
   fringe_benefit_status varchar(10) not null,
   constraint PK__fringe_b__528CCB7C1930C91B primary key (fringe_benefit_id)
);


-- ****************************************************
-- *                   payment_recon                  *
-- ****************************************************
create table payment_recon (
   payment_recon_id          bigint not null,
   intermediary_code         varchar(8) not null,
   payment_amount_in_cents   bigint not null,
   statement_amount_in_cents bigint not null,
   payment_schedule_type     varchar(50) not null,
   cut_off_period            date not null,
   process_cut_off_date      date not null,
   created_timestamp         datetime not null);

alter table payment_recon
add constraint payment_recon_uidx1 unique (intermediary_code, cut_off_period, process_cut_off_date);


-- ****************************************************
-- *            agt_finance_loan_migration            *
-- ****************************************************
create table agt_finance_loan_migration (
   agt_finance_loan_migration_id                  bigint not null,
   intermediary_code                              varchar(8) not null,
   loan_number                                    varchar(2) not null,
   loan_type                                      varchar(4) not null,
   loan_settlement_amount_in_cents                bigint not null,
   nca_indicator                                  bit,
   loan_inception_date                            datetime,
   reason_code                                    varchar(30),
   loan_amount_in_cents                           bigint not null,
   instalment_amount_in_cents                     bigint not null,
   interest_due_amount_in_cents                   bigint,
   term                                           int not null,
   outstanding_term                               int not null,
   home_inc                                       bit,
   rejected                                       bit,
   rejected_reason                                int,
   written_back                                   int,
   partition_number                               int,
   loan_date                                      datetime,
   lawyer_code                                    varchar(5),
   lawyer_name                                    varchar(30),
   lawyer_dial_code                               varchar(6),
   lawyer_telephone_number                        varchar(10),
   termination_date                               datetime,
   last_contract                                  int,
   self_buy                                       bit,
   sell                                           varchar(1),
   supplier                                       varchar(30),
   associate_code                                 varchar(8),
   interest_rate                                  decimal,
   migration_period                               int,
   interest_charged_on_settlement_amount_in_cents bigint,
   loan_capital_outstanding_amount_in_cents       bigint,
   constraint PK__agt_fina__ED867DAAB86EFB12 primary key (agt_finance_loan_migration_id)
);


-- ****************************************************
-- *               agt_finance_migration              *
-- ****************************************************
create table agt_finance_migration (
   agt_finance_migration_id                                            bigint not null,
   debt_inception_date                                                 datetime,
   intermediary_code                                                   varchar(8) not null,
   financing_balance_amount_in_cents                                   bigint,
   nett_interest_amount_in_cents                                       bigint,
   vesting_assistance_amount_in_cents                                  bigint,
   finance_loss_written_off_amount_in_cents                            bigint,
   namibia_old_company_commission                                      bigint,
   bridging_bonus_paid_amount_in_cents                                 bigint,
   bridging_loan_paid_amount_in_cents                                  bigint,
   bridging_written_off_amount_in_cents                                bigint,
   finance_loss_indication                                             varchar(1),
   finance_loss_reason                                                 varchar(30),
   credit_written_off_amount_in_cents                                  bigint,
   write_off_date                                                      datetime,
   j_k_indication_date                                                 datetime,
   debt_management_balance_amount_in_cents                             bigint,
   latest_deposit_amount_in_cents                                      bigint,
   cumulative_deposit_amount_in_cents                                  bigint,
   latest_deposit_date                                                 datetime,
   fringe_benefit_amount_in_cents                                      bigint,
   interest_charged_current_debt_amount_in_cents                       bigint,
   interest_start_date                                                 datetime,
   low_interest_fringe_benefit_amount_in_cents                         bigint,
   nca_loan_settlement_amount_in_cents                                 bigint,
   non_nca_loan_settlement_amount_in_cents                             bigint,
   total_current_interest_charged_amount_in_cents                      bigint,
   total_previous_interest_charged_amount_in_cents                     bigint,
   total_current_deposit_received_amount_in_cents                      bigint,
   total_current_credit_transferred_amount_in_cents                    bigint,
   total_previous_deposit_received_amount_in_cents                     bigint,
   total_previous_credit_transferred_amount_in_cents                   bigint,
   unapplied_credit_received_amount_in_cents                           bigint,
   hand_over_amount_in_cents                                           bigint,
   ccma_indicator                                                      varchar(30),
   contract_code_1                                                     int,
   contract_indicator_1                                                int,
   contract_code_2                                                     int,
   contract_indicator_2                                                int,
   contract_code_3                                                     int,
   contract_indicator_3                                                int,
   contract_code_4                                                     int,
   contract_indicator_4                                                int,
   contract_code_5                                                     int,
   contract_indicator_5                                                int,
   contract_code_end_date_1                                            date,
   contract_code_end_date_2                                            date,
   contract_code_end_date_3                                            date,
   contract_code_end_date_4                                            date,
   contract_code_end_date_5                                            date,
   lapses_from_date_of_debt_amount_in_cents                            bigint,
   package_s_additional_vesting_assistance_written_backamount_in_cents bigint,
   retention_offer_clawed_back_amount_in_cents                         bigint,
   contract_code_start_date_1                                          date,
   contract_code_start_date_2                                          date,
   contract_code_start_date_3                                          date,
   contract_code_start_date_4                                          date,
   contract_code_start_date_5                                          date,
   bridging_bonus_clawback_amount_in_cents                             bigint,
   bridging_loan_clawback_amount_in_cents                              bigint,
   final_demand_date                                                   date,
   performance_bonus_paid_clawback_in_cents                            bigint,
   link_codes_income_current_month_amount_in_cents                     bigint,
   link_codes_income_current_month_minus_1_amount_in_cents             bigint,
   link_codes_income_current_month_minus_2_amount_in_cents             bigint,
   link_codes_income_current_month_minus_3_amount_in_cents             bigint,
   link_codes_income_current_month_minus_4_amount_in_cents             bigint,
   link_codes_income_current_month_minus_5_amount_in_cents             bigint,
   link_codes_income_current_month_minus_6_amount_in_cents             bigint,
   link_codes_income_current_month_minus_7_amount_in_cents             bigint,
   link_codes_income_current_month_minus_8_amount_in_cents             bigint,
   link_codes_income_current_month_minus_9_amount_in_cents             bigint,
   link_codes_income_current_month_minus_10_amount_in_cents            bigint,
   link_codes_income_current_month_minus_11_amount_in_cents            bigint,
   successfully_migrated                                               bit,
   successfully_processed                                              bit,
   total_interest_charged_amount_in_cents                              bigint,
   termination_transaction_date                                        date,
   start_transaction_date                                              date,
   in_duplum_effective_date                                            date,
   constraint PK__agt_fina__A1A77B2935A74A5C primary key (agt_finance_migration_id)
);

create unique index intermediary_code_idx on agt_finance_migration(intermediary_code);


-- ****************************************************
-- *           business_event_type_component          *
-- ****************************************************
create table business_event_type_component (
   component_id          bigint not null,
   component_name        varchar(100) not null,
   component_description varchar(100),
   business_event_type   int not null,
   constraint PK__business__AEB1DA59DA4E4F5A primary key (component_id)
);

alter table business_event_type_component
add constraint business_event_type_component_unique_idx unique (component_name, business_event_type);


-- ****************************************************
-- *          agt_finance_dod_bals_migration          *
-- ****************************************************
create table agt_finance_dod_bals_migration (
   dod_bals_migration_id   bigint not null,
   intermediary_code       varchar(8) not null,
   financial_date          date not null,
   current_month_balance   bigint not null,
   finance_balance         bigint not null,
   reserve                 bigint not null,
   debt_management_balance bigint not null,
   constraint PK__agt_fina__0626F98A16DAAAF8 primary key (dod_bals_migration_id)
);


-- ****************************************************
-- *               tmp_cost_centre_view               *
-- ****************************************************
create table tmp_cost_centre_view (
   cost_centre char(6) not null,
   descr       char(30) not null,
   status      char(1) not null,
   status_date date not null,
   cpy         varchar(4) not null,
   cpy_descr   varchar(30));

create index tmp_cost_centre_view_idx1 on tmp_cost_centre_view(cost_centre, cpy);

create index tmp_cost_centre_view_idx2 on tmp_cost_centre_view(cost_centre, descr, status, status_date);


-- ****************************************************
-- *              tmp_intermediary_event              *
-- ****************************************************
create table tmp_intermediary_event (
   event_id          bigint not null,
   intermediary_code varchar(8) not null,
   effective_date    date not null,
   created_timestamp datetime,
   constraint PK__tmp_inte__2370F7276D03D0CC primary key (event_id)
);


-- ****************************************************
-- *                     reversal                     *
-- ****************************************************
create table reversal (
   business_event_reversal_id bigint not null,
   source_system              varchar(50) not null,
   source_system_reference    varchar(50) not null,
   reversal_reference         varchar(50),
   status                     varchar(50),
   constraint PK__reversal__7E8B4DC129FBC11A primary key (business_event_reversal_id)
);


-- ****************************************************
-- *             agt_finance_b29_migration            *
-- ****************************************************
create table agt_finance_b29_migration (
   b29_migration_id                          bigint not null,
   intermediary_code                         varchar(8) not null,
   financial_date                            datetime,
   deposit_amounts_in_cents                  bigint,
   defaulted_amounts_in_cents                bigint,
   interest_charged_amounts_in_cents         bigint,
   credit_transfer_from_101_amounts_in_cents bigint,
   current_balance_amount_in_cents           bigint,
   migration_period                          int,
   credits_migrated                          bit,
   constraint PK__agt_fina__F4ADD561E8F68F7A primary key (b29_migration_id)
);


-- ****************************************************
-- *                  error_reference                 *
-- ****************************************************
create table error_reference (
   error_code        varchar(10) not null,
   error_description varchar(100),
   constraint PK__error_re__018D511060583FB2 primary key (error_code)
);


-- ****************************************************
-- *         business_account_entry_delete_tmp        *
-- ****************************************************
create table business_account_entry_delete_tmp (
   business_account_entry_id bigint not null);


-- ****************************************************
-- *              funds_transfer_control              *
-- ****************************************************
create table funds_transfer_control (
   funds_transfer_control_id            bigint not null,
   electronic_banking_suite_code        bigint not null,
   electronic_banking_suite_name        varchar(30) not null,
   transmission_number                  bigint not null,
   absa_transmission_number             bigint not null,
   data_set_status                      varchar(1) not null,
   eft_payment_cheque_number            bigint,
   payment_rejections_cheque_number     bigint,
   debit_order_cheque_number            bigint,
   debit_order_rejections_cheque_number bigint,
   generation_number                    bigint,
   sequence_number                      bigint,
   constraint PK__funds_tr__6F96A8D3EBE44A37 primary key (funds_transfer_control_id)
);


-- ****************************************************
-- *       tmp_intermediary_amount_payable_view       *
-- ****************************************************
create table tmp_intermediary_amount_payable_view (
   intermediary_code    varchar(8) not null,
   cut_off_period       date not null,
   amount_in_cents      bigint not null,
   process_cut_off_date date not null);

alter table tmp_intermediary_amount_payable_view
add constraint tmp_intermediary_amount_payable_view_uidx1 unique (intermediary_code, cut_off_period, process_cut_off_date);


-- ****************************************************
-- *            business_account_delete_tmp           *
-- ****************************************************
create table business_account_delete_tmp (
   business_account_id bigint not null);


-- ****************************************************
-- *                 program_reference                *
-- ****************************************************
create table program_reference (
   code              varchar(3) not null,
   posting_frequency varchar(1) not null,
   description       varchar(60) not null,
   constraint PK__program___357D4CF8479219BA primary key (code)
);


-- ****************************************************
-- *              cut_off_balance_staging             *
-- ****************************************************
create table cut_off_balance_staging (
   cut_off_balance_id                       bigint not null,
   intermediary_code                        varchar(8) not null,
   sub_account_number                       int not null,
   sub_account_description                  varchar(50) not null,
   process_cut_off_date                     date not null,
   opening_balance_in_cents                 bigint not null,
   process_cut_off_balance_in_cents         bigint not null,
   opening_debt_management_balance_in_cents bigint,
   balance_paid_in_cents_this_period        bigint);


-- ****************************************************
-- *          debt_management_profiling_rule          *
-- ****************************************************
create table debt_management_profiling_rule (
   debt_management_profiling_rule_id     bigint not null,
   debt_management_record_status         varchar(20),
   intermediary_type                     varchar(20),
   intermediary_status                   varchar(20),
   debt_management_category              varchar(100),
   bluestar_principal_status             varchar(20),
   historical_linked_active_code_profile varchar(50),
   nett_debt_in_cents                    varchar(20),
   nca_installment_paid                  varchar(20),
   admin_area_result                     varchar(50),
   reporting_group_result                varchar(50),
   report_result                         varchar(50),
   ledger                                varchar(20),
   ceded_to_debt_collector               varchar(20),
   debt_collector_result                 varchar(50),
   debt_collector_reference_result       varchar(50),
   constraint PK__debt_man__077EC41E8404D408 primary key (debt_management_profiling_rule_id)
);


-- ****************************************************
-- *              debt_profiling_staging              *
-- ****************************************************
create table debt_profiling_staging (
   debt_management_id           bigint not null,
   intermediary_code            varchar(8) not null,
   ledger_code                  varchar(3) not null,
   intermediary_status          varchar(10) not null,
   intermediary_type            varchar(7) not null,
   nett_balance_in_cents        bigint not null,
   is_bluestar_control_account  bit,
   principal_intermediary_code  varchar(8),
   is_bluestar_principal_active bit,
   is_associate_advisor         bit,
   is_additional_broker         bit,
   mngmt_admin_area_id          bigint,
   administration_area_code     varchar(10),
   cession_detail_id            bigint,
   actual_debt_collector_id     varchar(15),
   internal_debt_collector_id   varchar(15),
   external_debt_collector_id   varchar(15),
   constraint PK__debt_pro__54C60D73414170B3 primary key (debt_management_id)
);


-- ****************************************************
-- *               tmp_nca_loan_default               *
-- ****************************************************
create table tmp_nca_loan_default (
   intermediary_code         varchar(8) not null,
   cut_off_date              date not null,
   opening_balance           bigint,
   payments_applied          bigint,
   new_loan_defaults         bigint,
   interest_on_loan_defaults bigint,
   closing_balance           bigint,
   income_applied            bigint);

create unique index tmp_nca_loan_default_idx1 on tmp_nca_loan_default(intermediary_code, cut_off_date);


-- ****************************************************
-- *                transmission_number               *
-- ****************************************************
create table transmission_number (
   transmission_number_id bigint not null,
   transmission_number    bigint not null,
   status                 varchar(5) not null,
   created_date           datetime not null,
   accepted_date          datetime,
   error_code             bigint,
   error_message          varchar(60),
   rejected_date          datetime,
   corrected_by           varchar(7),
   resubmitted_date       datetime,
   constraint PK__transmis__26BB1CFDA317A50F primary key (transmission_number_id)
);


-- ****************************************************
-- *                gl_error_code_table               *
-- ****************************************************
create table gl_error_code_table (
   gl_error_code        varchar(2) not null,
   gl_error_description varchar(40),
   constraint PK__gl_error__1977C2CD04722CCE primary key (gl_error_code)
);


-- ****************************************************
-- *             absa_rejection_qualifier             *
-- ****************************************************
create table absa_rejection_qualifier (
   rejection_qualifier_code        varchar(5) not null,
   rejection_qualifier_description varchar(60) not null,
   constraint PK__absa_rej__FA064777CD548FAF primary key (rejection_qualifier_code)
);


-- ****************************************************
-- *             tmp_intermediary_profile             *
-- ****************************************************
create table tmp_intermediary_profile (
   intermediary_code                              varchar(8) not null,
   is_bluestar_control_account                    bit not null,
   is_broker_control_account                      bit not null,
   is_associate_advisor                           bit not null,
   is_sky_cross_sell                              bit not null,
   is_sks                                         bit not null,
   is_bluestar_principal_active                   bit not null,
   nett_balance_in_cents                          bigint,
   second_year_guarantee_balance_in_cents         bigint,
   reserve_balance_in_cents                       bigint,
   unvested_comission_balance_in_cents            bigint,
   deposit_in_current_month                       bit,
   nca_installment_paid                           bit,
   closing_balance_in_cents                       bigint,
   principal_intermediary_code                    varchar(8),
   is_additional_broker                           bit not null,
   nca_default_amount_in_cents                    bigint,
   nca_loan_settlements_amount_in_cents           bigint,
   non_nca_defaults_amount_in_cents               bigint,
   non_nca_loan_settlements_amount_in_cents       bigint,
   falcon_club_clawback_amount_in_cents           bigint,
   retention_payment_clawback_amount_in_cents     bigint,
   investment_recoupment_clawback_amount_in_cents bigint,
   vesting_support_clawback_amount_in_cents       bigint,
   constraint PK__tmp_inte__EAEE9AEEDA38E8DD primary key (intermediary_code)
);

create index tmp_intermediary_profile_nett_balance_in_cents_idx on tmp_intermediary_profile(intermediary_code, nett_balance_in_cents);


-- ****************************************************
-- *                      comment                     *
-- ****************************************************
create table comment (
   comment_id bigint not null,
   text       varchar(255) not null,
   timestamp  datetime not null,
   login_name varchar(7) not null,
   full_name  varchar(100) not null,
   constraint PK__comment__E7957687A833EBBF primary key (comment_id)
);


