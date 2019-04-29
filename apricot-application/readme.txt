----------------------------
ApricotDB, Release 0.3, 21/04/2019
----------------------------
This version of "Apricot DB" covers most of the crucial functions planned for the application.
Release 0.3 allows to create new Projects, Snapshots inside the Projects, create and edit Views, 
create and edit Entities and Relationships between them.
For more details please refer to the user documentation: "Apricot DB User Guide v0.3.pdf".
This version of "Apricot DB" provides the early support for the Reverse Engineering of the following databases:
Oracle;
SQL Server;
MySQL;
PostgreSQL;
H2

----------------------------
The "Apricot DB" Installation
----------------------------
The following 3 packages are provided:
apricotdb-0.3-jre-win64-bin.zip -> the self contained package for Windows;
apricotdb-0.3-all-bin.zip	-> the native multiplatform package;
apricotdb-0.3-src.zip		-> the source code of Apricot DB.

----------------------------
* Option A (for Windows x64 only): installation from the self contained package for Windows.
This package contains the Java Machine required to run "Apricot DB".
----------------------------
1. Download and unzip the archive: apricotdb-0.3-jre-win64-bin.zip
2. The following scripts need to be run in the shell: apricotdb.bat

----------------------------
* Option B (multi platform): installation from the the multi platform archive
----------------------------
A. Install Java 1.8 (not tested on earlier or later versions, therefore Java 1.8 is essential).
Set JAVA_HOME system variable, and include <JAVA_HOME>/bin into your system PATH.
1. Download and unzip the archive: apricotdb-0.3-all-bin.zip
2. The following scripts need to be run in the shell:
java -jar apricot-launcher-0.3.jar
java -jar apricot-ui-0.3.jar

----------------------------
* Option C (multi platform): compiling and running from the sources
----------------------------
A. Install Java 1.8 (not tested on earlier or later versions, therefore Java 1.8 is essential).
Set JAVA_HOME system variable, and include <JAVA_HOME>/bin into your system PATH.
B. Install Maven. I use apache-maven-3.3.9. The later versions will fe fine too.
Add Maven's bin- catalogue to your system PATH.
1. Download and unzip apricotdb-0.3-src.zip
2. In apricot-application catalogue compile the application using the following command:
mvn -e clean install
(ensure, that there is no exceptions during the compilation process and the compilation was successfully finished)
3. Run the application, using the following script:
apricot-application/apricot-ui/run.cmd

