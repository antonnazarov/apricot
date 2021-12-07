REM call mvn install:install-file -Dfile=ojdbc7.jar -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=postgresql-42.2.5.jar -DgroupId=org.postgresql -DartifactId=postgresql -Dversion=42.2.5 -Dpackaging=jar
call mvn install:install-file -Dfile=mysql-connector-java-8.0.15.jar -DgroupId=org.mysql -DartifactId=mysql-connector -Dversion=8.0.15 -Dpackaging=jar
call mvn install:install-file -Dfile=db2jcc.jar -DgroupId=com.ibm.db2 -DartifactId=db2-driver -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=db2jcc_license_cisuz.jar -DgroupId=com.ibm.db2 -DartifactId=license_cisuz -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=db2jcc_license_cu.jar -DgroupId=com.ibm.db2 -DartifactId=license_cu -Dversion=1.0 -Dpackaging=jar
REM updated 22/05/2021
call mvn install:install-file -Dfile=ojdbc11.jar -DgroupId=com.oracle -DartifactId=ojdbc11 -Dversion=21.1 -Dpackaging=jar
call mvn install:install-file -Dfile=orai18n.jar -DgroupId=com.oracle -DartifactId=orai18n -Dversion=21.1 -Dpackaging=jar

