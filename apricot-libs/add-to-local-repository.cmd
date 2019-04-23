call mvn install:install-file -Dfile=ojdbc7.jar -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=postgresql-42.2.5.jar -DgroupId=org.postgresql -DartifactId=postgresql -Dversion=42.2.5 -Dpackaging=jar
call mvn install:install-file -Dfile=mysql-connector-java-8.0.15.jar -DgroupId=org.mysql -DartifactId=mysql-connector -Dversion=8.0.15 -Dpackaging=jar
