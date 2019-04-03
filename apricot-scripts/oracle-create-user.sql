CREATE USER intermediary_account IDENTIFIED BY password;
GRANT CONNECT TO intermediary_account;
GRANT CONNECT, RESOURCE, DBA TO intermediary_account;
GRANT CREATE SESSION TO intermediary_account;
GRANT UNLIMITED TABLESPACE TO intermediary_account;