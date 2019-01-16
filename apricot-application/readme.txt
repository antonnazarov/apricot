This version of the "Apricot DB" application is an early concept.
There is no working functionality in the menu items.
The concept allows to play with the testing entity/relationship diagram
in the View Port.

----------------------------
The Apricot DB Installation
----------------------------

----------------------------
Prerequisites
----------------------------
A. Install Java 1.8 (not tested on earlier or later versions, therefore Java 1.8 is essential).
Set JAVA_HOME system variable, and include <JAVA_HOME>/bin into your system PATH.

B. Install Maven. I use apache-maven-3.3.9. The later versions will fe fine too.
Add Maven's bin- catalogue to your system PATH.

----------------------------
Installation
----------------------------
1. Unzip the archive.
2. Copy the file apricot-db/apricot-project.mv.db into the catalogue <user home>/.apricotdb
3. Go to apricot-application catalogue and compile the application using the following command:
mvn -e clean install
(ensure, that there is no exceptions during the compilation process and the compilation was successfully finished)
4. Run the application, using the following script:
apricot-application/apricot-ui/run.cmd


