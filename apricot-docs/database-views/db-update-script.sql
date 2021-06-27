--******************************************
--             THE NEW TABLES               
--******************************************
create table APRICOT_DBVIEW (
   DB_VIEW_ID         BIGINT identity primary key,
   DB_VIEW_NAME       VARCHAR(200) not null,
   DB_VIEW_DEFINITION VARCHAR not null,
   SNAPSHOT_ID        BIGINT not null
);

create table APRICOT_DBVIEW_COLUMN (
   DBVIEW_COLUMN_ID               BIGINT identity primary key,
   DBVIEW_COLUMN_NAME             VARCHAR(100) not null,
   DBVIEW_COLUMN_ORDINAL_POSITION INTEGER not null,
   DBVIEW_COLUMN_IS_NULLABLE      BOOLEAN not null,
   DBVIEW_COLUMN_DATA_TYPE        VARCHAR(25) not null,
   DBVIEW_COLUMN_VALUE_LENGTH     VARCHAR(10),
   DB_VIEW_ID                     BIGINT not null
);

create table APRICOT_DBVIEW_RELATED_TABLE (
   REL_TABLE_ID      BIGINT identity primary key,
   REL_TABLE_NAME    VARCHAR(100) not null,
   REL_TABLE_COLUMNS VARCHAR(2000),
   DB_VIEW_ID        BIGINT not null
);

--******************************************
--    THE CONSTRAINTS OF THE NEW TABLES     
--******************************************
alter table APRICOT_DBVIEW
   add constraint FK_SNAPSHOT_TO_DBVIEW
   foreign key (SNAPSHOT_ID) references APRICOT_SNAPSHOT (SNAPSHOT_ID);

alter table APRICOT_DBVIEW_RELATED_TABLE
   add constraint FK_DBVIEW_TO_RELTABLE
   foreign key (DB_VIEW_ID) references APRICOT_DBVIEW (DB_VIEW_ID);

alter table APRICOT_DBVIEW_COLUMN
   add constraint FK_DBVIEW_TO_COLUMN
   foreign key (DB_VIEW_ID) references APRICOT_DBVIEW (DB_VIEW_ID);





-- SQL Server
