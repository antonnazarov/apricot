----------------------------
Apricot DB, Release 0.5b, 07/06/2019
----------------------------
This is the bugfix release after Release 0.5.

For more details please refer to the user documentation: "Apricot DB User Guide v0.5.pdf".
This version of "Apricot DB" provides the Reverse Engineering of the following databases:
Oracle;
SQL Server;
MySQL;
PostgreSQL;
DB2;
H2

----------------------------
Apricot DB Installation
----------------------------
The following 3 packages are available:
apricotdb-0.5b-jre-win64-bin.zip -> the self contained package for Windows;
apricotdb-0.5b-nojre-bin.zip	 -> the native multiplatform package;
apricotdb-0.5b-src.zip		 -> the source code of Apricot DB.

----------------------------
* Option A (for Windows x64 only): installation from the self contained package for Windows.
This package includes the Windows version of Java Machine (Java(TM) SE Runtime Environment (build 1.8.0_202-b08)).
----------------------------
1. Download and unzip the archive: apricotdb-0.5b-jre-win64-bin.zip
2. Run the script: apricotdb.bat

----------------------------
* Option B (multi platform): installation from the the multi platform archive
----------------------------
A. Download and install Java 1.8 for the platform of your choice ("Apricot DB" was not tested on earlier or later versions, 
therefore Java 1.8 is essential).
Set JAVA_HOME system variable, and include <JAVA_HOME>/bin into your system PATH.
1. Download and unzip the archive: apricotdb-0.5b-nojre-bin.zip
2. The following scripts need to be run in the shell:
java -jar apricot-launcher-0.5b.jar
java -Xms1024m -Xmx2048m -jar apricot-ui-0.5.jar
Note: the apricot-launcher-0.5b.jar script needs to be run only once on the first run of the application.

----------------------------
* Option C (multi platform): compiling and running from the sources
----------------------------
A. Install Java 1.8 (not tested on earlier or later versions, therefore Java 1.8 is essential).
Set JAVA_HOME system variable, and include <JAVA_HOME>/bin into your system PATH.
B. Install Maven. I use apache-maven-3.3.9. The later versions will fe fine too.
Add Maven's bin- catalogue to your system PATH.
C. Add the JDBC driver libraries to the local Maven repository. Run the following script:
apricot/apricot-libs/add-to-local-repository.cmd
1. Download and unzip apricotdb-0.5b-src.zip
2. In apricot-application catalogue compile the application using the following command:
mvn -e clean install
(ensure, that there is no exceptions during the compilation process and the compilation was successfully finished)
3. Run the application, using the following script:
apricot-application/apricot-ui/run.cmd

