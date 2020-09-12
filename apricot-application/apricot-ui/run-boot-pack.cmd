set JAVA_HOME=C:\jdk-13
set PATH=C:\jdk-13\bin;%PATH%
REM echo JAVA_HOME=%JAVA_HOME%
REM echo PATH=%PATH%
java -version

cd target
java -jar apricot-ui-2.4.jar
