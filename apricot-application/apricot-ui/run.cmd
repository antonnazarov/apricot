set MAVEN_OPTS=-Xms1024m -Xmx2048m
REM mvn -e exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="za.co.apricotdb.ui.ApricotMainApp"
mvn javafx:run