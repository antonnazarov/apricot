set MAVEN_OPTS=-Xms2048m -Xmx4096m
mvn -e exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="za.co.apricotdb.ui.ApricotMainApp"
REM mvn javafx:run