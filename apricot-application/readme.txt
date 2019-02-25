----------------------------
ApricotDB, Release 0.2 (early pre-alpha)
----------------------------
This version of the "Apricot DB" application is an early concept.
The concept allows to play with the testing entity/relationship diagram. This is not a 
fully functional application so far.

----------------------------
The "Apricot DB" Installation
----------------------------
The following 3 packages are provided:
apricotdb-0.2-jre-win64-bin.zip -> the self contained package for Windows;
apricotdb-0.2-all-bin.zip	-> the native multiplatform package;
apricotdb-0.2-src.zip		-> the source code of Apricot DB.

----------------------------
* Option A (for Windows x64 only): installation from the self contained package for Windows.
This package contains the Java Machine required to run "Apricot DB".
----------------------------
1. Download and unzip the archive: apricotdb-0.2-jre-win64-bin.zip
2. The following scripts need to be run in the shell: apricotdb.bat

----------------------------
* Option B (multi platform): installation from the the multi platform archive
----------------------------
A. Install Java 1.8 (not tested on earlier or later versions, therefore Java 1.8 is essential).
Set JAVA_HOME system variable, and include <JAVA_HOME>/bin into your system PATH.
1. Download and unzip the archive: apricotdb-0.2-all-bin.zip
2. The following scripts need to be run in the shell:
java -jar apricot-launcher-0.2.jar
java -jar apricot-ui-0.2.jar

----------------------------
* Option C (multi platform): running from the sources
----------------------------
A. Install Java 1.8 (not tested on earlier or later versions, therefore Java 1.8 is essential).
Set JAVA_HOME system variable, and include <JAVA_HOME>/bin into your system PATH.
B. Install Maven. I use apache-maven-3.3.9. The later versions will fe fine too.
Add Maven's bin- catalogue to your system PATH.
1. Download and unzip apricotdb-0.2-src.zip
2. In apricot-application catalogue compile the application using the following command:
mvn -e clean install
(ensure, that there is no exceptions during the compilation process and the compilation was successfully finished)
3. Run the application, using the following script:
apricot-application/apricot-ui/run.cmd

