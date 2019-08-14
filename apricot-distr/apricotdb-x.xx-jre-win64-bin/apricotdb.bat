set JAVA_HOME=.\jre1.8.0_212
.\jre1.8.0_212\bin\java -jar apricot-launcher-0.6.jar
if errorlevel 1 goto fail
.\jre1.8.0_212\bin\java -Xms1024m -Xmx2048m -jar apricot-ui-0.6.jar
:fail