set MAVEN_OPTS=-Xms1024m -Xmx2048m
mvn -e exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="za.co.apricotdb.ui.ApricotMainApp" -Djavax.net.debug=ssl:handshake:verbose
REM mvn javafx:run