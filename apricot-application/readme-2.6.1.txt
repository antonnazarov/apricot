------------------------------------------
Apricot DB, Release 2.6.1, October 2021
------------------------------------------
* Fixed MariaDB/MySQL connection form. No Database is required. Only schema has been used;
* The comments of the field definitions have been added (the entity fields edit form);  

* Feature: the Reverse Engineering into a non empty Snapshot has been allowed;
* Fix: The procedure of the comparison of the Snapshots has been improved: no difference if the primary keys contain the same fields;
* Fix: The logging was configured, reimplemented;
* Fix: The optimization of the canvas renewal after the existing Entity is edited;
* Fix: Asynchronous switch between tabs has been implemented;
* Fix: The connection to the Oracle database has been improved - now it supprorts Service, SID and TNS;
* Other minor fixes
....................................

This version of "Apricot DB" provides the Reverse Engineering of the following databases:
Oracle;
SQL Server;
MySQL;
MariaDB;
PostgreSQL;
DB2;
DB2 LUW;
H2;
SQLite

----------------------------
Apricot DB Installation
----------------------------
1. Unzip apricotdb-2.6-windows-x64.zip;
2. Run apricotdb.exe in the apricot directory
