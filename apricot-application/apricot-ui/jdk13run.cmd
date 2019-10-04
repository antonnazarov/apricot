set JAVA_HOME=C:\jdk-13
set PATH=C:\jdk-13\bin;%PATH%
REM echo JAVA_HOME=%JAVA_HOME%
REM echo PATH=%PATH%
java -version

set MAVEN_OPTS=-Xms1024m -Xmx2048m
mvn javafx:run
REM mvn -e exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="za.co.apricotdb.ui.ApricotMainApp"