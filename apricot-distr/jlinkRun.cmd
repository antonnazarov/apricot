set JAVA_HOME=C:\jdk-13
set PATH=C:\jdk-13\bin;%PATH%
java -version

jlink --no-header-files --no-man-pages --compress=2 --strip-debug --add-modules java.base,java.scripting,java.xml,java.sql,java.desktop,java.management,java.naming,java.instrument,jdk.unsupported,jdk.unsupported.desktop --output java-runtime