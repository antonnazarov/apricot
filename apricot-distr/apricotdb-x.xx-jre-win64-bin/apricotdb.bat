set JAVA_HOME=.\jre1.8.0_181
.\jre1.8.0_181\bin\java -jar apricot-launcher-0.1.jar
if errorlevel 1 goto fail
.\jre1.8.0_181\bin\java -jar apricot-ui-0.1.jar
:fail